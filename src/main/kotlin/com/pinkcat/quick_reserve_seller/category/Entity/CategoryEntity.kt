package com.pinkcat.quick_reserve_seller.category.Entity

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import jakarta.persistence.*
import lombok.Data

@Entity
@Data
@Table(name = "category")
@AttributeOverride(name = "pk", column = Column(name = "category_pk"))
class CategoryEntity(
    @Column(name = "category_name", length = 100)
    val categoryName: String,
    @Column(name = "category_order")
    val categoryOrder: Int,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "top_category_pk")
    val topCategory: CategoryEntity?,
) : BaseEntity()