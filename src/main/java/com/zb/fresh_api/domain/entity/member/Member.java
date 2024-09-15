package com.zb.fresh_api.domain.entity.member;

import com.zb.fresh_api.domain.entity.base.BaseTimeEntity;
import com.zb.fresh_api.domain.enums.member.MemberRole;
import com.zb.fresh_api.domain.enums.member.MemberStatus;
import com.zb.fresh_api.domain.enums.member.Provider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "member"
)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '고유 번호'")
    private Long id;

    @Column(name = "email", nullable = false, columnDefinition = "varchar(255) comment '이메일'")
    private String email;

    @Column(name = "nickname", nullable = false, columnDefinition = "varchar(20) comment '닉네임'")
    private String nickname;

    @Column(name = "password", columnDefinition = "varchar(255) comment '비밀번호'")
    private String password;

    @Column(name = "phone", columnDefinition = "varchar(20) comment '전화번호'")
    private String phone;

    @Column(name = "profile_image", columnDefinition = "varchar(255) comment '프로필 이미지'")
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, columnDefinition = "varchar(20) comment '가입 경로'")
    private Provider provider;

    @Column(name = "provider_id", columnDefinition = "varchar(255) comment '공급 번호'")
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "varchar(20) comment '회원 상태'")
    private MemberStatus status;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "varchar(20) comment '권한'")
    private MemberRole role = MemberRole.ROLE_USER;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '탈퇴 일시'")
    private LocalDateTime deletedAt;

    @Column(name = "is_seller", columnDefinition = "boolean comment '판매자 인증 여부'")
    private boolean isSeller = false;

    @Column(name = "seller_verified_at", columnDefinition = "datetime comment '판매자 인증 일시'")
    private LocalDateTime sellerVerifiedAt;
  
    public static Member create(String nickname, String email, String password,
        Provider provider, String providerId, MemberRole memberRole, MemberStatus memberStatus){
        return Member.builder()
            .nickname(nickname)
            .email(email)
            .password(password)
            .provider(provider)
            .providerId(providerId)
            .role(memberRole)
            .status(memberStatus)
            .build();
    }

    public void updateProfile(final String nickname,
                              final String profileImage) {
        updateNickname(nickname);
        updateProfileImage(profileImage);
    }

    private void updateNickname(final String nickname) {
        if (Objects.isNull(nickname) || this.nickname.equals(nickname)) {
            return;
        }
        this.nickname = nickname;
    }

    private void updateProfileImage(final String profileImage) {
        if (Objects.isNull(profileImage)) {
            return;
        }
        this.profileImage = profileImage;
    }

    public void setMemberSeller(String phone) {
        this.phone = phone;
        this.isSeller = true;
        this.sellerVerifiedAt = LocalDateTime.now();
    }

}
