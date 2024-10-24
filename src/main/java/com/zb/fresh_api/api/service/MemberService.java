package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.dto.SignUpRequest;
import com.zb.fresh_api.api.dto.TermsAgreementDto;
import com.zb.fresh_api.api.dto.request.*;
import com.zb.fresh_api.api.dto.response.*;
import com.zb.fresh_api.api.factory.OauthProviderFactory;
import com.zb.fresh_api.api.principal.CustomUserDetailsService;
import com.zb.fresh_api.api.provider.TokenProvider;
import com.zb.fresh_api.api.utils.RandomUtil;
import com.zb.fresh_api.api.utils.S3Uploader;
import com.zb.fresh_api.common.constants.AppConstants;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.dto.file.UploadedFile;
import com.zb.fresh_api.domain.dto.member.LoginMember;
import com.zb.fresh_api.domain.dto.member.MemberWithPoint;
import com.zb.fresh_api.domain.dto.member.OauthLoginMember;
import com.zb.fresh_api.domain.dto.member.OauthUser;
import com.zb.fresh_api.domain.dto.token.Token;
import com.zb.fresh_api.domain.entity.address.DeliveryAddress;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.member.MemberTerms;
import com.zb.fresh_api.domain.entity.member.Word;
import com.zb.fresh_api.domain.entity.point.Point;
import com.zb.fresh_api.domain.entity.point.PointHistory;
import com.zb.fresh_api.domain.entity.terms.Terms;
import com.zb.fresh_api.domain.enums.category.CategoryType;
import com.zb.fresh_api.domain.enums.member.MemberRole;
import com.zb.fresh_api.domain.enums.member.MemberStatus;
import com.zb.fresh_api.domain.enums.member.Provider;
import com.zb.fresh_api.domain.enums.point.PointStatus;
import com.zb.fresh_api.domain.enums.point.PointTransactionType;
import com.zb.fresh_api.domain.repository.reader.*;
import com.zb.fresh_api.domain.repository.writer.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberReader memberReader;
    private final MemberWriter memberWriter;
    private final MemberTermsWriter memberTermsWriter;

    private final DeliveryAddressReader deliveryAddressReader;
    private final DeliveryAddressWriter deliveryAddressWriter;

    private final TermsReader termsReader;

    private final WordReader wordReader;

    private final PasswordEncoder passwordEncoder;
    private final PointWriter pointWriter;
    private final PointReader pointReader;
    private final PointHistoryWriter pointHistoryWriter;

    private final TokenProvider tokenProvider;
    private final OauthProviderFactory oauthProviderFactory;
    private final CustomUserDetailsService customUserDetailsService;

    private final S3Uploader s3Uploader;

    private static final int RANDOM_SUFFIX = 3;

    @Transactional
    public LoginResponse login(final LoginRequest request) {
        final MemberWithPoint memberWithPoint = memberReader.getMemberWithPointByEmailAndProvider(request.email(), Provider.EMAIL);
        final Member member = memberWithPoint.member();
        final Point point = memberWithPoint.point();

        validatePassword(request.password(), member.getPassword());

        final LoginMember loginMember = LoginMember.fromEntity(member, point);
        final Token token = tokenProvider.getTokenByEmail(request.email(), Provider.EMAIL);
        return new LoginResponse(token, loginMember);
    }

    @Transactional(readOnly = true)
    public OauthLoginResponse oauthLogin(final OauthLoginRequest request) {
        final Provider provider = request.provider();
        final String accessToken = oauthProviderFactory.getAccessToken(provider, request.redirectUri(), request.code());
        final OauthUser oAuthUser = oauthProviderFactory.getOauthUser(provider, accessToken);

        final String email = oAuthUser.email();
        final MemberWithPoint memberWithPoint = memberReader.findMemberWithPointByEmailAndProvider(email, provider);
        final boolean isSignup = !Objects.isNull(memberWithPoint);
        final Token token = resolveToken(isSignup, email, provider);

        final OauthLoginMember loginMember = isSignup ?
                OauthLoginMember.fromSignupMember(memberWithPoint.member(), memberWithPoint.point()) :
                OauthLoginMember.beforeSignupMember(email, generateRandomNickname(), provider, oAuthUser.id());

        return new OauthLoginResponse(token, loginMember, isSignup);
    }

    @Transactional
    public void updateProfile(final Member member,
                              final UpdateProfileRequest request,
                              final MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            final UploadedFile file = s3Uploader.upload(CategoryType.MEMBER, image);
            member.updateProfileImage(file.url());
        }

        member.updateNickname(request.nickname());
        memberWriter.store(member);
    }

    @Transactional(readOnly = true)
    public void nickNameValidate(String nickname) {
        validateDuplicatedNickname(nickname);
    }

    @Transactional(readOnly = true)
    public void emailValidate(String email, Provider provider) {
        if (email == null || email.isEmpty()) {
            throw new CustomException(ResponseCode.PARAM_EMAIL_NOT_VALID);
        }
        validateDuplicatedEmail(email, provider);
    }

    @Transactional(readOnly = true)
    public LoadProfileResponse loadProfile(final Member member) {
        final MemberWithPoint memberWithPoint = memberReader.getMemberWithPointByEmailAndProvider(member.getEmail(), member.getProvider());
        return new LoadProfileResponse(LoginMember.fromEntity(memberWithPoint.member(), memberWithPoint.point()));
    }

    /**
     * 회원가입 로직
     * 1. 이메일, 비밀번호, 닉네임, 약관동의관련 리스트를 입력 받습니다
     * 2. 닉네임, 이메일이 중복되는지 확인합니다
     * 3. 약관동의관련 리스트에서 필수인 약관들이 포함되었는지 확인합니다
     * 4. 사용자를 저장합니다
     * 5. 멤버 약관을 저장합니다
     * 6. 포인트를 생성합니다.
     * 7. 이벤트로 받은 500원을 추가합니다
     */
    @Transactional
    public SignupResponse signUp(SignUpRequest request) {
        final String email = request.email();
        final String password = request.password();
        final String nickname = request.nickname();
        final Provider provider = request.provider();
        final List<TermsAgreementDto> termsAgreementDtos = request.termsAgreements();

        validateDuplication(email, nickname, provider);
        validateMandatoryTermsIncluded(termsAgreementDtos);

        if(provider == Provider.EMAIL) {
            validatePasswordMatch(password, request.confirmPassword());
            validatePasswordFormat(password);
        }

        final Member member = memberWriter.store(createMember(nickname, email, password, provider, request.providerId()));
        processTermsAgreements(termsAgreementDtos, member);

        final Point point = pointWriter.store(Point.create(member, BigDecimal.valueOf(500), PointStatus.ACTIVE));
        final PointHistory pointHistory = PointHistory.create(point, PointTransactionType.CHARGE, BigDecimal.valueOf(500), BigDecimal.valueOf(0), BigDecimal.valueOf(500), "회원가입 기념 500원 충전");
        pointHistoryWriter.store(pointHistory);

        return new SignupResponse(member.getId(), resolveToken(email, provider));
    }

    private void validatePasswordMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new CustomException(ResponseCode.INVALID_PASSWORD_MATCH);
        }
    }

    private void validatePasswordFormat(String password) {
        if (password.length() < AppConstants.PASSWORD_MIN_LENGTH || password.length() > AppConstants.PASSWORD_MAX_LENGTH) {
            throw new CustomException(ResponseCode.INVALID_PASSWORD_FORMAT);
        }

        if (!password.matches(AppConstants.PASSWORD_PATTERN)) {
            throw new CustomException(ResponseCode.INVALID_PASSWORD_FORMAT);
        }
    }

    private Member createMember(String nickname, String email, String password, Provider provider, String providerId) {
        return (provider == Provider.EMAIL)
                ? Member.createForEmail(nickname, email, passwordEncoder.encode(password), provider, providerId, MemberRole.ROLE_USER, MemberStatus.ACTIVE)
                : Member.createForOauth(nickname, email, password, provider, providerId, MemberRole.ROLE_USER, MemberStatus.ACTIVE
        );
    }

    @Transactional
    public AddDeliveryAddressResponse addDeliveryAddress(final Member member, final AddDeliveryAddressRequest request) {
        final List<DeliveryAddress> deliveryAddresses = deliveryAddressReader.getAllActiveDeliveryAddressByMemberId(member.getId());
        final int addressCount = deliveryAddresses.size();

        validateAddressCount(addressCount);

        if (request.isDefault()) {
            deliveryAddresses.forEach(DeliveryAddress::cancelDefault);
        }

        final DeliveryAddress newDeliveryAddress = DeliveryAddress.create(member, request);
        deliveryAddressWriter.store(newDeliveryAddress);

        return new AddDeliveryAddressResponse(addressCount + 1L);
    }

    @Transactional
    public void modifyDeliveryAddress(final Member member, final Long deliveryAddressId, final ModifyDeliveryAddressRequest request) {
        final List<DeliveryAddress> deliveryAddresses = deliveryAddressReader.getAllActiveDeliveryAddressByMemberId(member.getId());
        final DeliveryAddress deliveryAddress = deliveryAddresses.stream()
                .filter(address -> address.getId().equals(deliveryAddressId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_DELIVERY_ADDRESS));

        if (request.isDefault()) {
            deliveryAddresses.stream()
                    .filter(address -> !address.getId().equals(deliveryAddressId))
                    .forEach(DeliveryAddress::cancelDefault);
        }

        deliveryAddress.modify(request);
    }

    @Transactional
    public void deleteDeliveryAddress(final Member member, final Long deliveryAddressId) {
        final DeliveryAddress deliveryAddress = deliveryAddressReader.getActiveDeliveryAddressByIdAndMemberId(deliveryAddressId, member.getId());
        deliveryAddress.delete();
    }

    @Transactional
    public void deleteAllDeliveryAddress(final Member member) {
        final List<DeliveryAddress> deliveryAddresses = deliveryAddressReader.getAllActiveDeliveryAddressByMemberId(member.getId());
        deliveryAddresses.forEach(DeliveryAddress::delete);
    }

    private String generateRandomNickname() {
        final List<Word> adjectiveWords = wordReader.getAllAdjectiveWord();
        final List<Word> nounWords = wordReader.getAllNounWord();
        String nickname;
        boolean exists;

        do {
            final String adjectiveWord = getRandomWord(adjectiveWords);
            final String randomWord = getRandomWord(nounWords);
            final String suffix = RandomUtil.generateRandomSuffix(RANDOM_SUFFIX);

            nickname = adjectiveWord + randomWord + suffix;
            exists = memberReader.existActiveNickname(nickname);

        } while (exists);

        return nickname;
    }

    public static String getRandomWord(List<Word> words) {
        Word word = RandomUtil.getRandomElement(words);
        return word.getWord();
    }


    /**
     * 필수인 약관이 request의 약관리스트에 포함되었는지 확인하는 로직
     */
    private void validateMandatoryTermsIncluded(List<TermsAgreementDto> termsAgreementDtos) {
        Set<Long> agreedTermsIds = termsAgreementDtos.stream()
            .map(TermsAgreementDto::termsId)
            .collect(Collectors.toSet());

        List<Terms> mandatoryTerms = termsReader.findAllByIsRequired(true);

        for (Terms terms : mandatoryTerms) {
            if (!agreedTermsIds.contains(terms.getId())) {
                throw new CustomException(ResponseCode.TERMS_MANDATORY_NOT_FOUND);
            }
        }
    }

    /**
     * 멤버약관을 저장하는 로직 
     * 1. request 약관리스트를 반복시켜 각 termsId에 해당하는 약관이 있는지 확인합니다
     * 2. 약관이 필수인데 동의를 받지 않을 경우 에러를 반환합니다.
     * 3. 이후 멤버약관을 저장합니다
     */
    private void processTermsAgreements(List<TermsAgreementDto> termsAgreementDtos, Member member) {
        termsAgreementDtos.forEach(termsAgreementDto -> {
            Terms terms = findTermsById(termsAgreementDto.termsId());

            validateMandatoryTerms(terms, termsAgreementDto);

            MemberTerms memberTerms = MemberTerms.create(member, terms,
                termsAgreementDto.isAgreed(), LocalDateTime.now());
            memberTermsWriter.store(memberTerms);
        });
    }

    private Terms findTermsById(Long termsId) {
        return termsReader.findById(termsId).orElseThrow(
            () -> new CustomException(ResponseCode.TERMS_NOT_FOUND)
        );
    }

    private void validateMandatoryTerms(Terms terms, TermsAgreementDto termsAgreementDto) {
        if (terms.isRequired() && !termsAgreementDto.isAgreed()) {
            throw new CustomException(ResponseCode.TERMS_MANDATORY_NOT_AGREED);
        }
    }

    private void  validatePassword(final String password, final String encPassword) {
        if (!passwordEncoder.matches(password, encPassword)) {
            throw new CustomException(ResponseCode.INVALID_PASSWORD);
        }
    }

    protected Token resolveToken(final boolean isSignup, final String email, final Provider provider) {
        return !isSignup ? Token.emptyToken() : tokenProvider.getTokenByEmail(email, provider);
    }

    protected Token resolveToken(final String email, final Provider provider) {
        return provider == Provider.EMAIL ? Token.emptyToken() : tokenProvider.getTokenByEmail(email, provider);
    }

    private void validateAddressCount(final int addressCount) {
        if (addressCount >= 3) {
            throw new CustomException(ResponseCode.EXCEEDED_DELIVERY_ADDRESS_COUNT);
        }
    }

    @Transactional
    public ChargePointResponse chargePoint(ChargePointRequest request, Long memberId) {
        // 멤버 유효한지 확인후
        Member member = memberReader.getById(memberId);
        Point point = pointReader.getByMemberId(memberId);

        // 포인트 히스토리 테이블 생성
        PointHistory pointHistory = pointHistoryWriter.store(
            PointHistory.create(point, PointTransactionType.CHARGE,
                request.point(), point.getBalance(), point.getBalance().add(request.point()),
                "포인트 충전"));

        // 포인트 테이블 업데이트
        point.charge(request.point());

        return new ChargePointResponse(request.point(), point.getBalance());
    }
    public GetAllAddressResponse getAllAddress(Long memberId) {
        Member member = memberReader.getById(memberId);
        List<DeliveryAddress> deliveryAddressList = deliveryAddressReader.getAllActiveDeliveryAddressByMemberId(
            memberId);

        return GetAllAddressResponse.fromEntities(deliveryAddressList);
    }

    private void validateDuplication(final String email, final String nickname, final Provider provider) {
        validateDuplicatedEmail(email, provider);
        validateDuplicatedNickname(nickname);
    }

    private void validateDuplicatedEmail(final String email, final Provider provider) {
        boolean existsByEmailAndProvider = memberReader.existsByEmailAndProvider(email, provider);
        if (existsByEmailAndProvider) {
            throw new CustomException(ResponseCode.EMAIL_ALREADY_IN_USE);
        }
    }

    private void validateDuplicatedNickname(final String nickname) {
        if (memberReader.existActiveNickname(nickname)) {
            throw new CustomException(ResponseCode.NICKNAME_ALREADY_IN_USE);
        }
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberReader.getById(memberId);
        member.delete();
    }

}
