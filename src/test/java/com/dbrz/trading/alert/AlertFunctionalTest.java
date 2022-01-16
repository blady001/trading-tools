package com.dbrz.trading.alert;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(AlertHelper.class)
class AlertFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlertHelper alertHelper;

    @Test
    public void shouldReturnAlerts() throws Exception {
        var givenAlertEntity = alertHelper.givenAlertEntity();
        var givenExpectedContent = String.format("""
                        [
                            {
                                "id": %d,
                                "symbol": "%s",
                                "exchange": "%s",
                                "exchangeTimeOffset": "%s",
                                "timeframe": "%s",
                                "trigger": "%s",
                                "notificationMethod": %d,
                                "isActive": %s
                            }
                        ]
                        """,
                givenAlertEntity.getId(),
                givenAlertEntity.getSymbol(),
                givenAlertEntity.getExchange(),
                givenAlertEntity.getExchangeTimeOffset().getId(),
                givenAlertEntity.getTimeframe().toString(),
                givenAlertEntity.getTrigger(),
                givenAlertEntity.getNotificationMethod(),
                givenAlertEntity.getIsActive()).trim();

        mockMvc.perform(get("/alerts"))
                .andExpect(status().isOk())
                .andExpect(content().json(givenExpectedContent));
    }
}
