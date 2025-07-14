package com.pinkcat.quick_reserve_seller.hotDeal.dto

import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealEntity

class HotDealDto {
}

data class HotDealRes(
    val pk: Long,
    val name: String,
    val thumbnail: String,
    val startAt: Long,
    val endAt: Long,
) {
    constructor(hotDeal: HotDealEntity) : this(
        pk = hotDeal.pk,
        name = hotDeal.name,
        thumbnail = hotDeal.thumbnail,
        startAt = hotDeal.startAt,
        endAt = hotDeal.endAt
    )
}