package com.pinkcat.quick_reserve_seller.hotDeal.respository

import com.pinkcat.quick_reserve_seller.common.enums.HotDealPublicStatusEnum
import com.pinkcat.quick_reserve_seller.common.repository.ActiveRepository
import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface HotDealRepository : ActiveRepository<HotDealEntity, Long> {
    fun findAllByPublicStatusNot(
            publicStatus: HotDealPublicStatusEnum,
            pageable: Pageable
    ): Page<HotDealEntity>
}