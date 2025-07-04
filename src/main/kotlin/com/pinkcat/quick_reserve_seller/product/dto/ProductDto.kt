package com.pinkcat.quick_reserve_seller.product.dto

import com.pinkcat.quick_reserve_seller.category.dto.CategoryRes
import com.pinkcat.quick_reserve_seller.discount.dto.DiscountDto
import com.pinkcat.quick_reserve_seller.hotDeal.dto.HotDealRes
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity
import com.pinkcat.quick_reserve_seller.product.entity.ProductStatus
import kotlin.math.round

data class ProductReq(
    val categoryPks: List<Long>,
    val name: String,
    val description: String,
    val price: Int,
    val stock: Int?,
    val status: ProductStatus,
    val discount: DiscountDto?,
)

data class ProductListRes(
    val pk: Long,
    val categories: List<CategoryRes>,
    val name: String,
    val description: String,
    val price: Int,
    val stock: Int?,
    val status: ProductStatus,
    val discount: DiscountDto?,
    val images: List<String>,
) {
    constructor(product: ProductEntity) : this(
        pk = product.pk!!,
        categories = product.categoryProducts.map { categoryProductEntity ->
            CategoryRes(categoryProductEntity.category)
        },
        name = product.name,
        description = product.description,
        price = product.price,
        stock = product.stock,
        status = product.status,
        discount = product.discount?.let { discount -> DiscountDto(discount) },
        images = product.images.map { it.url }
    )
}

data class ProductRes(
    val pk: Long,
    val categories: List<CategoryRes>,
    val name: String,
    val description: String,
    val price: Int,
    val stock: Int?,
    val reviewCount: Int,
    val avgRating: Int,
    val status: ProductStatus,
    val discount: DiscountDto?,
    val images: List<String>,
    val hotDeals: List<HotDealRes>
) {
    constructor(product: ProductEntity) : this(
        pk = product.pk!!,
        categories = product.categoryProducts.map { categoryProductEntity -> CategoryRes(categoryProductEntity.category) },
        name = product.name,
        description = product.description,
        price = product.price,
        reviewCount = product.reviewCount,
        avgRating = round(product.avgRating).toInt(),
        stock = product.stock,
        status = product.status,
        discount = product.discount?.let { discount -> DiscountDto(discount) },
        images = product.images.map { it.url },
        hotDeals = product.hotDealProducts.map { hotDealProductEntity -> HotDealRes(hotDealProductEntity.hotDeal) }
    )
}

data class PresignedUrlReq(
    val name: String,
    val contentType: String,
)