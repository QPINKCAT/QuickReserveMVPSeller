package com.pinkcat.quick_reserve_seller.category.Entity

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import lombok.Data

@Entity
@Data
class CategoryEntity(
    val categoryName: String,
    val categoryOrder: Int,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teop_category_pk")
    val topCategory: CategoryEntity?,
) : BaseEntity()