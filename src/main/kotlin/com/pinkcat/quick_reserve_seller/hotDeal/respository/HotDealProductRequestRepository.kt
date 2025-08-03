package com.pinkcat.quick_reserve_seller.hotDeal.respository

import com.pinkcat.quick_reserve_seller.common.repository.ActiveRepository
import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealEntity
import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealProductRequestEntity
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity
import java.util.Optional

interface HotDealProductRequestRepository : ActiveRepository<HotDealProductRequestEntity, Long> {
    fun existsByProductAndHotDeal(product: ProductEntity, hotDeal: HotDealEntity): Boolean;
    fun findByProductAndHotDeal(product: ProductEntity, hotDeal: HotDealEntity): Optional<HotDealProductRequestEntity>
}