package com.pinkcat.quick_reserve_seller.discount.entity

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import com.pinkcat.quick_reserve_seller.discount.dto.DiscountDto
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
    var discountPrice: Int,
    var startAt: Long?,
    var endAt: Long?
) : BaseEntity() {
    constructor(product: ProductEntity, dto: DiscountDto) : this(
        product = product,
        discountPrice = dto.price,
        startAt = dto.startAt,
        endAt = dto.endAt
    )

    fun update(dto: DiscountDto): DiscountEntity {
        this.discountPrice = dto.price
        this.startAt = dto.startAt
        this.endAt = dto.endAt

        return this
    }
}