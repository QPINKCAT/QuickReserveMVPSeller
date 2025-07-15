package com.pinkcat.quick_reserve_seller.order.repository

import com.pinkcat.quick_reserve_seller.common.repository.ActiveRepository
import com.pinkcat.quick_reserve_seller.order.model.ProductOrderItemEntity

interface ProductOrderItemRepository : ActiveRepository<ProductOrderItemEntity, Long> {
}