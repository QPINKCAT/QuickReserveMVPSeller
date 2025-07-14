package com.pinkcat.quick_reserve_seller.category.exception

import com.pinkcat.quick_reserve_seller.common.exceptions.ErrorMessageCode
import com.pinkcat.quick_reserve_seller.common.exceptions.PinkCatException

open class CategoryException(message: String, errorMessageCode: ErrorMessageCode) :
    PinkCatException(message, errorMessageCode)

class CategoryNotFoundException(
    message: String,
    errorMessageCode: ErrorMessageCode = ErrorMessageCode.CATEGORY_NOT_FOUND_EXCEPTION
) : CategoryException(message, errorMessageCode)

class CategoryNotTopCategoryException(
    message: String,
    errorMessageCode: ErrorMessageCode = ErrorMessageCode.CATEGORY_NOT_TOP_CATEGORY_EXCEPTION
) : CategoryException(message, errorMessageCode)