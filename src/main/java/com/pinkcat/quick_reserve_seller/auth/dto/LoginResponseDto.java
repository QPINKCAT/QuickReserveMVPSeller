package com.pinkcat.quick_reserve_seller.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponseDto {
  private String accessToken;
}
