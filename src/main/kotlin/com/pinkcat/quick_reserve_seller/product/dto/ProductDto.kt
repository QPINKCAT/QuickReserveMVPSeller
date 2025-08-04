package com.pinkcat.quick_reserve_seller.product.dto

import com.pinkcat.quick_reserve_seller.category.dto.CategoryRes
import com.pinkcat.quick_reserve_seller.customer.model.CustomerEntity
import com.pinkcat.quick_reserve_seller.discount.dto.DiscountDto
import com.pinkcat.quick_reserve_seller.discount.dto.DiscountReq
import com.pinkcat.quick_reserve_seller.hotDeal.dto.HotDealRes
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity
import com.pinkcat.quick_reserve_seller.product.entity.ProductStatus
import com.pinkcat.quick_reserve_seller.review.entity.CustomerProductReview
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import kotlin.math.round

data class ProductReq(
    @NotEmpty
    val categoryPks: List<Long>,
    @field:Pattern(regexp = "^[A-Za-z0-9가-힣][A-Za-z0-9가-힣 ]{0,29}\$")
    val name: String,
    @field:NotBlank
    val description: String,
    @field:Min(1)
    val price: Int,
    @field:Min(0)
    val stock: Int?,
    val status: ProductStatus,
    @field:Valid
    val discount: DiscountReq?,
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

data class ProductReviewRes(
    val customerProductReviewPk: Long,
    val customer: ReviewCustomerView,
    val comment: String,
    val rating: Int,
    val createdAt: Long
) {
    constructor(review: CustomerProductReview) : this(
        customerProductReviewPk = review.pk,
        customer = ReviewCustomerView(review.customer),
        comment = review.comment,
        rating = review.rating,
        createdAt = review.createdAt,
    )
}

data class ReviewCustomerView(
    val name: String,
    val phoneNumber: String,
) {
    constructor(customer: CustomerEntity) : this(
        name = customer.name,
        phoneNumber = customer.phoneNumber,
    )
}