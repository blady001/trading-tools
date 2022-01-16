package com.dbrz.trading.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    AlertDto createAlert(@NotNull @Valid @RequestBody AlertDto alertDto) {
        return alertService.createAlert(alertDto);
    }
}
