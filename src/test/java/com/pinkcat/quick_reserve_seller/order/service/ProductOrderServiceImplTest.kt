package com.pinkcat.quick_reserve_seller.order.service

import com.pinkcat.quick_reserve_seller.customer.model.CustomerEntity
import com.pinkcat.quick_reserve_seller.order.dto.ProductOrderItemStatusUpdateReq
import com.pinkcat.quick_reserve_seller.order.exception.ProductOrderItemNotFoundException
import com.pinkcat.quick_reserve_seller.order.model.ProductOrderEntity
import com.pinkcat.quick_reserve_seller.order.model.ProductOrderItemEntity
import com.pinkcat.quick_reserve_seller.order.model.ProductOrderItemStatus
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.Instant

class ProductOrderServiceImplTest {
    private val productOrderItemRepositoryImpl = mockk<ProductOrderItemRepositoryImpl>()
    private val productOrderService = ProductOrderServiceImpl(productOrderItemRepositoryImpl)

    @Nested
    inner class FindProductOrderList {
        @Test
        fun `param 전달 검증`() {
            val productPk = null
            val status = emptyList<ProductOrderItemStatus>()
            val page = 0
            val size = 10

            every {
                productOrderItemRepositoryImpl.findALlByProductPkAndProductStatusInAndActive(
                    productPk = productPk,
                    productStatusList = status,
                    active = true,
                    page = page,
                    size = size
                )
            } returns Page.empty()

            productOrderService.findProductOrderList(
                productPk = productPk,
                status = status,
                page = page,
                size = size
            )

            verify(exactly = 1) {
                productOrderItemRepositoryImpl.findALlByProductPkAndProductStatusInAndActive(
                    productPk = productPk,
                    productStatusList = status,
                    active = true,
                    page = page,
                    size = size
                )
            }
        }

        @Test
        fun `DTO 매핑 검증`() {
            val productPk = 1L
            val status = emptyList<ProductOrderItemStatus>()
            val page = 0
            val size = 10
            val productOrderItems = getProductOrderItems()

            every {
                productOrderItemRepositoryImpl.findALlByProductPkAndProductStatusInAndActive(
                    productPk = productPk,
                    productStatusList = status,
                    active = true,
                    page = page,
                    size = size
                )
            } returns PageImpl(productOrderItems, PageRequest.of(page, size), productOrderItems.size.toLong())

            val result = productOrderService.findProductOrderList(
                productPk = productPk,
                status = status,
                page = page,
                size = size
            )

            assertThat(result.totalElements).isEqualTo(productOrderItems.size.toLong())
            assertThat(result.pageable.pageNumber).isEqualTo(page)
            assertThat(result.size).isEqualTo(size)

            productOrderItems.forEachIndexed { index, productOrderItem ->
                val dto = result.content[index]!!

                assertThat(dto.pk).isEqualTo(productOrderItem.pk)
                assertThat(dto.price).isEqualTo(productOrderItem.salePrice)
                assertThat(dto.quantity).isEqualTo(productOrderItem.quantity)
                assertThat(dto.status).isEqualTo(productOrderItem.status)
                assertThat(dto.orderAt).isEqualTo(productOrderItem.productOrder.orderAt)
                assertThat(dto.customer.name).isEqualTo(productOrderItem.productOrder.customer.name)
                assertThat(dto.customer.phoneNumber).isEqualTo(productOrderItem.productOrder.customer.phoneNumber)
            }
        }
    }

    @Nested
    inner class UpdateProductOrderItemStatus {
        @Test
        fun `pk에 해당하는 ProductOrderItem이 없을 경우 에러`() {
            val req = baseReq()

            every {
                productOrderItemRepositoryImpl.findAllByPkInAndActive(
                    pks = req.status.keys,
                    active = true
                )
            } returns emptyList()

            assertThrows<ProductOrderItemNotFoundException> {
                productOrderService.updateProductOrderItemStatus(req)
            }
        }

        @Test
        fun `Status Update 성공`() {
            val req = baseReq()
            val productOrderItems = getProductOrderItems(pks = req.status.keys)

            every {
                productOrderItemRepositoryImpl.findAllByPkInAndActive(req.status.keys, true)
            } returns productOrderItems
            every {
                productOrderItemRepositoryImpl.saveAll(any())
            } returnsArgument 0

            productOrderService.updateProductOrderItemStatus(req)

            verify(exactly = 1) { productOrderItemRepositoryImpl.findAllByPkInAndActive(req.status.keys, true) }
            verify(exactly = 1) { productOrderItemRepositoryImpl.saveAll(any()) }

            productOrderItems.forEachIndexed { index, productOrderItem ->
                assertThat(productOrderItem.status).isEqualTo(productOrderItem.status)
            }
        }
    }

    fun baseReq(pks: List<Long> = listOf(1, 2)) = ProductOrderItemStatusUpdateReq(
        status = pks.associateWith { ProductOrderItemStatus.entries.toTypedArray().random() }
    )

    fun getProductOrderItems(pks: Collection<Long> = listOf(1, 2)) = pks.map {
        getProductOrderItem(
            productPk = 1L,
            productOrderItemPk = it
        )
    }

    fun getProductOrderItem(
        productPk: Long = 1,
        productOrderItemPk: Long = 1,
        productOrderItemStatus: ProductOrderItemStatus = ProductOrderItemStatus.PENDING
    ): ProductOrderItemEntity {
        val product = mockk<ProductEntity>()
        val productOrder = mockk<ProductOrderEntity>()
        val customer = mockk<CustomerEntity>()

        every { product.pk } returns productPk
        every { productOrder.orderAt } returns Instant.now().toEpochMilli()
        every { productOrder.customer } returns customer
        every { customer.name } returns "고객"
        every { customer.phoneNumber } returns "01012345678"

        return ProductOrderItemEntity(
            product = product,
            productOrder = productOrder,
            originalPrice = 10000,
            salePrice = 9000,
            quantity = 1,
            status = productOrderItemStatus
        ).also { it.pk = productOrderItemPk }
    }
}