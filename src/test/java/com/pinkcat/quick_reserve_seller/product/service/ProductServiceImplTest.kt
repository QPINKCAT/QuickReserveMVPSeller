package com.pinkcat.quick_reserve_seller.product.service

import com.pinkcat.quick_reserve_seller.category.Entity.CategoryEntity
import com.pinkcat.quick_reserve_seller.category.repository.CategoryRepository
import com.pinkcat.quick_reserve_seller.categoryProduct.entity.CategoryProductEntity
import com.pinkcat.quick_reserve_seller.categoryProduct.repository.CategoryProductRepository
import com.pinkcat.quick_reserve_seller.common.aws.AwsUtil
import com.pinkcat.quick_reserve_seller.discount.dto.DiscountDto
import com.pinkcat.quick_reserve_seller.discount.entity.DiscountEntity
import com.pinkcat.quick_reserve_seller.discount.repository.DiscountRepository
import com.pinkcat.quick_reserve_seller.product.dto.ProductReq
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity
import com.pinkcat.quick_reserve_seller.product.entity.ProductStatus
import com.pinkcat.quick_reserve_seller.product.repository.ProductRepository
import com.pinkcat.quick_reserve_seller.product.validation.ProductValidator
import com.pinkcat.quick_reserve_seller.seller.entity.SellerEntity
import com.pinkcat.quick_reserve_seller.seller.repository.SellerRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class ProductServiceImplTest {
    private val categoryRepository = mockk<CategoryRepository>()
    private val categoryProductRepository = mockk<CategoryProductRepository>()
    private val discountRepository = mockk<DiscountRepository>()
    private val productRepository = mockk<ProductRepository>()
    private val sellerRepository = mockk<SellerRepository>()
    private val productValidator = mockk<ProductValidator>()
    private val awsUtil = mockk<AwsUtil>()
    private val productService = ProductServiceImpl(
        categoryRepository,
        categoryProductRepository,
        discountRepository,
        productRepository,
        sellerRepository,
        productValidator,
        awsUtil
    )

    @Nested
    @DisplayName("상품 생성")
    inner class CreateProduct {
        private fun baseReq(): ProductReq = ProductReq(
            categoryPks = listOf(1L, 2L),
            name = "테스트 상품",
            description = "테스트 설명",
            price = 10000,
            stock = 100,
            status = ProductStatus.ON,
            discount = null
        )

        private fun getSeller(pk: Long = 1): SellerEntity = SellerEntity(
            name = "seller",
            password = "password",
            phoneNumber = "01000000001",
            email = "seller@gmail.com"
        ).also { it.pk = pk }

        private fun getCategory(pk: Long = 1, name: String): CategoryEntity = CategoryEntity(
            categoryName = name,
            categoryOrder = pk.toInt(),
            topCategory = null,
        ).also { it.pk = pk }

        @Test
        fun `상품 생성 성공`() {
            val req = baseReq()
            val seller = getSeller()
            val categories = req.categoryPks.map { categoryPk ->
                getCategory(name = "category$categoryPk")
            }
            val product = ProductEntity(seller = seller, req = req).also {
                it.pk = 1L
            }

            every { productValidator.createReqValidCheck(req) } returns Unit
            every { sellerRepository.findByPkAndActive(seller.pk, true) } returns Optional.of(seller)
            every { categoryRepository.findAllByPkInAndActive(req.categoryPks, true) } returns categories
            every { productRepository.save(any()) } returns product
            every { categoryProductRepository.save(any()) } answers {
                firstArg<CategoryProductEntity>()
                    .also { it.pk = it.category.pk }
            }

            productService.createProduct(seller.pk, req)

            verify(exactly = 1) { productRepository.save(any()) }
            verify(exactly = 0) { discountRepository.save(any()) }
            verify(exactly = req.categoryPks.size) { categoryProductRepository.save(any()) }
        }

        @Test
        fun `할인 상품 생성 성공`() {
            val req = baseReq().let { req ->
                req.copy(
                    discount = DiscountDto(
                        price = req.price - 1000,
                        startAt = Instant.now().toEpochMilli() + (1000 * 60 * 60 * 24),
                        endAt = Instant.now().toEpochMilli() + (1000 * 60 * 60 * 24 * 7)
                    )
                )
            }
            val seller = getSeller()
            val categories = req.categoryPks.map { categoryPk ->
                getCategory(name = "category$categoryPk")
            }
            val product = ProductEntity(seller = seller, req = req).also {
                it.pk = 1L
            }
            val discount = DiscountEntity(product = product, dto = req.discount!!).also {
                it.pk = 1L
            }

            every { productValidator.createReqValidCheck(req) } returns Unit
            every { sellerRepository.findByPkAndActive(any(), true) } returns Optional.of(seller)
            every { categoryRepository.findAllByPkInAndActive(req.categoryPks, true) } returns categories
            every { productRepository.save(any()) } returns product
            every { discountRepository.save(any()) } returns discount
            every { categoryProductRepository.save(any()) } answers {
                firstArg<CategoryProductEntity>()
                    .also { it.pk = it.category.pk }
            }

            productService.createProduct(seller.pk, req)

            verify(exactly = 1) { productRepository.save(any()) }
            verify(exactly = 1) { discountRepository.save(any()) }
            verify(exactly = req.categoryPks.size) { categoryProductRepository.save(any()) }
        }
    }

    @Test
    fun findProductList() {
    }

    @Test
    fun findProduct() {
    }

    @Test
    fun updateProduct() {
    }

    @Test
    fun deleteProduct() {
    }

    @Test
    fun getPresignedUrl() {
    }

    @Test
    fun productReqValidCheck() {
    }

    @Nested
    @DisplayName("유효성검사")
    inner class ProductReqValidCheck {

    }
}