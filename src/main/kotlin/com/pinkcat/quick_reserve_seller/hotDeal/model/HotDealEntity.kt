package com.pinkcat.quick_reserve_seller.hotDeal.model

import com.pinkcat.quick_reserve_seller.common.enums.HotDealPublicStatusEnum
import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import lombok.Data
import java.time.LocalDateTime

@Entity
@Data
@Table(name = "hot_deal")
class HotDealEntity(
        var name: String,
        var description: String,
        var thumbnail: String,
        var startAt: LocalDateTime,
        var endAt: LocalDateTime,
        @Enumerated(EnumType.STRING)
        var publicStatus: HotDealPublicStatusEnum,
        @OneToMany(mappedBy = "hotDeal")
        var hotDealProducts: MutableList<HotDealProductEntity> = mutableListOf()
        ) : BaseEntity()