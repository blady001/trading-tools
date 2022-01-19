package com.dbrz.trading.exchange;

import com.dbrz.trading.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ExchangeProviderFunctionalTest extends TestBase {

    @Autowired
    private ExchangeProvider exchangeProvider;

    @Test
    void shouldReturnExchange() {
        for (Exchange exchange : Exchange.values()) {
            assertThat(exchangeProvider.getExchangeAdapter(exchange)).isNotNull();
        }
    }
}
