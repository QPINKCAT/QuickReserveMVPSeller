package com.pinkcat.quick_reserve_seller.product.validation

import com.pinkcat.quick_reserve_seller.category.exception.CategoryNotFoundException
import com.pinkcat.quick_reserve_seller.category.exception.CategoryNotTopCategoryException
import com.pinkcat.quick_reserve_seller.category.repository.CategoryRepository
import com.pinkcat.quick_reserve_seller.discount.dto.DiscountDto
import com.pinkcat.quick_reserve_seller.product.dto.ProductReq
import com.pinkcat.quick_reserve_seller.product.entity.ProductStatus
import com.pinkcat.quick_reserve_seller.product.exception.ProductReqInvalidException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.*
import java.time.Instant

class ProductValidatorTest {
    private val categoryRepository = mockk<CategoryRepository>()
    private val productValidator = ProductValidator(categoryRepository)

    private val ONE_DAY = 1000L * 60 * 60 * 24

    private fun baseReq() = ProductReq(
        categoryPks = listOf(1L),
        name = "테스트 상품",
        description = "테스트 설명",
        price = 10000,
        stock = 100,
        status = ProductStatus.ON,
        discount = null
    )

    @Nested
    @DisplayName("createReqValidCheck 테스트")
    inner class CreateReqValidCheck {
        @Test
        fun `카테고리가 비어있으면 실패`() = assertThrowsCategoryMissing { productValidator.createReqValidCheck(it) }
        @Test
        fun `카테고리가 존재하지 않으면 실패`() = assertThrowsCategoryNotFound { productValidator.createReqValidCheck(it) }
        @Test
        fun `상위 카테고리면 실패`() = assertThrowsTopCategory { productValidator.createReqValidCheck(it) }
        @Test
        fun `가격이 0 이하이면 실패`() = assertThrowsInvalidPrice { productValidator.createReqValidCheck(it) }
        @Test
        fun `재고가 음수면 실패`() = assertThrowsInvalidStock { productValidator.createReqValidCheck(it) }
        @Test
        fun `할인 가격이 0 이하이면 실패`() = assertThrowsDiscountPriceTooLow { productValidator.createReqValidCheck(it) }
        @Test
        fun `할인 가격이 상품 가격 이상이면 실패`() = assertThrowsDiscountPriceTooHigh { productValidator.createReqValidCheck(it) }
        @Test
        fun `할인 시작 시간이 과거면 실패`() = assertThrowsDiscountStartBeforeNow { productValidator.createReqValidCheck(it) }
        @Test
        fun `할인 종료가 시작보다 빠르면 실패`() = assertThrowsDiscountEndBeforeStart { productValidator.createReqValidCheck(it) }
        @Test
        fun `정상 요청이면 성공`() = assertValid { productValidator.createReqValidCheck(it) }
    }

    @Nested
    @DisplayName("updateReqValidCheck 테스트")
    inner class UpdateReqValidCheck {
        @Test
        fun `카테고리가 비어있으면 실패`() = assertThrowsCategoryMissing { productValidator.updateReqValidCheck(it) }
        @Test
        fun `카테고리가 존재하지 않으면 실패`() = assertThrowsCategoryNotFound { productValidator.updateReqValidCheck(it) }
        @Test
        fun `상위 카테고리면 실패`() = assertThrowsTopCategory { productValidator.updateReqValidCheck(it) }
        @Test
        fun `가격이 0 이하이면 실패`() = assertThrowsInvalidPrice { productValidator.updateReqValidCheck(it) }
        @Test
        fun `재고가 음수면 실패`() = assertThrowsInvalidStock { productValidator.updateReqValidCheck(it) }
        @Test
        fun `할인 가격이 0 이하이면 실패`() = assertThrowsDiscountPriceTooLow { productValidator.updateReqValidCheck(it) }
        @Test
        fun `할인 가격이 상품 가격 이상이면 실패`() = assertThrowsDiscountPriceTooHigh { productValidator.updateReqValidCheck(it) }
        @Test
        fun `할인 시작 시간이 과거면 실패`() = assertThrowsDiscountStartBeforeNow { productValidator.updateReqValidCheck(it) }
        @Test
        fun `할인 종료가 시작보다 빠르면 실패`() = assertThrowsDiscountEndBeforeStart { productValidator.updateReqValidCheck(it) }
        @Test
        fun `정상 요청이면 성공`() = assertValid { productValidator.updateReqValidCheck(it) }
    }

