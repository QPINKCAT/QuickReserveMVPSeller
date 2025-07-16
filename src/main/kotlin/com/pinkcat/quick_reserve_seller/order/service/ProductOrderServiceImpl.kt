package com.pinkcat.quick_reserve_seller.order.service

import com.pinkcat.quick_reserve_seller.order.dto.ProductOrderListRes
import com.pinkcat.quick_reserve_seller.order.model.ProductOrderItemStatus
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductOrderServiceImpl(
    private val productOrderItemRepositoryImpl: ProductOrderItemRepositoryImpl
) : ProductOrderService {
    @Transactional(readOnly = true)
    override fun findProductOrderList(
        productPk: Long?,
        status: List<ProductOrderItemStatus>,
        page: Int,
        size: Int
    ): Page<ProductOrderListRes> {
        return productOrderItemRepositoryImpl.findALlByProductPkAndProductStatusInAndActive(
            productPk = productPk,
            productStatusList = status,
            active = true,
            page = page,
            size = size
        ).map { ProductOrderListRes(it) }
    }
}