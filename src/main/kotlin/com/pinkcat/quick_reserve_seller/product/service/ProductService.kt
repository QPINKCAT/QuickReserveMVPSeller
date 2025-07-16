package com.pinkcat.quick_reserve_seller.product.service

import com.pinkcat.quick_reserve_seller.product.dto.*
import org.springframework.data.domain.Page

interface ProductService {
    fun createProduct(sellerPk: Long, req: ProductReq)
    fun findProductList(categoryPk: Long?, page: Int, size: Int): Page<ProductListRes>
    fun findProduct(productPk: Long): ProductRes
    fun updateProduct(sellerPk: Long, productPk: Long, req: ProductReq): Boolean
    fun deleteProduct(sellerPk: Long, productPk: Long): Boolean

    fun getPresignedUrl(req: PresignedUrlReq): String

    fun findAllReview(productPk: Long, page: Int, size: Int): Page<ProductReviewRes>
}