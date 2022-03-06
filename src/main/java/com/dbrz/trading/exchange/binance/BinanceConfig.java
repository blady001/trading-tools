package com.dbrz.trading.exchange.binance;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BinanceConfig {

    @Bean
    BinanceApiClientFactory binanceApiClientFactory() {
        return BinanceApiClientFactory.newInstance();
    }

    @Bean
    BinanceApiRestClient binanceApiRestClient(BinanceApiClientFactory binanceApiClientFactory) {
        return binanceApiClientFactory.newRestClient();
    }
}
