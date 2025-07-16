package com.pinkcat.quick_reserve_seller.product

import com.pinkcat.quick_reserve_seller.product.dto.*
import com.pinkcat.quick_reserve_seller.product.service.ProductService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/product")
class ProductController(
    private val productService: ProductService
) {
    @PostMapping
    fun createProduct(sellerPk: Long, req: ProductReq) {
        return productService.createProduct(sellerPk, req)
    }

    @GetMapping("")
    fun findAll(categoryPk: Long?, page: Int, size: Int): ResponseEntity<Page<ProductListRes>> {
        return ResponseEntity.ok(productService.findProductList(categoryPk, page, size))
    }

    @GetMapping("/{productPk}")
    fun findProduct(@PathVariable productPk: Long): ResponseEntity<ProductRes> {
        return ResponseEntity.ok(productService.findProduct(productPk))
    }

    @PutMapping("/{productPk}")
    fun updateProduct(@PathVariable productPk: Long, sellerPk: Long, req: ProductReq): ResponseEntity<Boolean> {
        return ResponseEntity.ok(productService.updateProduct(sellerPk, productPk, req))
    }

    @DeleteMapping("/{productPk}")
    fun deleteProduct(@PathVariable productPk: Long, sellerPk: Long): ResponseEntity<Boolean> {
        return ResponseEntity.ok(productService.deleteProduct(sellerPk, productPk))
    }

    @PostMapping("/presigned-url")
    fun getPresignedUrl(@RequestBody req: PresignedUrlReq): Any {
        return ResponseEntity.ok(productService.getPresignedUrl(req))
    }

    @GetMapping("/{productPk}/review")
    fun findAllReview(
        @PathVariable productPk: Long,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<Page<ProductReviewRes>> {
        return ResponseEntity.ok(productService.findAllReview(productPk, page, size))
    }
}