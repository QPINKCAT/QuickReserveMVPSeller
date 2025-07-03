package com.pinkcat.quick_reserve_seller.hotDeal.respository

import com.pinkcat.quick_reserve_seller.common.repository.ActiveRepository
import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealProductEntity

interface HotDealProductRepository : ActiveRepository<HotDealProductEntity, Long> {
}