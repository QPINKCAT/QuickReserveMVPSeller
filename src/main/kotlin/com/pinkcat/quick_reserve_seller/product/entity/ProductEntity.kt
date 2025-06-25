package com.pinkcat.quick_reserve_seller.product.entity

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import com.pinkcat.quick_reserve_seller.seller.entity.SellerEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.Data

@Entity
@Data
@Table(name = "product")
class ProductEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val seller: SellerEntity,
    val name: String,
    val description: String,
    val price: Int,
    val stock: Int?,
    val avgRating: Double,
    val reviewCount: Int,
    val status: ProductStatus,
) : BaseEntity()

enum class ProductStatus {
    ON, OFF;
}