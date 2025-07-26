package com.pinkcat.quick_reserve_seller.seller.entity

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import com.pinkcat.quick_reserve_seller.store.entity.StoreEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.Data

@Entity
@Data
@Table(name = "seller")
class SellerEntity(
        val id: String,
        val name: String,
        val password: String,
        val phoneNumber: String,
        val email: String,
        @ManyToOne(fetch = FetchType.LAZY)
        val store: StoreEntity? = null
) : BaseEntity()