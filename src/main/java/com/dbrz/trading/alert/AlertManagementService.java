package com.dbrz.trading.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class AlertManagementService {

    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;

    List<AlertDto> getAlerts() {
        return alertMapper.alertsToAlertDtos(alertRepository.findAll());
    }

    AlertDto createAlert(AlertDto alertDto) {
        var alert = alertMapper.alertDtoToAlert(alertDto);
        alertRepository.save(alert);
        return alertMapper.alertToAlertDto(alert);
    }

    void deleteAlert(Long id) {
        alertRepository.deleteById(id);
    }
}
