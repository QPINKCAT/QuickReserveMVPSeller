package com.pinkcat.quick_reserve_seller.order.model

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import com.pinkcat.quick_reserve_seller.customer.model.CustomerEntity
import com.pinkcat.quick_reserve_seller.payment.model.PaymentEntity
import jakarta.persistence.*
import lombok.Data

@Entity
@Data
class ProductOrderEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val customer: CustomerEntity,
    @Column(length = 30)
    var orderNum: String,
    val orderAt: Long,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    val items: MutableList<ProductOrderItemEntity> = mutableListOf(),

    @OneToOne(mappedBy = "productOrder", fetch = FetchType.LAZY)
    var payment: PaymentEntity? = null,
) : BaseEntity()