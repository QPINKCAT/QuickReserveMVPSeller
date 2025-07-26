package com.pinkcat.quick_reserve_seller.seller.dto;

import com.pinkcat.quick_reserve_seller.seller.entity.SellerEntity;
import com.pinkcat.quick_reserve_seller.store.entity.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SellerGetResponseDto {
  private Long sellerPk;
  private String sellerId;
  private String sellerName;
  private String sellerPhoneNumber;
  private String sellerEmail;

  private String storeName;
  private String storeAddress;
  private String storeContactNumber;

  public static SellerGetResponseDto fromEntity(SellerEntity seller) {
    StoreEntity store = seller.getStore();
    return SellerGetResponseDto.builder()
            .sellerPk(seller.getPk())
            .sellerId(seller.getId())
            .sellerName(seller.getName())
            .sellerPhoneNumber(seller.getPhoneNumber())
            .sellerEmail(seller.getEmail())
            .storeName(store.getName())
            .storeAddress(store.getAddress())
            .storeContactNumber(store.getContactNumber())
            .build();
  }
}