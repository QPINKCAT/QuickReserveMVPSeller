package com.pinkcat.quick_reserve_seller.order.exception

import com.pinkcat.quick_reserve_seller.common.exceptions.ErrorMessageCode
import com.pinkcat.quick_reserve_seller.common.exceptions.PinkCatException

class ProductOrderException(message: String, errorMessageCode: ErrorMessageCode) :
    PinkCatException(message, errorMessageCode)

class ProductOrderItemNotFoundException(
    message: String,
    errorMessageCode: ErrorMessageCode = ErrorMessageCode.PRODUCT_ORDER_ITEM_NOT_FOUND_EXCEPTION
) : PinkCatException(message, errorMessageCode)

class ProductOrderItemStatusUpdateReqInvalid(
    message: String,
    errorMessageCode: ErrorMessageCode = ErrorMessageCode.PRODUCT_ORDER_ITEM_STATUS_UPDATE_REQ_INVALID
) : PinkCatException(message, errorMessageCode)