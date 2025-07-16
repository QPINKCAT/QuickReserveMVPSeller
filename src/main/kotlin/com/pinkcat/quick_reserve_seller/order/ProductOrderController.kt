package com.pinkcat.quick_reserve_seller.order

import com.pinkcat.quick_reserve_seller.order.dto.ProductOrderItemStatusUpdateReq
import com.pinkcat.quick_reserve_seller.order.dto.ProductOrderListRes
import com.pinkcat.quick_reserve_seller.order.model.ProductOrderItemStatus
import com.pinkcat.quick_reserve_seller.order.service.ProductOrderService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/product-order")
class ProductOrderController(
    private val productOrderService: ProductOrderService
) {
    @GetMapping("")
    fun findProductOrderList(
        @RequestParam productPk: Long?,
        @RequestParam status: List<ProductOrderItemStatus>,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<Page<ProductOrderListRes>> {
        return ResponseEntity.ok(
            productOrderService.findProductOrderList(
                productPk = productPk,
                status = status,
                page = page,
                size = size
            )
        )
    }

    @PutMapping("/status")
    fun updateProductOrderItemStatus(
        @RequestBody req: ProductOrderItemStatusUpdateReq
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(
            productOrderService.updateProductOrderItemStatus(req)
        )
    }
}