package com.pinkcat.quick_reserve_seller.payment.repository

import com.pinkcat.quick_reserve_seller.common.repository.ActiveRepository
import com.pinkcat.quick_reserve_seller.payment.model.PaymentEntity

interface PaymentRepository : ActiveRepository<PaymentEntity, Long> {
}