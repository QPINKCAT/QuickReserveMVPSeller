package com.pinkcat.quick_reserve_seller.review.entity

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import com.pinkcat.quick_reserve_seller.customer.model.CustomerEntity
import com.pinkcat.quick_reserve_seller.order.model.ProductOrderItemEntity
import jakarta.persistence.*
import lombok.Data

@Entity
@Data
class CustomerProductReview(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_pk", nullable = false, updatable = false)
    val customer: CustomerEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_order_item_pk", nullable = false, updatable = false)
    val orderItem: ProductOrderItemEntity,
    @Column(name = "customer_product_review_rating", nullable = false)
    val rating: Int,
    @Column(name = "customer_product_review_comment")
    val comment: String
) : BaseEntity()