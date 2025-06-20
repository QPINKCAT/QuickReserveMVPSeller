package com.pinkcat.quick_reserve_seller.category.Entity

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import jakarta.persistence.*
import lombok.Data

@Entity
@Data
@Table(name = "category")
class CategoryEntity(
    val categoryName: String,
    val categoryOrder: Int,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teop_category_pk")
    val topCategory: CategoryEntity?,
) : BaseEntity()