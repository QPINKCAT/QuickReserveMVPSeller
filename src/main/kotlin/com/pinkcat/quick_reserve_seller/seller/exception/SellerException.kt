package com.pinkcat.quick_reserve_seller.seller.exception

import com.pinkcat.quick_reserve_seller.common.exceptions.ErrorMessageCode
import com.pinkcat.quick_reserve_seller.common.exceptions.PinkCatException

open class SellerException(message: String, errorMessageCode: ErrorMessageCode) :
    PinkCatException(message, errorMessageCode)

class SellerNotFoundException(
    message: String,
    errorMessageCode: ErrorMessageCode = ErrorMessageCode.SELLER_NOT_FOUND_EXCEPTION
) : SellerException(message, errorMessageCode)