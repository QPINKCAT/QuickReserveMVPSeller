package com.pinkcat.quick_reserve_seller.category

import com.pinkcat.quick_reserve_seller.category.service.CategoryService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/category")
class CategoryController(
    private val categoryService: CategoryService
) {

}