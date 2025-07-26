package com.pinkcat.quick_reserve_seller.seller.dto;

import com.pinkcat.quick_reserve_seller.seller.validation.PasswordValid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerUpdatePasswordRequestDto {
    @NotBlank
    private String password;

    @NotBlank
    @PasswordValid
    private String newPassword;
}
