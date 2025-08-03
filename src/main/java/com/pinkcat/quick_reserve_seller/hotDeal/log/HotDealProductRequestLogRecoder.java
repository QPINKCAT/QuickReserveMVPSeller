package com.pinkcat.quick_reserve_seller.hotDeal.log;

import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealProductRequestLogEntity;
import com.pinkcat.quick_reserve_seller.hotDeal.respository.HotDealProductRequestLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HotDealProductRequestLogRecoder {
    private final HotDealProductRequestLogRepository hotDealProductRequestLogRepository;

    public void record(HotDealProductRequestLogCommand cmd) {
        HotDealProductRequestLogEntity entity = new HotDealProductRequestLogEntity(
                cmd.getSeller(),
                cmd.getAdmin(),
                cmd.getHotDeal(),
                cmd.getProduct(),
                cmd.getStatus(),
                cmd.getReason()
        );
        hotDealProductRequestLogRepository.save(entity);
    }
}
