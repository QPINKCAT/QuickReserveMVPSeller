package com.pinkcat.quick_reserve_seller.order.dto

import com.pinkcat.quick_reserve_seller.customer.model.CustomerEntity
import com.pinkcat.quick_reserve_seller.order.model.ProductOrderItemEntity
import com.pinkcat.quick_reserve_seller.order.model.ProductOrderItemStatus

class ProductOrderDto {
}

data class ProductOrderListRes(
    val pk: Long,
    val price: Int,
    val quantity: Int,
    val status: ProductOrderItemStatus,
    val orderAt: Long,
    val customer: CustomerView
) {
    constructor(productOrderItemEntity: ProductOrderItemEntity) : this(
        pk = productOrderItemEntity.pk,
        price = productOrderItemEntity.salePrice,
        quantity = productOrderItemEntity.quantity,
        status = productOrderItemEntity.status,
        orderAt = productOrderItemEntity.productOrder.orderAt,
        customer = CustomerView(productOrderItemEntity.productOrder.customer)
    )
}

data class CustomerView(
    val name: String,
    val phoneNumber: String,
) {
    constructor(customerEntity: CustomerEntity) : this(
        name = customerEntity.name,
        phoneNumber = customerEntity.phoneNumber,
    )
}

data class ProductOrderItemStatusUpdateReq(
    val pks: List<Long>,
    val status: List<ProductOrderItemStatus>,
)