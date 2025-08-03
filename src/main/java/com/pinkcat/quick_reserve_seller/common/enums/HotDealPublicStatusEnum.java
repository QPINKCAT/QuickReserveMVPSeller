package com.pinkcat.quick_reserve_seller.common.enums;

public enum HotDealPublicStatusEnum {
    ADMIN_ONLY("관리자만"),
    ADMIN_SELLER("관리자와판매자만"),
    ALL("전체");

    private final String value;

    HotDealPublicStatusEnum(String value){
        this.value = value;
    }
}