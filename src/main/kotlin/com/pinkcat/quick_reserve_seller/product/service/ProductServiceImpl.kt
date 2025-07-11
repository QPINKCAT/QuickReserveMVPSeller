package com.pinkcat.quick_reserve_seller.product.service

import com.pinkcat.quick_reserve_seller.category.repository.CategoryRepository
import com.pinkcat.quick_reserve_seller.categoryProduct.entity.CategoryProductEntity
import com.pinkcat.quick_reserve_seller.categoryProduct.repository.CategoryProductRepository
import com.pinkcat.quick_reserve_seller.common.aws.AwsUtil
import com.pinkcat.quick_reserve_seller.common.exceptions.PinkCatErrorFactory.forbidden
import com.pinkcat.quick_reserve_seller.discount.entity.DiscountEntity
import com.pinkcat.quick_reserve_seller.discount.repository.DiscountRepository
import com.pinkcat.quick_reserve_seller.product.dto.PresignedUrlReq
import com.pinkcat.quick_reserve_seller.product.dto.ProductListRes
import com.pinkcat.quick_reserve_seller.product.dto.ProductReq
import com.pinkcat.quick_reserve_seller.product.dto.ProductRes
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity
import com.pinkcat.quick_reserve_seller.product.exception.ProductNotFoundException
import com.pinkcat.quick_reserve_seller.product.repository.ProductRepository
import com.pinkcat.quick_reserve_seller.product.validation.ProductValidator
import com.pinkcat.quick_reserve_seller.seller.exception.SellerNotFoundException
import com.pinkcat.quick_reserve_seller.seller.repository.SellerRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val categoryProductRepository: CategoryProductRepository,
    private val discountRepository: DiscountRepository,
    private val productRepository: ProductRepository,
    private val sellerRepository: SellerRepository,

    private val productValidator: ProductValidator,

    private val awsUtil: AwsUtil
) : ProductService {
    @Transactional
    override fun createProduct(sellerPk: Long, req: ProductReq) {
        productValidator.createReqValidCheck(req)

        val seller = sellerRepository.findByPkAndActive(sellerPk, true)
            .orElseThrow { SellerNotFoundException("]-----] ProductServiceImpl::createProduct Seller Not Found(sellerPk: $sellerPk) [-----[") }

        val categories = categoryRepository.findAllByPkInAndActive(req.categoryPks, true)

        val product = productRepository.save(ProductEntity(seller = seller, req = req))

        if (req.discount != null) {
            discountRepository.save(
                DiscountEntity(
                    product = product,
                    discountPrice = req.discount.price,
                    startAt = req.discount.startAt,
                    endAt = req.discount.endAt,
                )
            )
        }

        categories.map { category ->
            categoryProductRepository.save(
                CategoryProductEntity(
                    category = category,
                    product = product
                )
            )
        }.also { product.categoryProducts.addAll(it) }
    }

    @Transactional
    override fun findProductList(
        categoryPk: Long?,
        page: Int,
        size: Int
    ): Page<ProductListRes> {
        val categoryPks = if (categoryPk != null) {
            categoryRepository.findAllChildrenPkRecursive(categoryPk) + categoryPk
        } else emptyList()
        val pageable = PageRequest.of(page, size)

        return productRepository
            .findAllByCategoryPk(categoryPks, true, pageable)
            .map {
                ProductListRes(it)
            }
    }

    @Transactional
    override fun findProduct(productPk: Long): ProductRes {
        return productRepository.findByPkAndActive(productPk, true)
            .orElseThrow { ProductNotFoundException("]-----] ProductServiceImpl::findProduct Product Not Found(productPk: $productPk) [-----[") }
            .let { ProductRes(it) }
    }

    @Transactional
    override fun updateProduct(sellerPk: Long, productPk: Long, req: ProductReq): Boolean {
        productValidator.updateReqValidCheck(req)

        val product = productRepository.findByPkAndActive(productPk, true)
            .orElseThrow { ProductNotFoundException("]-----] ProductServiceImpl::findProduct Product Not Found(productPk: $productPk) [-----[") }

        if (product.seller.pk != sellerPk)
            throw forbidden()

        product.update(req)

        productRepository.save(product)

        product.categoryProducts.removeAll { categoryProduct -> categoryProduct.category.pk !in req.categoryPks }

        val categoryProductPks = product.categoryProducts.map { it.category.pk!! }

        req.categoryPks.filter { it !in categoryProductPks }.let { addCategoryPks ->
            val categoryProducts = categoryRepository.findAllByPkInAndActive(addCategoryPks, true)
                .map { category ->
                    CategoryProductEntity(category = category, product = product)
                }

            product.categoryProducts.addAll(categoryProducts)
        }

        productRepository.save(product)

        return true
    }

    @Transactional
    override fun deleteProduct(sellerPk: Long, productPk: Long): Boolean {
        val product = productRepository.findByPkAndActive(sellerPk, true)
            .orElseThrow { ProductNotFoundException("]-----] ProductServiceImpl::findProduct Product Not Found(productPk: $productPk) [-----[") }

        if (product.seller.pk != sellerPk)
            throw forbidden()

        productRepository.save(product.delete())

        return true
    }

    override fun getPresignedUrl(req: PresignedUrlReq): String {
        return awsUtil.generateUploadUrl(req.name, req.contentType).path
    }
}