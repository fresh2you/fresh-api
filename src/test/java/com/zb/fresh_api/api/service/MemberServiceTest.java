package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.dto.TermsAgreementDto;
import com.zb.fresh_api.api.dto.request.AddDeliveryAddressRequest;
import com.zb.fresh_api.api.dto.request.ModifyDeliveryAddressRequest;
import com.zb.fresh_api.api.dto.request.OauthLoginRequest;
import com.zb.fresh_api.api.dto.response.AddDeliveryAddressResponse;
import com.zb.fresh_api.api.dto.response.OauthLoginResponse;
import com.zb.fresh_api.api.factory.OauthProviderFactory;
import com.zb.fresh_api.common.base.ServiceTest;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.dto.member.KakaoAccount;
import com.zb.fresh_api.domain.dto.member.KakaoUser;
import com.zb.fresh_api.domain.dto.token.Token;
import com.zb.fresh_api.domain.entity.address.DeliveryAddress;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.point.Point;
import com.zb.fresh_api.domain.entity.point.PointHistory;
import com.zb.fresh_api.domain.entity.terms.Terms;
import com.zb.fresh_api.domain.enums.member.MemberRole;
import com.zb.fresh_api.domain.enums.member.MemberStatus;
import com.zb.fresh_api.domain.enums.member.Provider;
import com.zb.fresh_api.domain.repository.reader.DeliveryAddressReader;
import com.zb.fresh_api.domain.repository.reader.MemberReader;
import com.zb.fresh_api.domain.repository.reader.TermsReader;
import com.zb.fresh_api.domain.repository.writer.*;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("회원 비즈니스 테스트")
class MemberServiceTest extends ServiceTest {

    @Mock
    private MemberReader memberReader;

    @Mock
    private MemberWriter memberWriter;

    @Mock
    private DeliveryAddressReader deliveryAddressReader;

    @Mock
    private DeliveryAddressWriter deliveryAddressWriter;

    @Mock
    private MemberTermsWriter memberTermsWriter;

    @Mock
    private TermsReader termsReader;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private OauthProviderFactory oauthProviderFactory;

    @Mock
    private PointWriter pointWriter;

    @Mock
    private PointHistoryWriter pointHistoryWriter;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("배송지 전체 삭제 - 성공")
    void 배송지_전체_삭제_성공() {
        // given
        Member member = getConstructorMonkey().giveMeBuilder(Member.class)
                .set("id", Arbitraries.longs().greaterOrEqual(1))
                .set("email", Arbitraries.strings().alpha().ofMinLength(4).ofMaxLength(8).map(param -> param + "@gmail.com"))
                .set("phone", Arbitraries.strings().numeric().ofLength(11))
                .set("provider", Arbitraries.of(Provider.class).sample())
                .set("role", MemberRole.ROLE_USER)
                .sample();

        Long deliveryAddressId = Arbitraries.longs().between(1, 10).sample();
        DeliveryAddress deliveryAddress = getBuilderMonkey().giveMeBuilder(DeliveryAddress.class)
                .set("id", deliveryAddressId)
                .set("member", member)
                .set("isDefault", Arbitraries.of(true, false))
                .sample();

        List<DeliveryAddress> deliveryAddresses = new ArrayList<>();
        deliveryAddresses.add(deliveryAddress);
        int repeat = Arbitraries.integers().between(0, 2).sample();
        for (int i = 0; i < repeat; i++) {
            DeliveryAddress newDeliveryAddress = getBuilderMonkey().giveMeBuilder(DeliveryAddress.class)
                    .set("id", Arbitraries.longs().greaterOrEqual(10).sample())
                    .set("member", member)
                    .sample();
            deliveryAddresses.add(newDeliveryAddress);
        }

        // when
        doReturn(deliveryAddresses).when(deliveryAddressReader).getAllActiveDeliveryAddressByMemberId(member.getId());

        // then
        memberService.deleteAllDeliveryAddress(member);

        deliveryAddresses.forEach(address -> assertNotNull(address.getDeletedAt()));
        verify(deliveryAddressReader, times(1)).getAllActiveDeliveryAddressByMemberId(member.getId());

    }

