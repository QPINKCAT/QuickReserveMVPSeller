package com.pinkcat.quick_reserve_seller.order.model

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity
import jakarta.persistence.*
import lombok.Data

@Entity
@Data
class ProductOrderItemEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val productOrder: ProductOrderEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    val product: ProductEntity,
    val originalPrice: Int,
    val salePrice: Int,
    val quantity: Int,
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    val status: ProductOrderItemStatus
) : BaseEntity()

enum class ProductOrderItemStatus {
    PENDING,
    COMPLETED,
    CANCELLED,
    USED
}