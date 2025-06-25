package com.pinkcat.quick_reserve_seller.product.repository

import com.pinkcat.quick_reserve_seller.common.repository.ActiveRepository
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity

interface ProductRepository : ActiveRepository<ProductEntity, Long> {
}