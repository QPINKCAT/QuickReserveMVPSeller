package com.pinkcat.quick_reserve_seller.hotDeal.model

import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table
import lombok.Data

@Entity
@Data
@Table(name = "hot_deal")
class HotDealEntity(
    var name: String,
    var description: String,
    var thumbnail: String,
    var startAt: Long,
    var endAt: Long
) : BaseEntity()