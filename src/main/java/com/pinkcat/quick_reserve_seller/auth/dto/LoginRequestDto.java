package com.pinkcat.quick_reserve_seller.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
  @NotBlank private String userId;
  @NotBlank private String password;
}
