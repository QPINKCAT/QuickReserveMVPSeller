package com.pinkcat.quick_reserve_seller.categoryProduct.repository

import com.pinkcat.quick_reserve_seller.categoryProduct.entity.CategoryProductEntity
import com.pinkcat.quick_reserve_seller.common.repository.ActiveRepository

interface CategoryProductRepository : ActiveRepository<CategoryProductEntity, Long> {
}