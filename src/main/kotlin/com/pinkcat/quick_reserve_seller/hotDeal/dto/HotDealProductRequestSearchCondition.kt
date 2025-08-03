package com.pinkcat.quick_reserve_seller.hotDeal.dto;

import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealProductRequestStatus;

data class HotDealProductRequestSearchCondition @JvmOverloads constructor(
        val hotDealPk: Long? = null,
        val productPk: Long? = null,
        val status: HotDealProductRequestStatus? = null
)
