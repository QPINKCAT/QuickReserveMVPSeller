package com.pinkcat.quick_reserve_seller.payment.model

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import com.pinkcat.quick_reserve_seller.order.model.ProductOrderEntity
import jakarta.persistence.*
import lombok.Data

@Entity
@Data
class PaymentEntity(
    @OneToOne(fetch = FetchType.LAZY)
    val productOrder: ProductOrderEntity,
    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    val status: PaymentStatus,
    val totalPrice: Int
) : BaseEntity()

enum class PaymentStatus {
    COMPLETED,
    CANCELLED
}