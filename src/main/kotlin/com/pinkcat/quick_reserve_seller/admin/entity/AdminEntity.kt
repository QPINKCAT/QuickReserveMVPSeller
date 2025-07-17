package com.pinkcat.quick_reserve_seller.admin.entity

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import jakarta.persistence.Entity

@Entity
class AdminEntity(
    val name: String
) : BaseEntity()