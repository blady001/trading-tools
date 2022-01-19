package com.dbrz.trading.alert;

import com.dbrz.trading.TestBase;
import com.dbrz.trading.analysis.Timeframe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AlertFunctionalTest extends TestBase {

    @Autowired
    private AlertHelper alertHelper;

    @Autowired
    private AlertMapper alertMapper;

    @Test
    void shouldReturnAlerts() throws Exception {
        var givenAlertEntity = alertHelper.givenAlertEntity();
        var givenExpectedContent = String.format("[%s]", givenJson(alertMapper.alertToAlertDto(givenAlertEntity)));
        mockMvc.perform(get("/alerts"))
                .andExpect(status().isOk())
                .andExpect(content().json(givenExpectedContent));
    }

    @Test
    void shouldCreateAlert() throws Exception {
        // given
        var givenDto = alertHelper.givenAlertDto(null);

        // when
        mockMvc.perform(post("/alerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson(givenDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        // then
        var actualAlerts = alertHelper.getAllSavedAlerts();
        assertThat(actualAlerts).hasSize(1);
    }

    @Test
    void shouldNotCreateAlertWithInvalidPayload() throws Exception {
        // given
        var givenDto = new AlertDto(null, "", null, Timeframe.HOUR, null, null, false);

        // when
        mockMvc.perform(post("/alerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson(givenDto)))
                .andExpect(status().isBadRequest());

        // then
        var actualAlerts = alertHelper.getAllSavedAlerts();
        assertThat(actualAlerts).isEmpty();
    }

    @Test
    void shouldDeleteAlert() throws Exception {
        // given
        var givenAlert = alertHelper.givenAlertEntity();

        // when
        mockMvc.perform(delete("/alerts/" + givenAlert.getId()))
                .andExpect(status().isOk());

        // then
        alertHelper.thenAlertDoesNotExist(givenAlert.getId());
    }

    private String givenJson(AlertDto alertDto) {
        return String.format("""
                        {
                            "id": %d,
                            "symbol": "%s",
                            "exchange": "%s",
                            "timeframe": "%s",
                            "trigger": "%s",
                            "notificationMethod": %d,
                            "isActive": %s
                        }
                        """,
                alertDto.id(),
                alertDto.symbol(),
                alertDto.exchange(),
                alertDto.timeframe().toString(),
                alertDto.trigger(),
                alertDto.notificationMethod(),
                alertDto.isActive());
    }
}
