package com.dbrz.trading.alert;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface AlertMapper {

    AlertDto alertToAlertDto(Alert alert);
}
