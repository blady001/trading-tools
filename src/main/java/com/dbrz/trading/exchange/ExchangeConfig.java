package com.dbrz.trading.exchange;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
class ExchangeConfig {

    private final ExchangeAdapter binanceAdapter;

    @Bean
    Map<Exchange, ExchangeAdapter> exchangeAdapterMap() {
        Map<Exchange, ExchangeAdapter> map = new HashMap<>();
        map.put(Exchange.BINANCE, binanceAdapter);
        return map;
    }
}
