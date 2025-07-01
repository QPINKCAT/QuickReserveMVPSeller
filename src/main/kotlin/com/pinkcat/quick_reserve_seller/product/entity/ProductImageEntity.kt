package com.pinkcat.quick_reserve_seller.product.entity

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import jakarta.persistence.*
import lombok.Data

@Entity
@Data
@Table(
    name = "product_image",
    uniqueConstraints = [UniqueConstraint(name = "UK_product_order", columnNames = ["product_pk", "display_order"])]
)
class ProductImageEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val product: ProductEntity,
    val url: String,
    val displayOrder: Int,
) : BaseEntity()