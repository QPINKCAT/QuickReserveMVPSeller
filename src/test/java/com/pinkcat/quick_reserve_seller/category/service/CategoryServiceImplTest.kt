package com.pinkcat.quick_reserve_seller.category.service

import com.pinkcat.quick_reserve_seller.category.Entity.CategoryEntity
import com.pinkcat.quick_reserve_seller.category.repository.CategoryRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class CategoryServiceImplTest {
    private val categoryRepository: CategoryRepository = mockk()
    private val categoryService = CategoryServiceImpl(categoryRepository)

    @Test
    fun findAll() {
        // given
        val categories = listOf(
            CategoryEntity(
                categoryName = "test1",
                categoryOrder = 1,
                topCategory = null
            ).also {
                it.pk = 1
                it.active = true
            },
            CategoryEntity(
                categoryName = "test2",
                categoryOrder = 1,
                topCategory = null
            ).also {
                it.pk = 2
                it.active = true
            },
            CategoryEntity(
                categoryName = "test3",
                categoryOrder = 1,
                topCategory = null
            ).also {
                it.pk = 3
                it.active = false
            }
        )

        every { categoryRepository.findAllByActive(true) } returns categories.filter { it.active }

        // when
        val result = categoryService.findAll()

        // then
        assertThat(result).hasSize(2)
        assertThat(result[0].pk).isEqualTo(1L)
        assertThat(result[1].pk).isEqualTo(2L)

        verify(exactly = 1) { categoryRepository.findAllByActive(true) }
    }
}