    @Test
    @DisplayName("배송지 삭제 - 성공")
    void 배송지_삭제_성공() {
        // given
        Member member = getConstructorMonkey().giveMeBuilder(Member.class)
                .set("id", Arbitraries.longs().greaterOrEqual(1))
                .set("email", Arbitraries.strings().alpha().ofMinLength(4).ofMaxLength(8).map(param -> param + "@gmail.com"))
                .set("phone", Arbitraries.strings().numeric().ofLength(11))
                .set("provider", Arbitraries.of(Provider.class).sample())
                .set("role", MemberRole.ROLE_USER)
                .sample();

        Long deliveryAddressId = Arbitraries.longs().between(1, 10).sample();

        DeliveryAddress deliveryAddress = getBuilderMonkey().giveMeBuilder(DeliveryAddress.class)
                .set("id", deliveryAddressId)
                .set("member", member)
                .set("isDefault", false)
                .sample();

        // when
        doReturn(deliveryAddress).when(deliveryAddressReader).getActiveDeliveryAddressByIdAndMemberId(deliveryAddressId, member.getId());

        // then
        memberService.deleteDeliveryAddress(member, deliveryAddressId);

        assertNotNull(deliveryAddress.getDeletedAt());
        verify(deliveryAddressReader, times(1)).getActiveDeliveryAddressByIdAndMemberId(deliveryAddressId, member.getId());
    }

    @Test
    @DisplayName("다른 유저의 배송지 삭제(존재하지 않는 배송지) - 실패")
    void 다른_유저의_배송지_삭제_실패() {
        // given
        Member member = getConstructorMonkey().giveMeBuilder(Member.class)
                .set("id", Arbitraries.longs().greaterOrEqual(1))
                .set("email", Arbitraries.strings().alpha().ofMinLength(4).ofMaxLength(8).map(param -> param + "@gmail.com"))
                .set("phone", Arbitraries.strings().numeric().ofLength(11))
                .set("provider", Arbitraries.of(Provider.class).sample())
                .set("role", MemberRole.ROLE_USER)
                .sample();

        Long deliveryAddressId = Arbitraries.longs().between(2, 10).sample();

        // when
        doThrow(new CustomException(ResponseCode.NOT_FOUND_DELIVERY_ADDRESS))
                .when(deliveryAddressReader).getActiveDeliveryAddressByIdAndMemberId(deliveryAddressId, member.getId());

        // then
        assertThrows(CustomException.class, () -> memberService.deleteDeliveryAddress(member, deliveryAddressId));
        verify(deliveryAddressReader, times(1)).getActiveDeliveryAddressByIdAndMemberId(deliveryAddressId, member.getId());
    }

