package com.pinkcat.quick_reserve_seller.category.service

import com.pinkcat.quick_reserve_seller.category.repository.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
) : CategoryService {
}