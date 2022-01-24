package com.dbrz.trading.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Clock;

@Configuration
@EnableScheduling
class TradingToolsConfig {

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
