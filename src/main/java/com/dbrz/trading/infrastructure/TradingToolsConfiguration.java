package com.dbrz.trading.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class TradingToolsConfiguration {

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
