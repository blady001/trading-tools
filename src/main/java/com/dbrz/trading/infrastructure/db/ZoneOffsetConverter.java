package com.dbrz.trading.infrastructure.db;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.ZoneOffset;

@Converter(autoApply = true)
class ZoneOffsetConverter implements AttributeConverter<ZoneOffset, String> {

    @Override
    public String convertToDatabaseColumn(ZoneOffset zoneOffset) {
        return zoneOffset == null ? null : zoneOffset.getId();
    }

    @Override
    public ZoneOffset convertToEntityAttribute(String s) {
        return s == null ? null : ZoneOffset.of(s);
    }
}
