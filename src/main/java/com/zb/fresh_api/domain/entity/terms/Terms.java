package com.zb.fresh_api.domain.entity.terms;


import com.zb.fresh_api.domain.converter.TermsTypeConverter;
import com.zb.fresh_api.domain.entity.base.BaseTimeEntity;
import com.zb.fresh_api.domain.enums.terms.TermsType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "terms"
)
public class Terms extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '고유 번호'")
    private Long id;

    @Column(name = "title", nullable = false, columnDefinition = "varchar(20) comment '약관명'")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "mediumtext comment '약관 내용'")
    private String content;

    @Convert(converter = TermsTypeConverter.class)
    @Column(name = "type", nullable = false, updatable = false, columnDefinition = "varchar(8) comment '약관 타입'")
    private TermsType type;

    @Column(name = "is_used", columnDefinition = "tinyint comment '사용 여부'")
    private boolean isUsed;

    @Column(name = "is_required", columnDefinition = "tinyint comment '필수 여부'")
    private boolean isRequired;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

    public void use() {
        this.isUsed = true;
    }

    public void stopUse() {
        this.isUsed = false;
    }
}
