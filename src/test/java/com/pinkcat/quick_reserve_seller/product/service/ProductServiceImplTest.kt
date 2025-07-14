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
import com.pinkcat.quick_reserve_seller.product.dto.ProductRes
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity
import com.pinkcat.quick_reserve_seller.product.entity.ProductStatus
import com.pinkcat.quick_reserve_seller.product.exception.ProductNotFoundException
import com.pinkcat.quick_reserve_seller.product.repository.ProductRepository
import com.pinkcat.quick_reserve_seller.product.validation.ProductValidator
import com.pinkcat.quick_reserve_seller.seller.entity.SellerEntity
import com.pinkcat.quick_reserve_seller.seller.repository.SellerRepository
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
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

    @Nested
    @DisplayName("상품 목록 조회")
    inner class FindProductList() {
        @Test
        fun `categoryPk가 null이 아니면 자식 카테고리 포함하여 조회`() {
            val parentPk = 1L
            val childrenPk = listOf(2L, 3)

            every { categoryRepository.findAllChildrenPkRecursive(parentPk) } returns childrenPk

            val allPks = childrenPk + parentPk
            val page = 0
            val size = 10
            val pageable = PageRequest.of(page, size)

            every { productRepository.findAllByCategoryPk(allPks, true, pageable) } returns Page.empty(pageable)

            productService.findProductList(parentPk, page, size)

            verify(exactly = 1) { categoryRepository.findAllChildrenPkRecursive(parentPk) }
            verify(exactly = 1) { productRepository.findAllByCategoryPk(allPks, true, pageable) }
        }

        @Test
        fun `categoryPk가 null이면 바로 조회`() {
            val page = 0
            val size = 10
            val pageable = PageRequest.of(page, size)

            every { productRepository.findAllByCategoryPk(emptyList(), true, pageable) } returns Page.empty(pageable)

            productService.findProductList(null, page, size)

            verify(exactly = 0) { categoryRepository.findAllChildrenPkRecursive(any()) }
            verify(exactly = 1) { productRepository.findAllByCategoryPk(emptyList(), true, pageable) }
        }

        @Test
        fun `결과 Page Mapping 테스트`() {
            val products = listOf(
                makeProduct(1L, listOf()),
                makeProduct(2L, listOf())
            )
            val page = 0
            val size = 10
            val pageable = PageRequest.of(page, size)
            val pageImpl = PageImpl(products, pageable, 2)

            every { productRepository.findAllByCategoryPk(emptyList(), true, pageable) } returns pageImpl

            val result = productService.findProductList(null, page, size)

            assertThat(result.totalElements).isEqualTo(products.size.toLong())
            assertThat(result.pageable.pageNumber).isEqualTo(page)
            assertThat(result.map { it.pk }).containsExactly(1L, 2L)
        }

        fun makeProduct(productPk: Long = 1L, categoryPks: List<Long>): ProductEntity {
            val seller = SellerEntity(
                name = "seller",
                password = "password",
                phoneNumber = "01000000001",
                email = "seller@google.com"
            ).also { it.pk = 1 }
            return ProductEntity(
                seller = seller,
                req = ProductReq(
                    categoryPks = categoryPks,
                    name = "product$productPk",
                    description = "product$productPk",
                    price = 10000,
                    stock = null,
                    status = ProductStatus.ON,
                    discount = null
                )
            ).also { it.pk = productPk }
        }
    }

    @Nested
    inner class FindProduct() {
        @Test
        fun `상품이 존재할 경우`() {
            val productPk = 1L
            val product = makeProduct(productPk, emptyList())
            val excepted = ProductRes(product)

            every { productRepository.findByPkAndActive(productPk, true) } returns Optional.of(product)

            val result = productService.findProduct(productPk)

            verify(exactly = 1) { productRepository.findByPkAndActive(productPk, true) }
            assertThat(result).isEqualTo(excepted)
        }

        @Test
        fun `상품이 없을 경우`() {
            val productPk = 1L

            every { productRepository.findByPkAndActive(productPk, true) } returns Optional.empty()

            val ex = assertThrows<ProductNotFoundException> { productService.findProduct(1L) }
            assertThat(ex).hasMessageContaining("productPk: $productPk")

            verify(exactly = 1) { productRepository.findByPkAndActive(productPk, true) }
        }

        fun makeProduct(productPk: Long = 1L, categoryPks: List<Long>): ProductEntity {
            val seller = SellerEntity(
                name = "seller",
                password = "password",
                phoneNumber = "01000000001",
                email = "seller@google.com"
            ).also { it.pk = 1 }
            return ProductEntity(
                seller = seller,
                req = ProductReq(
                    categoryPks = categoryPks,
                    name = "product$productPk",
                    description = "product$productPk",
                    price = 10000,
                    stock = null,
                    status = ProductStatus.ON,
                    discount = null
                )
            ).also { it.pk = productPk }
        }

    }

    @Nested
    inner class UpdateProduct {
        @Test
        fun `상품이 없을 경우 에러 발생`() {
            val productPk = 1L

            every { productValidator.updateReqValidCheck(any()) } just Runs
            every { productRepository.findByPkAndActive(productPk, true) } returns Optional.empty()

            val ex = assertThrows<ProductNotFoundException> { productService.updateProduct(1L, productPk, baseReq()) }

            verify(exactly = 1) { productRepository.findByPkAndActive(productPk, true) }
            assertThat(ex).hasMessageContaining("productPk: $productPk")
        }

        @Test
        fun `상품 판매자가 아닐 경우 에러 발생`() {
            val productPk = 1L
            val seller = getSeller(pk = 1)
            val product = makeProduct(productPk = productPk, seller = seller)

            every { productValidator.updateReqValidCheck(any()) } just Runs
            every { productRepository.findByPkAndActive(productPk, true) } returns Optional.of(product)

            val ex = assertThrows<ResponseStatusException> {
                productService.updateProduct(seller.pk + 1, productPk, baseReq())
            }

            assertThat(ex.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        }

        @Nested
        @DisplayName("업데이트 성공")
        inner class UpdateSuccess {
            @Test
            fun `기본 필드 업데이트`() {
                val seller = getSeller(pk = 1)
                val product = makeProduct(productPk = 1L, seller = seller)
                val updateReq = baseReq().copy(name = "변경된 이름")

                every { productValidator.updateReqValidCheck(updateReq) } just runs
                every { productRepository.findByPkAndActive(product.pk, true) } returns Optional.of(product)
                every { productRepository.save(any()) } returnsArgument 0
                every { categoryRepository.findAllByPkInAndActive(emptyList(), true) } returns emptyList()

                productService.updateProduct(seller.pk, product.pk, updateReq)

                assertThat(product.name).isEqualTo(updateReq.name)
                verify(exactly = 2) { productRepository.save(any()) }
                verify(exactly = 1) { categoryRepository.findAllByPkInAndActive(any(), true) }
            }

            @Test
            fun `카테고리 변경`() {
                val seller = getSeller(pk = 1)
                val product = makeProduct(productPk = 1L, seller = seller)
                val updateReq = baseReq().copy(categoryPks = listOf(1L, 11L))

                every { productValidator.updateReqValidCheck(updateReq) } just runs
                every { productRepository.findByPkAndActive(product.pk, true) } returns Optional.of(product)
                every { productRepository.save(product) } returns product
                every { categoryRepository.findAllByPkInAndActive(listOf(11L), true) }
                    .returns(listOf(getCategory(11L)))

                productService.updateProduct(seller.pk, product.pk, updateReq)

                verify(exactly = 2) { productRepository.save(product) }
                verify(exactly = 1) { categoryRepository.findAllByPkInAndActive(listOf(11L), true) }

                assertThat(product.categoryProducts.map { it.category.pk }).containsExactlyInAnyOrder(1L, 11L)
            }
        }
    }

    @Nested
    inner class DeleteProduct {
        @Test
        fun `상품이 없을 경우 에러 발생`() {
            val productPk = 1L

            every { productValidator.updateReqValidCheck(any()) } just Runs
            every { productRepository.findByPkAndActive(productPk, true) } returns Optional.empty()

            val ex = assertThrows<ProductNotFoundException> { productService.deleteProduct(1L, productPk) }

            verify(exactly = 1) { productRepository.findByPkAndActive(productPk, true) }
            assertThat(ex).hasMessageContaining("productPk: $productPk")
        }

        @Test
        fun `판매자가 아닐 경우 에러 발생`() {
            val productPk = 1L
            val sellerPk = 1L
            val product = mockk<ProductEntity>()

            every { product.seller.pk } returns sellerPk
            every { productRepository.findByPkAndActive(productPk, true) } returns Optional.of(product)

            val ex = assertThrows<ResponseStatusException> { productService.deleteProduct(sellerPk + 1, productPk) }

            assertThat(ex.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        }

        @Test
        fun `삭제 성공`() {
            val sellerPk = 1L
            val productPk = 1L
            val product = makeProduct(productPk, getSeller(sellerPk))

            every { productRepository.findByPkAndActive(productPk, true) } returns Optional.of(product)
            every { productRepository.save(product) } returns product

            productService.deleteProduct(sellerPk, productPk)

            verify(exactly = 1) { productRepository.save(product) }
            assertThat(product.active).isFalse
        }
    }

    private fun baseReq(name: String = "테스트 상품"): ProductReq = ProductReq(
        categoryPks = listOf(1L, 2L),
        name = name,
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

    private fun getCategory(pk: Long = 1, name: String = ""): CategoryEntity = CategoryEntity(
        categoryName = name,
        categoryOrder = pk.toInt(),
        topCategory = null,
    ).also { it.pk = pk }

    private fun makeProduct(productPk: Long, seller: SellerEntity, req: ProductReq = baseReq()): ProductEntity {
        return ProductEntity(seller, req).also {
            it.pk = productPk

            it.categoryProducts.addAll(
                req.categoryPks.map { categoryPk ->
                    CategoryProductEntity(
                        category = getCategory(categoryPk),
                        product = it
                    )
                }
            )
        }
    }
}