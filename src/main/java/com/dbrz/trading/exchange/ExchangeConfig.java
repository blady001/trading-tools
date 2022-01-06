package com.dbrz.trading.exchange;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class ExchangeConfig {

    private ExchangeConfig exchangeConfig;

    @Bean
    BinanceApiRestClient binanceApiRestClient() {
        return binanceApiClientFactory().newRestClient();
    }

    @Bean
    BinanceApiClientFactory binanceApiClientFactory() {
        return BinanceApiClientFactory.newInstance();
    }
}
