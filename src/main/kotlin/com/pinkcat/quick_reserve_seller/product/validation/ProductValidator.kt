package com.pinkcat.quick_reserve_seller.product.validation

import com.pinkcat.quick_reserve_seller.category.exception.CategoryNotFoundException
import com.pinkcat.quick_reserve_seller.category.exception.CategoryNotTopCategoryException
import com.pinkcat.quick_reserve_seller.category.repository.CategoryRepository
import com.pinkcat.quick_reserve_seller.product.dto.ProductReq
import com.pinkcat.quick_reserve_seller.product.exception.ProductReqInvalidException
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class ProductValidator(
    private val categoryRepository: CategoryRepository
) {
    fun createReqValidCheck(req: ProductReq) {
        reqValidCheck(req)
    }

    fun updateReqValidCheck(req: ProductReq) {
        reqValidCheck(req)
    }

    private fun reqValidCheck(req: ProductReq) {
        req.categoryPks.forEach { categoryPk ->
            if (!categoryRepository.existsByPkAndActive(categoryPk, true))
                throw CategoryNotFoundException("]-----] ProductValidator::productReqValidCheck Category Not Found(req: $req) [-----[")
            if (categoryRepository.existsByTopCategoryPkAndActive(categoryPk, true))
                throw CategoryNotTopCategoryException("]-----] ProductValidator::productReqValidCheck Category Have Leaf(req: $req) [-----[")
        }
        if (req.discount != null) {
            if (req.discount.price <= 0 || req.discount.price >= req.price) throw ProductReqInvalidException("]-----] ProductValidator::productReqValidCheck Product Request Invalid(req: $req) [-----[")
            if (req.discount.startAt != null && req.discount.startAt < Instant.now().toEpochMilli())
                throw ProductReqInvalidException("]-----] ProductValidator::productReqValidCheck Product Request Invalid(req: $req) [-----[")
            if (req.discount.endAt != null && req.discount.startAt != null && req.discount.endAt < req.discount.startAt)
                throw ProductReqInvalidException("]-----] ProductValidator::productReqValidCheck Product Request Invalid(req: $req) [-----[")
        }
    }
}