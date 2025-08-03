package com.pinkcat.quick_reserve_seller.hotDeal.respository

import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealProductRequestLogEntity
import com.pinkcat.quick_reserve_seller.hotDeal.model.QHotDealProductRequestLogEntity
import com.pinkcat.quick_reserve_seller.hotDeal.dto.HotDealProductRequestSearchCondition
import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class HotDealProductRequestLogRepositoryImpl(
        private val queryFactory: JPAQueryFactory
) : HotDealProductRequestLogRepositoryCustom {

    override fun search(
            condition: HotDealProductRequestSearchCondition,
            pageable: Pageable,
            userPk: Long
    ): Page<HotDealProductRequestLogEntity> {
        val log = QHotDealProductRequestLogEntity.hotDealProductRequestLogEntity
        val sub = QHotDealProductRequestLogEntity("sub")

        val latestCreatedAtSubquery = JPAExpressions
                .select(sub.createdAt.max())
                .from(sub)
                .where(
                        sub.product.pk.eq(log.product.pk),
                        sub.hotDeal.pk.eq(log.hotDeal.pk),
                        sub.seller.pk.eq(userPk)
                )

        val where = BooleanBuilder()
                .and(log.seller.pk.eq(userPk))
                .and(log.createdAt.eq(latestCreatedAtSubquery))

        condition.hotDealPk?.let {
            where.and(log.hotDeal.pk.eq(it))
        }
        condition.productPk?.let {
            where.and(log.product.pk.eq(it))
        }
        condition.status?.let {
            where.and(log.hotDealProductRequestStatus.eq(it))
        }

        val content = queryFactory
                .selectFrom(log)
                .where(where)
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .orderBy(log.createdAt.desc())
                .fetch()

        val total = queryFactory
                .select(log.count())
                .from(log)
                .where(where)
                .fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }
}