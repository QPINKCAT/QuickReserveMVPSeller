package com.pinkcat.quick_reserve_seller.seller.entity

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table
import lombok.Data

@Entity
@Data
@Table(name = "seller")
class SellerEntity(
    val name: String,
    val password: String,
    val phoneNumber: String,
    val email: String
) : BaseEntity()