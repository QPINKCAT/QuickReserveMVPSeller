package com.pinkcat.quick_reserve_seller.product.repository

import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity
import org.springframework.data.domain.Page

interface ProductRepositoryCustom {
    fun findAllByCategory(categoryPks: List<Long>, page: Int, size: Int): Page<ProductEntity>
}