    private fun stubCategoryExists(pks: List<Long>, exists: Boolean = true, hasChildren: Boolean = false) {
        pks.forEach { pk ->
            every { categoryRepository.existsByPkAndActive(pk, true) } returns exists
            every { categoryRepository.existsByTopCategoryPkAndActive(pk, true) } returns hasChildren
        }
    }

    private fun assertThrowsCategoryMissing(check: (ProductReq) -> Unit) {
        val req = baseReq().copy(categoryPks = listOf())
        assertThrows<ProductReqInvalidException> { check(req) }
    }

    private fun assertThrowsCategoryNotFound(check: (ProductReq) -> Unit) {
        val req = baseReq()

        stubCategoryExists(req.categoryPks, exists = false)

        assertThrows<CategoryNotFoundException> { check(req) }
    }

    private fun assertThrowsTopCategory(check: (ProductReq) -> Unit) {
        val req = baseReq()

        stubCategoryExists(req.categoryPks, exists = true, hasChildren = true)

        assertThrows<CategoryNotTopCategoryException> { check(req) }
    }

    private fun assertThrowsInvalidPrice(check: (ProductReq) -> Unit) {
        val req = baseReq().copy(price = 0)

        stubCategoryExists(req.categoryPks)

        assertThrows<ProductReqInvalidException> { check(req) }
    }

    private fun assertThrowsInvalidStock(check: (ProductReq) -> Unit) {
        val req = baseReq().copy(stock = -1)

        stubCategoryExists(req.categoryPks)

        assertThrows<ProductReqInvalidException> { check(req) }
    }

    private fun assertThrowsDiscountPriceTooLow(check: (ProductReq) -> Unit) {
        val req = baseReq().copy(
            discount = DiscountDto(price = 0, startAt = nowPlus(1), endAt = nowPlus(7))
        )
        stubCategoryExists(req.categoryPks)
        assertThrows<ProductReqInvalidException> { check(req) }
    }

    private fun assertThrowsDiscountPriceTooHigh(check: (ProductReq) -> Unit) {
        val req = baseReq().copy(
            discount = DiscountDto(price = baseReq().price + 1_000, startAt = nowPlus(1), endAt = nowPlus(7))
        )
        stubCategoryExists(req.categoryPks)
        assertThrows<ProductReqInvalidException> { check(req) }
    }

    private fun assertThrowsDiscountStartBeforeNow(check: (ProductReq) -> Unit) {
        val req = baseReq().copy(
            discount = DiscountDto(price = baseReq().price - 1_000, startAt = nowMinus(1), endAt = nowPlus(7))
        )
        stubCategoryExists(req.categoryPks)
        assertThrows<ProductReqInvalidException> { check(req) }
    }

    private fun assertThrowsDiscountEndBeforeStart(check: (ProductReq) -> Unit) {
        val req = baseReq().copy(
            discount = DiscountDto(price = baseReq().price - 1_000, startAt = nowPlus(1), endAt = nowMinus(7))
        )
        stubCategoryExists(req.categoryPks)
        assertThrows<ProductReqInvalidException> { check(req) }
    }

    private fun assertValid(check: (ProductReq) -> Unit) {
        val req = baseReq().copy(
            discount = DiscountDto(price = baseReq().price - 1_000, startAt = nowPlus(1), endAt = nowPlus(7))
        )
        stubCategoryExists(req.categoryPks)
        assertDoesNotThrow { check(req) }
    }

    private fun nowPlus(days: Long): Long = Instant.now().toEpochMilli() + (ONE_DAY * days)
    private fun nowMinus(days: Long): Long = Instant.now().toEpochMilli() - (ONE_DAY * days)
}