    @Test
    @DisplayName("배송지 수정 - 성공")
    void 배송지_정보수정_성공() {
        // given
        Member member = getConstructorMonkey().giveMeBuilder(Member.class)
                .set("id", Arbitraries.longs().greaterOrEqual(1))
                .set("email", Arbitraries.strings().alpha().ofMinLength(4).ofMaxLength(8).map(param -> param + "@gmail.com"))
                .set("phone", Arbitraries.strings().numeric().ofLength(11))
                .set("provider", Arbitraries.of(Provider.class).sample())
                .set("role", MemberRole.ROLE_USER)
                .sample();

        ModifyDeliveryAddressRequest request = getConstructorMonkey().giveMeBuilder(ModifyDeliveryAddressRequest.class)
                .set("recipientName", Arbitraries.strings().alpha().ofMinLength(4).ofMaxLength(10))
                .set("phone", Arbitraries.strings().numeric().ofLength(11))
                .set("address", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20))
                .set("detailedAddress", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20))
                .set("postalCode", Arbitraries.strings().numeric().ofLength(5))
                .set("isDefault", Arbitraries.of(true, false))
                .sample();

        Long deliveryAddressId = Arbitraries.longs().between(1, 10).sample();

        DeliveryAddress deliveryAddress = getBuilderMonkey().giveMeBuilder(DeliveryAddress.class)
                .set("id", deliveryAddressId)
                .set("member", member)
                .set("isDefault", false)
                .sample();

        List<DeliveryAddress> deliveryAddresses = new ArrayList<>();
        deliveryAddresses.add(deliveryAddress);
        int repeat = Arbitraries.integers().between(0, 2).sample();
        for (int i = 0; i < repeat; i++) {
            DeliveryAddress newDeliveryAddress = getBuilderMonkey().giveMeBuilder(DeliveryAddress.class)
                    .set("id", Arbitraries.longs().greaterOrEqual(10).sample())
                    .sample();
            deliveryAddresses.add(newDeliveryAddress);
        }

        // when
        doReturn(deliveryAddresses).when(deliveryAddressReader).getAllActiveDeliveryAddressByMemberId(member.getId());

        // then
        memberService.modifyDeliveryAddress(member, deliveryAddressId, request);

        assertEquals(deliveryAddress.getRecipientName(), request.recipientName());
        assertEquals(deliveryAddress.getPhone(), request.phone());
        assertEquals(deliveryAddress.getAddress(), request.address());
        assertEquals(deliveryAddress.getDetailedAddress(), request.detailedAddress());
        assertEquals(deliveryAddress.getPostalCode(), request.postalCode());

        verify(deliveryAddressReader, times(1)).getAllActiveDeliveryAddressByMemberId(member.getId());
    }

    @Test
    @DisplayName("기본 배송지 변경 및 배송지 수정 - 성공")
    void 배송지_정보수정_기본배송지변경_성공() {
        // given
        Member member = getConstructorMonkey().giveMeBuilder(Member.class)
                .set("id", Arbitraries.longs().greaterOrEqual(1))
                .set("email", Arbitraries.strings().alpha().ofMinLength(4).ofMaxLength(8).map(param -> param + "@gmail.com"))
                .set("phone", Arbitraries.strings().numeric().ofLength(11))
                .set("provider", Arbitraries.of(Provider.class).sample())
                .set("role", MemberRole.ROLE_USER)
                .sample();

        ModifyDeliveryAddressRequest request = getConstructorMonkey().giveMeBuilder(ModifyDeliveryAddressRequest.class)
                .set("recipientName", Arbitraries.strings().alpha().ofMinLength(4).ofMaxLength(10))
                .set("phone", Arbitraries.strings().numeric().ofLength(11))
                .set("address", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20))
                .set("detailedAddress", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20))
                .set("postalCode", Arbitraries.strings().numeric().ofLength(5))
                .set("isDefault", Arbitraries.of(true, false))
                .sample();

        Long deliveryAddressId = Arbitraries.longs().between(1, 10).sample();

        DeliveryAddress deliveryAddress = getBuilderMonkey().giveMeBuilder(DeliveryAddress.class)
                .set("id", deliveryAddressId)
                .set("member", member)
                .set("isDefault", true)
                .sample();

        List<DeliveryAddress> deliveryAddresses = new ArrayList<>();
        deliveryAddresses.add(deliveryAddress);
        int repeat = Arbitraries.integers().between(0, 2).sample();
        for (int i = 0; i < repeat; i++) {
            DeliveryAddress newDeliveryAddress = getBuilderMonkey().giveMeBuilder(DeliveryAddress.class)
                    .set("id", Arbitraries.longs().greaterOrEqual(10).sample())
                    .sample();
            deliveryAddresses.add(newDeliveryAddress);
        }

        // when
        doReturn(deliveryAddresses).when(deliveryAddressReader).getAllActiveDeliveryAddressByMemberId(member.getId());

        // then
        memberService.modifyDeliveryAddress(member, deliveryAddressId, request);

        deliveryAddresses.stream()
                .filter(address -> !address.getId().equals(deliveryAddressId))
                .forEach(address -> assertFalse(address.isDefault()));

        assertEquals(deliveryAddress.getRecipientName(), request.recipientName());
        assertEquals(deliveryAddress.getPhone(), request.phone());
        assertEquals(deliveryAddress.getAddress(), request.address());
        assertEquals(deliveryAddress.getDetailedAddress(), request.detailedAddress());
        assertEquals(deliveryAddress.getPostalCode(), request.postalCode());

        verify(deliveryAddressReader, times(1)).getAllActiveDeliveryAddressByMemberId(member.getId());
    }

    @Test
    @DisplayName("배송지 추가 3개 이상 등록 시도 - 실패")
    void 배송지_초과등록시도_실패() {
        // given
        Member member = getConstructorMonkey().giveMeBuilder(Member.class)
                .set("id", Arbitraries.longs().greaterOrEqual(1))
                .set("email", Arbitraries.strings().alpha().ofMinLength(4).ofMaxLength(8).map(param -> param + "@gmail.com"))
                .set("phone", Arbitraries.strings().numeric().ofLength(11))
                .set("provider", Arbitraries.of(Provider.class).sample())
                .set("role", MemberRole.ROLE_USER)
                .sample();

        AddDeliveryAddressRequest request = getConstructorMonkey().giveMeBuilder(AddDeliveryAddressRequest.class)
                .set("recipientName", Arbitraries.strings().alpha().ofMinLength(4).ofMaxLength(10))
                .set("phone", Arbitraries.strings().numeric().ofLength(11))
                .set("address", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20))
                .set("detailedAddress", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20))
                .set("postalCode", Arbitraries.strings().numeric().ofLength(5))
                .set("isDefault", Arbitraries.of(true, false))
                .sample();

        List<DeliveryAddress> deliveryAddresses = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            DeliveryAddress deliveryAddress = getConstructorMonkey().giveMeBuilder(DeliveryAddress.class).sample();
            deliveryAddresses.add(deliveryAddress);
        }

        // when, then
        doReturn(deliveryAddresses).when(deliveryAddressReader).getAllActiveDeliveryAddressByMemberId(member.getId());
        assertThrows(CustomException.class, () -> memberService.addDeliveryAddress(member, request));

        verify(deliveryAddressReader, times(1)).getAllActiveDeliveryAddressByMemberId(member.getId());
    }

    @Test
    @DisplayName("배송지 추가 - 성공")
    void 배송지추가_성공() {
        // given
        Member member = getConstructorMonkey().giveMeBuilder(Member.class)
                .set("id", Arbitraries.longs().greaterOrEqual(1))
                .set("email", Arbitraries.strings().alpha().ofMinLength(4).ofMaxLength(8).map(param -> param + "@gmail.com"))
                .set("phone", Arbitraries.strings().numeric().ofLength(11))
                .set("provider", Arbitraries.of(Provider.class).sample())
                .set("role", MemberRole.ROLE_USER)
                .sample();

        AddDeliveryAddressRequest request = getConstructorMonkey().giveMeBuilder(AddDeliveryAddressRequest.class)
                .set("recipientName", Arbitraries.strings().alpha().ofMinLength(4).ofMaxLength(10))
                .set("phone", Arbitraries.strings().numeric().ofLength(11))
                .set("address", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20))
                .set("detailedAddress", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20))
                .set("postalCode", Arbitraries.strings().numeric().ofLength(5))
                .set("isDefault", Arbitraries.of(true, false))
                .sample();

        List<DeliveryAddress> deliveryAddresses = new ArrayList<>();
        int repeat = Arbitraries.integers().between(0, 2).sample();
        for (int i = 0; i < repeat; i++) {
            DeliveryAddress deliveryAddress = getConstructorMonkey().giveMeBuilder(DeliveryAddress.class).sample();
            deliveryAddresses.add(deliveryAddress);
        }

        // when
        doReturn(deliveryAddresses).when(deliveryAddressReader).getAllActiveDeliveryAddressByMemberId(member.getId());

        // then
        AddDeliveryAddressResponse response = memberService.addDeliveryAddress(member, request);

        assertNotNull(response);
        assertEquals(deliveryAddresses.size() + 1, response.addressCount());

        verify(deliveryAddressReader, times(1)).getAllActiveDeliveryAddressByMemberId(member.getId());
        verify(deliveryAddressWriter, times(1)).store(any(DeliveryAddress.class));
    }

    @Test
    @DisplayName("회원 가입을 진행하지 않은 사용자가 소셜 로그인을 진행한다.")
    void oauthLogin_sign_up_false() {
        // given
        Provider provider = Arbitraries.of(Provider.class).sample();
        String accessToken = Arbitraries.strings().alpha().ofMinLength(40).ofMaxLength(60).sample();

        OauthLoginRequest request = getConstructorMonkey().giveMeBuilder(OauthLoginRequest.class)
                .set("code", Arbitraries.strings().alpha().ofMinLength(20).ofMaxLength(40))
                .set("redirectUri", Arbitraries.strings().alpha().ofMinLength(20).ofMaxLength(40))
                .set("provider", provider)
                .sample();

        KakaoAccount kakaoAccount = getConstructorMonkey().giveMeBuilder(KakaoAccount.class)
                .set("emailNeedsAgreement", Arbitraries.of(true, false))
                .set("isEmailValid", Arbitraries.of(true, false))
                .set("isEmailVerified", Arbitraries.of(true, false))
                .set("email", Arbitraries.strings().alpha().ofMinLength(4).ofMaxLength(8).map(param -> param + "@gmail.com"))
                .sample();

        KakaoUser oauthUser = getConstructorMonkey().giveMeBuilder(KakaoUser.class)
                .set("id", Arbitraries.strings().numeric().ofMinLength(1).ofMaxLength(4))
                .set("hasSignedUp", Arbitraries.of(true, false))
                .set("connectedAt", Arbitraries.of(LocalDateTime.now()).sample())
                .set("synchedAt", Arbitraries.of(LocalDateTime.now()).sample())
                .set("kakaoAccount", kakaoAccount)
                .sample();

        boolean isSignup = false;
        Token token = Token.emptyToken();

        doReturn(accessToken).when(oauthProviderFactory).getAccessToken(request.provider(), request.redirectUri(), request.code());
        doReturn(oauthUser).when(oauthProviderFactory).getOauthUser(request.provider(), accessToken);
        doReturn(isSignup).when(memberReader).existsByEmailAndProvider(oauthUser.email(), request.provider());

        // when
        OauthLoginResponse response = memberService.oauthLogin(request);

        // then
        Assertions.assertNotNull(response);
        assertEquals(response.isSignup(), isSignup);
        assertEquals(response.loginMember().email(), oauthUser.email());
        assertEquals(response.loginMember().provider(), request.provider());
        assertEquals(response.token(), token);

        verify(oauthProviderFactory).getAccessToken(request.provider(), request.redirectUri(), request.code());
        verify(oauthProviderFactory).getOauthUser(request.provider(), accessToken);
        verify(memberReader).existsByEmailAndProvider(oauthUser.email(), request.provider());
    }

    @Test
    @DisplayName("닉네임 중복 검사 실패")
    void nickNameValidate_duplicateExists() {
        // given
        when(memberReader.existActiveNickname(anyString())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.nickNameValidate("gin"))
            .isInstanceOf(CustomException.class)
            .hasMessage(ResponseCode.NICKNAME_ALREADY_IN_USE.getMessage());
    }

    @Test
    @DisplayName("이메일 중복 검사 실패")
    void emailValidate_duplicateExists() {
        // given
        when(memberReader.existsByEmailAndProvider(anyString(),
            any(Provider.class))).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.emailValidate("test@test.com", Provider.EMAIL))
            .isInstanceOf(CustomException.class)
            .hasMessage(ResponseCode.EMAIL_ALREADY_IN_USE.getMessage());
    }

    @Test
    @DisplayName("이메일 회원가입 성공")
    void  signUp_success() {
        // given
        String email = Arbitraries.strings().withCharRange('a', 'z').ofMinLength(5).ofMaxLength(5)
            .map(x -> x + "@example.com").sample();
        String password = Arbitraries.strings().all().sample();
        String nickname = Arbitraries.strings().ofMinLength(2).ofMaxLength(20).sample();
        Provider provider = Provider.EMAIL;
        String providerId = null;
        Long firstTermsId = Arbitraries.longs().between(1L, 999999L).sample();
        Long secondTermsId = Arbitraries.longs().between(1L, 999999L).sample();
        Long thirdTermsId = Arbitraries.longs().between(1L, 999999L).sample();
        boolean isNicknameDuplicated = false;
        List<TermsAgreementDto> termsAgreementDtos = new ArrayList<>(List.of(
            getConstructorMonkey().giveMeBuilder(TermsAgreementDto.class)
                .set("termsId", firstTermsId).set("isAgreed", true)
                .sample(),
            getConstructorMonkey().giveMeBuilder(TermsAgreementDto.class)
                .set("termsId", secondTermsId).set("isAgreed", true)
                .sample(),
            getConstructorMonkey().giveMeBuilder(TermsAgreementDto.class)
                .set("termsId", thirdTermsId).set("isAgreed", true)
                .sample()
            ));
        List<Terms> terms = new ArrayList<>(List.of(
            getConstructorMonkey().giveMeBuilder(Terms.class)
                .set("id", firstTermsId).set("isAgreed", true)
                .set("title", Arbitraries.strings().all().ofMinLength(1))
                .sample(),
            getConstructorMonkey().giveMeBuilder(Terms.class)
                .set("id", secondTermsId).set("isAgreed", true)
                .set("title", Arbitraries.strings().all().ofMinLength(1))
                .sample(),
            getConstructorMonkey().giveMeBuilder(Terms.class)
                .set("id", thirdTermsId).set("isAgreed", true)
                .set("title", Arbitraries.strings().all().ofMinLength(1))
                .sample()
        ));
        Member member = getConstructorMonkey().giveMeBuilder(Member.class).set("nickname", nickname)
            .set("email", email).set("password", passwordEncoder.encode(password))
            .set("provider", provider).set("providerId", providerId)
            .set("role", MemberRole.ROLE_USER).set("status", MemberStatus.ACTIVE).sample();

        doReturn(isNicknameDuplicated).when(memberReader).existActiveNickname(nickname);
        doReturn(false).when(memberReader).existsByEmailAndProvider(email, provider);
        doReturn(terms).when(termsReader).findAllByIsRequired(true);

        doReturn(member).when(memberWriter).store(argThat(m ->
            m.getNickname().equals(member.getNickname()) &&
                m.getEmail().equals(member.getEmail())
        ));
        doReturn(Optional.ofNullable(terms.getFirst())).when(termsReader).findById(firstTermsId);
        doReturn(Optional.ofNullable(terms.get(1))).when(termsReader).findById(secondTermsId);
        doReturn(Optional.ofNullable(terms.get(2))).when(termsReader).findById(thirdTermsId);

        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        // when
        memberService.signUp(email,password,nickname,termsAgreementDtos,provider,providerId);

        // then
        verify(memberWriter).store(memberCaptor.capture());
        Member capturedMember = memberCaptor.getValue();
        assertEquals(member.getEmail(), capturedMember.getEmail());
        verify(memberReader).existActiveNickname(nickname);
        verify(memberReader).existsByEmailAndProvider(email, provider);
        verify(termsReader).findAllByIsRequired(true);
        verify(pointWriter).store(any(Point.class));
        verify(pointHistoryWriter).store(any(PointHistory.class));
    }

    @Test
    @DisplayName("회원가입 실패 (필수 약관 미동의)")
    void signUp_fail_mandatoryTermsNotAgreed() {
        // given
        List<TermsAgreementDto> requestTermsAgreementDtos = List.of(
            new TermsAgreementDto(1L, false), // 필수 약관 미동의
            new TermsAgreementDto(2L, true)
        );
        Terms terms = Terms.builder().id(1L).title("필수 약관1").isRequired(true).build();
        List<Terms> termsList = List.of(
            terms
        );

        when(memberReader.existActiveNickname(anyString())).thenReturn(false);
        when(memberReader.existsByEmailAndProvider(anyString(),
            any(Provider.class))).thenReturn(false);
        when(termsReader.findAllByIsRequired(true)).thenReturn(termsList);
        when(termsReader.findById(anyLong())).thenReturn(Optional.of(terms));
        // then
        assertThatThrownBy(() -> memberService.signUp("test@test.com", "password", "gin",
            requestTermsAgreementDtos, Provider.EMAIL, "1"))
            .isInstanceOf(CustomException.class)
            .hasMessage(ResponseCode.TERMS_MANDATORY_NOT_AGREED.getMessage());
    }
}

