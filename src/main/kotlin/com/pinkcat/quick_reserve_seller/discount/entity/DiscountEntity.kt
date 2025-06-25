package com.pinkcat.quick_reserve_seller.discount.entity

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import lombok.Data

@Entity
@Data
@Table(name = "discount")
class DiscountEntity(
    @OneToOne(fetch = FetchType.LAZY)
    val product: ProductEntity,
    val discountPrice: Int,
    val startAt: Long?,
    val endAt: Long?
) : BaseEntity()