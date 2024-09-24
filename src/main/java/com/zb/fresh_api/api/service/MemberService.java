package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.dto.TermsAgreementDto;
import com.zb.fresh_api.api.dto.request.*;
import com.zb.fresh_api.api.dto.response.AddDeliveryAddressResponse;
import com.zb.fresh_api.api.dto.response.ChargePointResponse;
import com.zb.fresh_api.api.dto.response.LoginResponse;
import com.zb.fresh_api.api.dto.response.OauthLoginResponse;
import com.zb.fresh_api.api.factory.OauthProviderFactory;
import com.zb.fresh_api.api.principal.CustomUserDetails;
import com.zb.fresh_api.api.principal.CustomUserDetailsService;
import com.zb.fresh_api.api.provider.TokenProvider;
import com.zb.fresh_api.api.utils.S3Uploader;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.dto.file.UploadedFile;
import com.zb.fresh_api.domain.dto.member.LoginMember;
import com.zb.fresh_api.domain.dto.member.OauthLoginMember;
import com.zb.fresh_api.domain.dto.member.OauthUser;
import com.zb.fresh_api.domain.dto.token.Token;
import com.zb.fresh_api.domain.entity.address.DeliveryAddress;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.member.MemberTerms;
import com.zb.fresh_api.domain.entity.point.Point;
import com.zb.fresh_api.domain.entity.point.PointHistory;
import com.zb.fresh_api.domain.entity.terms.Terms;
import com.zb.fresh_api.domain.enums.category.CategoryType;
import com.zb.fresh_api.domain.enums.member.MemberRole;
import com.zb.fresh_api.domain.enums.member.MemberStatus;
import com.zb.fresh_api.domain.enums.member.Provider;
import com.zb.fresh_api.domain.enums.point.PointStatus;
import com.zb.fresh_api.domain.enums.point.PointTransactionType;
import com.zb.fresh_api.domain.repository.reader.DeliveryAddressReader;
import com.zb.fresh_api.domain.repository.reader.MemberReader;
import com.zb.fresh_api.domain.repository.reader.PointReader;
import com.zb.fresh_api.domain.repository.reader.TermsReader;
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

    private final PasswordEncoder passwordEncoder;
    private final PointWriter pointWriter;
    private final PointReader pointReader;
    private final PointHistoryWriter pointHistoryWriter;

    private final TokenProvider tokenProvider;
    private final OauthProviderFactory oauthProviderFactory;
    private final CustomUserDetailsService customUserDetailsService;

    private final S3Uploader s3Uploader;

    @Transactional
    public LoginResponse login(final LoginRequest request) {
        final CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(request.email());
        final Member member = userDetails.member();

        validatePassword(request.password(), member.getPassword());

        final LoginMember loginMember = LoginMember.fromEntity(member);
        final Token token = tokenProvider.getTokenByEmail(request.email());
        return new LoginResponse(token, loginMember);
    }

    @Transactional(readOnly = true)
    public OauthLoginResponse oauthLogin(final OauthLoginRequest request) {
        final Provider provider = request.provider();
        final String accessToken = oauthProviderFactory.getAccessToken(provider, request.redirectUri(), request.code());
        final OauthUser oAuthUser = oauthProviderFactory.getOauthUser(provider, accessToken);

        final String email = oAuthUser.email();
        final boolean isSignup = memberReader.existsByEmailAndProvider(email, provider);
        final Token token = resolveToken(isSignup, email);
        return new OauthLoginResponse(token, new OauthLoginMember(email, provider, oAuthUser.id()), isSignup);
    }

    @Transactional
    public void updateProfile(final Member member,
                              final UpdateProfileRequest request,
                              final MultipartFile image) {
        final UploadedFile file = s3Uploader.upload(CategoryType.MEMBER, image);
        member.updateProfile(request.nickname(), file.url());
        memberWriter.store(member);
    }

    @Transactional(readOnly = true)
    public void nickNameValidate(String nickname) {
        boolean existsByNicknameIgnoreCase = memberReader.existActiveNickname(
            nickname);
        if (existsByNicknameIgnoreCase) {
            throw new CustomException(ResponseCode.NICKNAME_ALREADY_IN_USE);
        }
    }

    @Transactional(readOnly = true)
    public void emailValidate(String email, Provider provider) {
        if (email == null || email.isEmpty()) {
            throw new CustomException(ResponseCode.PARAM_EMAIL_NOT_VALID);
        }
        boolean existsByEmailAndProvider = memberReader.existsByEmailAndProvider(email, provider);
        if (existsByEmailAndProvider) {
            throw new CustomException(ResponseCode.EMAIL_ALREADY_IN_USE);
        }
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
    public void signUp(String email, String password, String nickName,
        List<TermsAgreementDto> termsAgreementDtos, Provider provider, String providerId) {
        nickNameValidate(nickName);
        emailValidate(email, provider);
        validateMandatoryTermsIncluded(termsAgreementDtos);
        Member member = memberWriter.store(
            Member.create(nickName, email, passwordEncoder.encode(password), provider, providerId,
                MemberRole.ROLE_USER, MemberStatus.ACTIVE));

        processTermsAgreements(termsAgreementDtos, member);
        Point point = pointWriter.store(
            Point.create(member, BigDecimal.valueOf(500), PointStatus.ACTIVE)
        );
        pointHistoryWriter.store(
            PointHistory.create(point, PointTransactionType.CHARGE, BigDecimal.valueOf(500),
                BigDecimal.valueOf(0), BigDecimal.valueOf(500),
                "회원가입 기념 500원 충전"));
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

    private void validatePassword(final String password, final String encPassword) {
        if (!passwordEncoder.matches(password, encPassword)) {
            throw new CustomException(ResponseCode.COMMON_INVALID_PARAM);
        }
    }

    protected Token resolveToken(final boolean isSignup, final String email) {
        return !isSignup ? Token.emptyToken() : tokenProvider.getTokenByEmail(email);
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
}
