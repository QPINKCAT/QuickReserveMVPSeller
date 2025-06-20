package com.pinkcat.quick_reserve_seller.category.service

import com.pinkcat.quick_reserve_seller.category.dto.CategoryRes

interface CategoryService {
    fun findAll(): List<CategoryRes>
}