package com.pinkcat.quick_reserve_seller.customer.model

import com.pinkcat.quick_reserve_seller.common.enums.GenderEnum
import com.pinkcat.quick_reserve_seller.common.model.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import lombok.Data

@Entity
@Data
class CustomerEntity(
    @Column(name = "customer_id")
    var id: String,
    @Column(name = "customer_name")
    var name: String,
    @Column(name = "customer_password")
    var password: String,
    @Column(name = "customer_phone_number")
    var phoneNumber: String,
    @Column(name = "customer_email")
    var email: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_gender")
    var gender: GenderEnum
) : BaseEntity()