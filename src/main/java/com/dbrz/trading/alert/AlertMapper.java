package com.dbrz.trading.alert;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
interface AlertMapper {

    AlertDto alertToAlertDto(Alert alert);

    List<AlertDto> alertsToAlertDtos(List<Alert> alerts);

    Alert alertDtoToAlert(AlertDto alertDto);
}
