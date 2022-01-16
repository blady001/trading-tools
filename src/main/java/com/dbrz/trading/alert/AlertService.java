package com.dbrz.trading.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class AlertService {

    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;

    public List<AlertDto> getAlerts() {
        return alertRepository.findAll().stream().map(alertMapper::alertToAlertDto).toList();
    }
}
