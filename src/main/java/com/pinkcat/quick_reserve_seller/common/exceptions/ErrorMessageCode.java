package com.pinkcat.quick_reserve_seller.common.exceptions;

public enum ErrorMessageCode {

    SUCCESS(20000, "success"),
    ERROR(50000, "error"),

    // Category Exception - 60000
    CATEGORY_NOT_FOUND_EXCEPTION(60001, "category_not_found_exception"),
    CATEGORY_NOT_TOP_CATEGORY_EXCEPTION(60002, "category_not_top_category_exception"),

    // Product Exception - 70000
    PRODUCT_NOT_FOUND_EXCEPTION(70001, "product_not_found_exception"),
    PRODUCT_REQUEST_INVALID_EXCEPTION(70002, "product_request_invalid_exception"),

    // Seller Exception - 80000
    SELLER_NOT_FOUND_EXCEPTION(80001, "seller_not_found_exception"),
    SELLER_INACTIVE_EXCEPTION(80002, "seller inactive exception"),
    SELLER_INVALID_PASSWORD_EXCEPTION(80003, "seller invalid password"),

    // Product Order Exception - 90000
    PRODUCT_ORDER_ITEM_NOT_FOUND_EXCEPTION(90001, "product_order_item_not_found_exception"),
    PRODUCT_ORDER_ITEM_STATUS_UPDATE_REQ_INVALID(90002, "product_order_item_status_update_req_invalid"),

    // HotDeal Exception - 100000
    HOTDEAL_NOT_FOUND_EXCEPTION(100001, "hotdeal not found exception"),
    HOTDEAL_ACCESS_DENIED_EXCEPTION(100002, "hotdeal access denied exception"),

    // HotDeal Product Request Exception - 110000
    HOTDEAL_PRODUCT_REQUEST_ALREADY_EXISTS_EXCEPTION(110001, "hotdeal product request exists exception"),
    HOTDEAL_PRODUCT_REQUEST_NOT_FOUND_EXCEPTION(110002, "hotdeal product request not found exception");


    private final int codeValue;
    private final String message;

    ErrorMessageCode(int codeValue, String message) {
        this.codeValue = codeValue;
        this.message = message;
    }

    public int getCode() {
        return codeValue;
    }

    public String printMessage() {
        return message;
    }
}