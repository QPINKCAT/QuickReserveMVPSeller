package com.pinkcat.quick_reserve_seller.store.entity

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table
import lombok.Data

@Entity
@Data
@Table(name = "store")
class StoreEntity(
    val name: String,
    val address: String,
    val contactNumber: String
) : BaseEntity()