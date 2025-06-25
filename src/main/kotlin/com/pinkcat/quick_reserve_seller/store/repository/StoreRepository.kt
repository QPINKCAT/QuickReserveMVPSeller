package com.pinkcat.quick_reserve_seller.store.repository

import com.pinkcat.quick_reserve_seller.common.repository.ActiveRepository
import com.pinkcat.quick_reserve_seller.store.entity.StoreEntity

interface StoreRepository : ActiveRepository<StoreEntity, Long> {
}