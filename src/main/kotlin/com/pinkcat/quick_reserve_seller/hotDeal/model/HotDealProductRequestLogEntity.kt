package com.pinkcat.quick_reserve_seller.hotDeal.model

import com.pinkcat.quick_reserve_seller.admin.entity.AdminEntity
import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity
import com.pinkcat.quick_reserve_seller.seller.entity.SellerEntity
import jakarta.persistence.*
import lombok.Data

@Entity
@Data
@Table(
        name = "hot_deal_product_reqeust_log",
)
class HotDealProductRequestLogEntity(
        @ManyToOne(fetch = FetchType.LAZY)
        val seller: SellerEntity?,
        @ManyToOne(fetch = FetchType.LAZY)
        val admin: AdminEntity?,
        @ManyToOne(fetch = FetchType.LAZY)
        val hotDeal: HotDealEntity,
        @ManyToOne(fetch = FetchType.LAZY)
        val product: ProductEntity,
        @Enumerated(EnumType.STRING)
        @Column(length = 20)
        var hotDealProductRequestStatus: HotDealProductRequestStatus,
        @Column(length = 100)
        var reason: String? = null
) : BaseEntity()

enum class HotDealProductRequestStatus {
    REQUESTED,
    APPROVED,
    REJECTED,
    CANCELLED
}