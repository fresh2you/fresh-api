package com.zb.fresh_api.domain.entity.member;

import com.zb.fresh_api.domain.entity.base.BaseTimeEntity;
import com.zb.fresh_api.domain.enums.member.MemberRole;
import com.zb.fresh_api.domain.enums.member.MemberStatus;
import com.zb.fresh_api.domain.enums.member.Provider;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(name = "name", columnDefinition = "varchar(20) comment '회원 이름'")
    private String name;

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

}
