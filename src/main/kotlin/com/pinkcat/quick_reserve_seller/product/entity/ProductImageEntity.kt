package com.pinkcat.quick_reserve_seller.product.entity

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import jakarta.persistence.*
import lombok.Data

@Entity
@Data
@Table(
    name = "product_image",
    uniqueConstraints = [UniqueConstraint(
        name = "UK_product_order",
        columnNames = ["product_pk", "product_image_display_order"]
    )]
)
@AttributeOverride(name = "pk", column = Column(name = "product_image_pk"))
class ProductImageEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val product: ProductEntity,
    @Column(length = 255, name = "product_image_url")
    val url: String,
    @Column(name = "product_image_display_order")
    val displayOrder: Int,
) : BaseEntity()