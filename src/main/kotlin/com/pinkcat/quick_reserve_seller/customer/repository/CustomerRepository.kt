package com.pinkcat.quick_reserve_seller.customer.repository

import com.pinkcat.quick_reserve_seller.common.repository.ActiveRepository
import com.pinkcat.quick_reserve_seller.customer.model.CustomerEntity

interface CustomerRepository : ActiveRepository<CustomerEntity, Long> {
}