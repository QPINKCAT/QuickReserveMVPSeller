package com.pinkcat.quick_reserve_seller.hotDeal.model

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity
import jakarta.persistence.*
import lombok.Data

@Entity
@Data
@Table(
    name = "hot_deal_product_reqeust",
    uniqueConstraints = [
        UniqueConstraint(name = "UK_hotDeal_product", columnNames = ["hot_deal_pk", "product_pk"])
    ]
)
class HotDealProductRequestEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    val hotDeal: HotDealEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    val product: ProductEntity,
) : BaseEntity()