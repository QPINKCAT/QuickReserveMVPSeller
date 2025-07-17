package com.pinkcat.quick_reserve_seller.admin.repository

import com.pinkcat.quick_reserve_seller.admin.entity.AdminEntity
import com.pinkcat.quick_reserve_seller.common.repository.ActiveRepository

interface AdminRepository : ActiveRepository<AdminEntity, Long> {
}