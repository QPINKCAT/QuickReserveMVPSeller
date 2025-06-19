package com.pinkcat.quick_reserve_seller.common.model;

import lombok.Data;

@Data
public class BaseView {
    Long pk;

    Long createdAt;
    Long updatedAt;
    Boolean active;
}