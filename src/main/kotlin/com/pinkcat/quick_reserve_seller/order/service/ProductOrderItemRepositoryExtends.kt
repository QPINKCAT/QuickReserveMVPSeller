package com.pinkcat.quick_reserve_seller.order.service

import com.pinkcat.quick_reserve_seller.order.model.ProductOrderItemEntity
import com.pinkcat.quick_reserve_seller.order.model.ProductOrderItemStatus
import com.pinkcat.quick_reserve_seller.order.model.QProductOrderEntity.productOrderEntity
import com.pinkcat.quick_reserve_seller.order.model.QProductOrderItemEntity.productOrderItemEntity
import com.pinkcat.quick_reserve_seller.order.repository.ProductOrderItemRepository
import com.pinkcat.quick_reserve_seller.product.entity.QProductEntity.productEntity
import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

@Component
class ProductOrderItemRepositoryExtends(
    private val productOrderItemRepository: ProductOrderItemRepository,

    private val jpaQueryFactory: JPAQueryFactory
) {
    fun findALlByProductPkAndProductStatusInAndActive(
        productPk: Long?,
        productStatusList: List<ProductOrderItemStatus>,
        active: Boolean,
        page: Int,
        size: Int
    ): Page<ProductOrderItemEntity> {
        val booleanBuilder = BooleanBuilder()

        if (productPk != null) booleanBuilder.and(productEntity.pk.eq(productPk))
        if (productStatusList.isNotEmpty()) booleanBuilder.and(productOrderItemEntity.status.`in`(productStatusList))

        booleanBuilder.and(productOrderItemEntity.active.eq(active))

        val result = jpaQueryFactory
            .select(productOrderItemEntity)
            .join(productOrderEntity, productOrderItemEntity.productOrder).fetchJoin()
            .join(productEntity, productOrderItemEntity.product).fetchJoin()
            .where(booleanBuilder)
            .orderBy(productOrderEntity.orderAt.desc(), productEntity.pk.asc())
            .offset((page * size).toLong())
            .limit(size.toLong())
            .fetch()

        val totalCount = jpaQueryFactory
            .select(productOrderItemEntity.countDistinct())
            .join(productOrderEntity, productOrderItemEntity.productOrder)
            .join(productEntity, productOrderItemEntity.product)
            .where(booleanBuilder)
            .fetchOne() ?: 0

        return PageImpl(
            result,
            PageRequest.of(page, size),
            totalCount
        )
    }

    fun findAllByPkInAndActive(pks: Collection<Long>, active: Boolean) =
        productOrderItemRepository.findAllByPkInAndActive(pks, active)

    fun saveAll(entities: Collection<ProductOrderItemEntity>) = productOrderItemRepository.saveAll(entities)
}