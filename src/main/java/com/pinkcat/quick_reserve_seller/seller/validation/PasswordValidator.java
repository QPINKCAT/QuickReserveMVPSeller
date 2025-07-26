package com.pinkcat.quick_reserve_seller.seller.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<PasswordValid, String> {
  private static final Pattern PATTERN = Pattern.compile(SellerValidationPatterns.PASSWORD);

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isBlank()) return true;
    return PATTERN.matcher(value).matches();
  }
}
