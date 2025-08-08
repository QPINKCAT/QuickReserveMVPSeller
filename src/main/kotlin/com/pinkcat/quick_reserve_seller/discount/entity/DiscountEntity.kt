package com.pinkcat.quick_reserve_seller.discount.entity

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import com.pinkcat.quick_reserve_seller.discount.dto.DiscountReq
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity
import jakarta.persistence.*
import lombok.Data

@Entity
@Data
@Table(name = "discount")
@AttributeOverride(name = "pk", column = Column(name = "discount_pk"))
class DiscountEntity(
    @OneToOne(fetch = FetchType.LAZY)
    val product: ProductEntity,
    @Column(name = "discount_price")
    var discountPrice: Int,
    @Column(name = "discount_start_at")
    var startAt: Long?,
    @Column(name = "discount_end_at")
    var endAt: Long?
) : BaseEntity() {
    constructor(product: ProductEntity, dto: DiscountReq) : this(
        product = product,
        discountPrice = dto.price,
        startAt = dto.startAt,
        endAt = dto.endAt
    ) {
        product.discount = this
    }

    fun update(dto: DiscountReq): DiscountEntity {
        this.discountPrice = dto.price
        this.startAt = dto.startAt
        this.endAt = dto.endAt

        return this
    }
}