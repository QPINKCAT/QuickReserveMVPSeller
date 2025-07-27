package com.pinkcat.quick_reserve_seller.hotDeal.respository;

import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealProductRequestLogEntity;
import com.pinkcat.quick_reserve_seller.hotDeal.dto.HotDealProductRequestSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

interface HotDealProductRequestLogRepositoryCustom {
    fun search(
            condition: HotDealProductRequestSearchCondition,
            pageable: Pageable,
            userPk: Long
    ): Page<HotDealProductRequestLogEntity>
}