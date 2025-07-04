package com.pinkcat.quick_reserve_seller.product.exception

import com.pinkcat.quick_reserve_seller.common.exceptions.ErrorMessageCode
import com.pinkcat.quick_reserve_seller.common.exceptions.PinkCatException

open class ProductException(message: String, errorMessageCode: ErrorMessageCode) :
    PinkCatException(message, errorMessageCode)

class ProductNotFoundException(
    message: String,
    errorMessageCode: ErrorMessageCode = ErrorMessageCode.PRODUCT_NOT_FOUND_EXCEPTION
) : ProductException(message, errorMessageCode)

class ProductReqInvalidException(
    message: String,
    errorMessageCode: ErrorMessageCode = ErrorMessageCode.PRODUCT_REQUEST_INVALID_EXCEPTION
) : ProductException(message, errorMessageCode)