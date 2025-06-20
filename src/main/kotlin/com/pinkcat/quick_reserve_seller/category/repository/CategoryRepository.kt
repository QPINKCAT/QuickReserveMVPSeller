package com.pinkcat.quick_reserve_seller.category.repository

import com.pinkcat.quick_reserve_seller.category.Entity.CategoryEntity
import com.pinkcat.quick_reserve_seller.common.repository.ActiveRepository

interface CategoryRepository : ActiveRepository<CategoryEntity, Long> {
}