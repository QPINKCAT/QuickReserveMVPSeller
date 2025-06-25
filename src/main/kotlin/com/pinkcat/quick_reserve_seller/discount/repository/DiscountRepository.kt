package com.pinkcat.quick_reserve_seller.discount.repository

import com.pinkcat.quick_reserve_seller.common.repository.ActiveRepository
import com.pinkcat.quick_reserve_seller.discount.entity.DiscountEntity

interface DiscountRepository : ActiveRepository<DiscountEntity, Long> {
}