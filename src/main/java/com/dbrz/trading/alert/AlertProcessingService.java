package com.dbrz.trading.alert;

import com.dbrz.trading.exchange.TickEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class AlertProcessingService {

    private final AlertManagementService alertManagementService;

    @EventListener
    void process(TickEvent event) {
        log.info("Event received: {}", event);
        // get alerts for corresponding timeframe / exchange
        // for each alert -> notify user
    }
}
