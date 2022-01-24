package com.dbrz.trading.infrastructure.db;

import com.dbrz.trading.exchange.Exchange;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
class ExchangeConverter implements AttributeConverter<Exchange, String> {

    @Override
    public String convertToDatabaseColumn(Exchange exchange) {
        return exchange == null ? null : exchange.name();
    }

    @Override
    public Exchange convertToEntityAttribute(String s) {
        return s == null ? null : Exchange.valueOf(s);
    }
}
