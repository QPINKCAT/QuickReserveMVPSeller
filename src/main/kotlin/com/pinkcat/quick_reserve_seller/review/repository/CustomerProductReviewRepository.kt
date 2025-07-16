package com.pinkcat.quick_reserve_seller.review.repository

import com.pinkcat.quick_reserve_seller.common.repository.ActiveRepository
import com.pinkcat.quick_reserve_seller.review.entity.CustomerProductReview

interface CustomerProductReviewRepository : ActiveRepository<CustomerProductReview, Long> {
}