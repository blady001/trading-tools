package com.dbrz.trading.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
class AlertController {

    private final AlertService alertService;

    @GetMapping
    List<AlertDto> getAlerts() {
        return alertService.getAlerts();
    }
}
