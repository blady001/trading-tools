package com.dbrz.trading.infrastructure.db;

import com.dbrz.trading.exchange.Timeframe;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
class TimeframeConverter implements AttributeConverter<Timeframe, String> {

    @Override
    public String convertToDatabaseColumn(Timeframe timeframe) {
        return timeframe == null ? null : timeframe.getSymbol();
    }

    @Override
    public Timeframe convertToEntityAttribute(String s) {
        return s == null ? null : Timeframe.from(s);
    }
}
