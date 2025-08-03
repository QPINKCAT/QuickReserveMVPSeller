package com.pinkcat.quick_reserve_seller.admin.entity

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "admin")
class AdminEntity(
        val id: String,
        val name: String,
        val password: String
) : BaseEntity()