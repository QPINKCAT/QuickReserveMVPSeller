package com.pinkcat.quick_reserve_seller.seller.repository

import com.pinkcat.quick_reserve_seller.common.repository.ActiveRepository
import com.pinkcat.quick_reserve_seller.seller.entity.SellerEntity

interface SellerRepository : ActiveRepository<SellerEntity, Long> {
}