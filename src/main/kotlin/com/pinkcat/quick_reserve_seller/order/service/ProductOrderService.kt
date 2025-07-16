package com.pinkcat.quick_reserve_seller.order.service

import com.pinkcat.quick_reserve_seller.order.dto.ProductOrderItemStatusUpdateReq
import com.pinkcat.quick_reserve_seller.order.dto.ProductOrderListRes
import com.pinkcat.quick_reserve_seller.order.model.ProductOrderItemStatus
import org.springframework.data.domain.Page

interface ProductOrderService {
    fun findProductOrderList(
        productPk: Long?,
        status: List<ProductOrderItemStatus>,
        page: Int,
        size: Int
    ): Page<ProductOrderListRes>

    fun updateProductOrderItemStatus(req: ProductOrderItemStatusUpdateReq)
}