package com.pinkcat.quick_reserve_seller.discount.dto

import com.pinkcat.quick_reserve_seller.discount.entity.DiscountEntity

open class DiscountDto(
    val price: Int,
    val startAt: Long?,
    val endAt: Long?,
) {
    constructor(discount: DiscountEntity) : this(
        price = discount.discountPrice,
        startAt = discount.startAt,
        endAt = discount.endAt,
    )
}