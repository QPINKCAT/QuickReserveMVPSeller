package com.pinkcat.quick_reserve_seller.hotDeal.log;

import com.pinkcat.quick_reserve_seller.admin.entity.AdminEntity;
import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealEntity;
import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealProductRequestStatus;
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity;
import com.pinkcat.quick_reserve_seller.seller.entity.SellerEntity;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HotDealProductRequestLogCommand {
    @NotNull
    private final ProductEntity product;
    @NotNull
    private final HotDealEntity hotDeal;
    @NotNull
    private final HotDealProductRequestStatus status;
    private final SellerEntity seller;
    private final AdminEntity admin;
    private final String reason;

    public static HotDealProductRequestLogCommand of(
            ProductEntity product,
            HotDealEntity hotDeal,
            HotDealProductRequestStatus status,
            @Nullable SellerEntity seller,
            @Nullable AdminEntity admin,
            @Nullable String reason
    ) {
        return HotDealProductRequestLogCommand.builder()
                .product(product)
                .hotDeal(hotDeal)
                .status(status)
                .seller(seller)
                .admin(admin)
                .reason(reason)
                .build();
    }
}
