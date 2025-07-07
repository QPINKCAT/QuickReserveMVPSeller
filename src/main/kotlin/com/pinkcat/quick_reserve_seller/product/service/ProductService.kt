package com.pinkcat.quick_reserve_seller.product.service

import com.pinkcat.quick_reserve_seller.product.dto.PresignedUrlReq
import com.pinkcat.quick_reserve_seller.product.dto.ProductListRes
import com.pinkcat.quick_reserve_seller.product.dto.ProductReq
import com.pinkcat.quick_reserve_seller.product.dto.ProductRes
import org.springframework.data.domain.Page

interface ProductService {
    fun createProduct(sellerPk: Long, req: ProductReq)
    fun findProductList(categoryPk: Long?, page: Int, size: Int): Page<ProductListRes>
    fun findProduct(productPk: Long): ProductRes
    fun updateProduct(sellerPk: Long, productPk: Long, req: ProductReq): Boolean
    fun deleteProduct(sellerPk: Long, productPk: Long): Boolean

    fun getPresignedUrl(req: PresignedUrlReq): String
}