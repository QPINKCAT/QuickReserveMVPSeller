package com.pinkcat.quick_reserve_seller.product.entity

import com.pinkcat.quick_reserve_seller.categoryProduct.entity.CategoryProductEntity
import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import com.pinkcat.quick_reserve_seller.discount.entity.DiscountEntity
import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealProductEntity
import com.pinkcat.quick_reserve_seller.product.dto.ProductReq
import com.pinkcat.quick_reserve_seller.seller.entity.SellerEntity
import jakarta.persistence.*
import lombok.Data
import org.hibernate.annotations.BatchSize

@Entity
@Data
@Table(name = "product")
@AttributeOverride(name = "pk", column = Column(name = "product_pk"))
class ProductEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val seller: SellerEntity,
    @Column(length = 30, name = "product_name")
    var name: String,
    @Column(columnDefinition = "text", name = "product_description")
    var description: String,
    @Column(name = "product_price")
    var price: Int,
    @Column(name = "product_stock")
    var stock: Int?,
    @Column(name = "product_avg_rating")
    val avgRating: Double,
    @Column(name = "product_review_count")
    val reviewCount: Int,
    @Enumerated(EnumType.STRING)
    @Column(length = 30, name = "product_status")
    var status: ProductStatus,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var discount: DiscountEntity?,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    @BatchSize(size = 10)
    val categoryProducts: MutableList<CategoryProductEntity>,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("display_oder asc")
    @BatchSize(size = 10)
    val images: MutableList<ProductImageEntity>,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val hotDealProducts: MutableList<HotDealProductEntity>,
) : BaseEntity() {
    constructor(seller: SellerEntity, req: ProductReq) : this(
        seller = seller,
        name = req.name,
        description = req.description,
        price = req.price,
        stock = req.stock,
        avgRating = 0.0,
        reviewCount = 0,
        status = req.status,
        discount = null,
        categoryProducts = mutableListOf(),
        images = mutableListOf(),
        hotDealProducts = mutableListOf(),
    )

    fun update(req: ProductReq) {
        this.name = req.name
        this.description = req.description
        this.price = req.price
        this.stock = req.stock
        this.status = req.status

        if (req.discount != null) {
            this.discount?.update(req.discount) ?: run {
                this.discount = DiscountEntity(this, req.discount)
            }
        } else this.discount = null
    }

    fun delete(): ProductEntity {
        this.active = false

        return this
    }
}

enum class ProductStatus {
    ON, OFF;
}