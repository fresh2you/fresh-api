package com.zb.fresh_api.domain.entity.member;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "customer"
)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '고유 번호'")
    private Long id;

    @Column(name = "email", nullable = false, columnDefinition = "varchar(255) comment '이메일'")
    private String email;

    @Column(name = "nickname", nullable = false, columnDefinition = "varchar(20) comment '닉네임'")
    private String nickname;

    @Column(name = "phone", columnDefinition = "varchar(20) comment '전화번호'")
    private String phone;

    @Builder.Default
    @Column(name = "created_at", columnDefinition = "datetime comment '생성일'")
    private LocalDateTime createdAt = LocalDateTime.now();

    public static Customer of(Member member){
        return Customer.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .phone(member.getPhone())
                .build();
    }

}
