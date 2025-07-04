package com.pinkcat.quick_reserve_seller.product.repository

import com.pinkcat.quick_reserve_seller.category.Entity.QCategoryEntity.categoryEntity
import com.pinkcat.quick_reserve_seller.categoryProduct.entity.QCategoryProductEntity.categoryProductEntity
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity
import com.pinkcat.quick_reserve_seller.product.entity.QProductEntity.productEntity
import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

@Component
class ProductRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : ProductRepositoryCustom {
    override fun findAllByCategory(categoryPks: List<Long>, page: Int, size: Int): Page<ProductEntity> {
        val booleanBuilder = BooleanBuilder()

        if (categoryPks.isNotEmpty()) booleanBuilder.and(categoryEntity.pk.`in`(categoryPks))

        val result = jpaQueryFactory
            .select(productEntity)
            .from(productEntity)
            .join(categoryProductEntity).on(categoryProductEntity.product.eq(productEntity))
            .join(categoryProductEntity.category, categoryEntity)
            .where(booleanBuilder)
            .offset((page * size).toLong())
            .limit(size.toLong())
            .fetch()

        val totalCount = jpaQueryFactory
            .select(productEntity.countDistinct())
            .from(productEntity)
            .join(categoryProductEntity).on(categoryProductEntity.product.eq(productEntity))
            .join(categoryProductEntity.category, categoryEntity)
            .where(booleanBuilder)
            .offset((page * size).toLong())
            .limit(size.toLong())
            .fetchOne() ?: 0

        return PageImpl<ProductEntity>(result, PageRequest.of(page, size), totalCount)
    }
}