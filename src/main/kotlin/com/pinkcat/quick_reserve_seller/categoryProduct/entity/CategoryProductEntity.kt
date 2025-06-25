package com.pinkcat.quick_reserve_seller.categoryProduct.entity

import com.pinkcat.quick_reserve_seller.category.Entity.CategoryEntity
import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.Data

@Entity
@Data
@Table(name = "category_product")
class CategoryProductEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val category: CategoryEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    val product: ProductEntity,
) : BaseEntity()