package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.dto.LoginMember;
import com.zb.fresh_api.api.dto.TermsAgreementDto;
import com.zb.fresh_api.api.dto.request.LoginRequest;
import com.zb.fresh_api.api.dto.request.OauthLoginRequest;
import com.zb.fresh_api.api.dto.request.UpdateProfileRequest;
import com.zb.fresh_api.api.dto.response.LoginResponse;
import com.zb.fresh_api.api.dto.response.OauthLoginMember;
import com.zb.fresh_api.api.dto.response.OauthLoginResponse;
import com.zb.fresh_api.api.factory.OauthProviderFactory;
import com.zb.fresh_api.api.principal.CustomUserDetails;
import com.zb.fresh_api.api.principal.CustomUserDetailsService;
import com.zb.fresh_api.api.provider.TokenProvider;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.dto.member.OauthUser;
import com.zb.fresh_api.domain.dto.token.Token;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.member.MemberTerms;
import com.zb.fresh_api.domain.entity.terms.Terms;
import com.zb.fresh_api.domain.enums.member.MemberRole;
import com.zb.fresh_api.domain.enums.member.MemberStatus;
import com.zb.fresh_api.domain.enums.member.Provider;
import com.zb.fresh_api.domain.repository.reader.MemberReader;
import com.zb.fresh_api.domain.repository.reader.TermsReader;
import com.zb.fresh_api.domain.repository.writer.MemberTermsWriter;
import com.zb.fresh_api.domain.repository.writer.MemberWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberReader memberReader;
    private final MemberWriter memberWriter;
    private final MemberTermsWriter memberTermsWriter;

    private final TermsReader termsReader;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;
    private final OauthProviderFactory oauthProviderFactory;
    private final CustomUserDetailsService customUserDetailsService;

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
        final OauthUser oAuthUser = oauthProviderFactory.getOAuthUser(provider, accessToken);

        final String email = oAuthUser.email();
        final boolean isSignup = memberReader.existsByEmailAndProvider(email, provider);
        final Token token = resolveToken(isSignup, email);
        return new OauthLoginResponse(token, new OauthLoginMember(email, provider, oAuthUser.id()), isSignup);
    }

    @Transactional
    public void updateProfile(final Member member,
                              final UpdateProfileRequest request,
                              final MultipartFile image) {
        // TODO 1. 이미지 변환 (S3)
        final String profileImage = null;

        // 2. 프로필 변경
        member.updateProfile(request.nickname(), profileImage);
    }

    @Transactional(readOnly = true)
    public void nickNameValidate(String nickname) {
        boolean existsByNicknameIgnoreCase = memberReader.existActiveNickname(nickname);
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
     */
    @Transactional
    public void signUp(String email, String password, String nickName,
        List<TermsAgreementDto> termsAgreementDtos, Provider provider, String providerId) {
        nickNameValidate(nickName);
        emailValidate(email, provider);
        validateMandatoryTermsIncluded(termsAgreementDtos);
        Member member = memberWriter.store(
            Member.builder()
                .nickname(nickName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .provider(provider)
                .providerId(providerId)
                .role(MemberRole.ROLE_USER)
                .status(MemberStatus.ACTIVE)
                .build()
        );
        processTermsAgreements(termsAgreementDtos, member);
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

}
