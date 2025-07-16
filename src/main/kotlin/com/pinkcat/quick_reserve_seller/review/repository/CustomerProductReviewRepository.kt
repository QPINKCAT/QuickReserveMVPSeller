package com.pinkcat.quick_reserve_seller.review.repository

import com.pinkcat.quick_reserve_seller.common.repository.ActiveRepository
import com.pinkcat.quick_reserve_seller.review.entity.CustomerProductReview
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query

interface CustomerProductReviewRepository : ActiveRepository<CustomerProductReview, Long> {
    @Query(
        """
            SELECT cpr FROM CustomerProductReview cpr
            JOIN ProductOrderItemEntity poie ON cpr.orderItem = poie
            WHERE 
                poie.product.pk = :productPk AND 
                cpr.active = :active
        """
    )
    fun findAllByProductPkAndActive(productPk: Long, active: Boolean, pageable: Pageable): Page<CustomerProductReview>
}