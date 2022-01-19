package com.dbrz.trading.exchange;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ExchangeProvider {
    
    private final Map<Exchange, ExchangeAdapter> exchangeAdapterMap;

    public ExchangeAdapter getExchangeAdapter(Exchange exchange) {
        return exchangeAdapterMap.get(exchange);
    }
}
