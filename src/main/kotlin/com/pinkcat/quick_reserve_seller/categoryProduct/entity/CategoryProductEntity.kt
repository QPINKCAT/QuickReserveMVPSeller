package com.pinkcat.quick_reserve_seller.categoryProduct.entity

import com.pinkcat.quick_reserve_seller.category.Entity.CategoryEntity
import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity
import jakarta.persistence.*
import lombok.Data

@Entity
@Data
@Table(name = "category_product")
@AttributeOverride(name = "pk", column = Column(name = "category_product_pk"))
class CategoryProductEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val category: CategoryEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    val product: ProductEntity,
) : BaseEntity()