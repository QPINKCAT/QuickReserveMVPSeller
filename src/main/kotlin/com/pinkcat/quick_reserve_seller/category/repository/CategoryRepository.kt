package com.pinkcat.quick_reserve_seller.category.repository

import com.pinkcat.quick_reserve_seller.category.Entity.CategoryEntity
import com.pinkcat.quick_reserve_seller.common.repository.ActiveRepository
import org.springframework.data.jpa.repository.Query

interface CategoryRepository : ActiveRepository<CategoryEntity, Long> {
    fun existsByPkAndActive(pk: Long, active: Boolean): Boolean
    fun existsByTopCategoryPkAndActive(topCategoryPk: Long, active: Boolean): Boolean
    fun findAllByPkInAndActive(pks: List<Long>, active: Boolean): List<CategoryEntity>

    @Query(
        value = """
            WITH RECURSIVE category_tree AS (
                SELECT pk
                FROM category
                WHERE pk = :pk -- 시작 카테고리
                UNION ALL
                SELECT c.*
                FROM category c
                INNER JOIN category_tree ct ON c.top_category_pk = ct.pk
            )
            SELECT pk FROM category_tree;
        """,
        nativeQuery = true
    )
    fun findAllChildrenPkRecursive(pk: Long): List<Long>
}