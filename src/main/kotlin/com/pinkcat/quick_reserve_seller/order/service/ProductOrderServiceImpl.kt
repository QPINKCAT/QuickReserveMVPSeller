package com.pinkcat.quick_reserve_seller.order.service

import com.pinkcat.quick_reserve_seller.order.dto.ProductOrderItemStatusUpdateReq
import com.pinkcat.quick_reserve_seller.order.dto.ProductOrderListRes
import com.pinkcat.quick_reserve_seller.order.exception.ProductOrderItemNotFoundException
import com.pinkcat.quick_reserve_seller.order.exception.ProductOrderItemStatusUpdateReqInvalid
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

    @Transactional
    override fun updateProductOrderItemStatus(
        req: ProductOrderItemStatusUpdateReq
    ) {
        if (req.status.keys.size != req.status.size)
            throw ProductOrderItemStatusUpdateReqInvalid("]-----] ProductOrderServiceImpl::updateProductOrderItemStatus Pk, Status Size Not Equal(req: $req) [-----[")

        val productOrderItems = productOrderItemRepositoryImpl.findAllByPkInAndActive(req.status.keys, true)
            .associateBy { it.pk }

        req.status.forEach { pk, status ->
            val productOrderItem = productOrderItems[pk]
                ?: throw ProductOrderItemNotFoundException("]-----] ProductOrderServiceImpl::updateProductOrderItemStatus ProductOrderItem Not Found(pk: $pk) [-----[")

            productOrderItem.status = status
        }

        productOrderItemRepositoryImpl.saveAll(productOrderItems.map { it.value })
    }
}