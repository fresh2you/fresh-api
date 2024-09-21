package com.zb.fresh_api.domain.entity.category;

import com.zb.fresh_api.domain.entity.base.BaseTimeEntity;
import com.zb.fresh_api.domain.enums.category.CategoryType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "category"
)
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '고유 번호'")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", columnDefinition = "varchar(50) comment '타입'")
    private CategoryType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id",nullable = true, columnDefinition = "BIGINT UNSIGNED comment '카테고리 부모 번호'")
    private Category parent;

    @Column(name="is_used", nullable = false, columnDefinition = "BOOLEAN comment '사용 여부'")
    private boolean isUsed;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(20) comment '카테고리명'")
    private String name;
}
