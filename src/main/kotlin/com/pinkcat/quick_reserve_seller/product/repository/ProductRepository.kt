package com.pinkcat.quick_reserve_seller.product.repository

import com.pinkcat.quick_reserve_seller.common.repository.ActiveRepository
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query

interface ProductRepository : ActiveRepository<ProductEntity, Long> {
    @Query(
        value = """
            SELECT pe FROM ProductEntity pe
            LEFT JOIN CategoryProductEntity cpe ON cpe.product = pe
            WHERE cpe.category.pk in :categoryPks AND pe.active = :active
        """
    )
    fun findAllByCategoryPk(categoryPks: List<Long>, active: Boolean, pageable: Pageable): Page<ProductEntity>
}