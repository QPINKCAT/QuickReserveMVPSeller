package com.pinkcat.quick_reserve_seller.category.dto

import com.pinkcat.quick_reserve_seller.category.Entity.CategoryEntity

data class CategoryRes(
    val pk: Long,
    val categoryName: String,
    val categoryOrder: Int,
    val topCategoryPk: Long?
) {
    constructor(category: CategoryEntity) : this(
        pk = category.pk,
        categoryName = category.categoryName,
        categoryOrder = category.categoryOrder,
        topCategoryPk = category.topCategory?.pk
    )
}