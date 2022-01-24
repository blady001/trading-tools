package com.dbrz.trading.notification.console;

import com.dbrz.trading.notification.SendingStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConsoleNotificationAdapterTest {

    @Test
    void shouldReturnStatusSuccess() {
        // given
        var adapter = new ConsoleNotificationAdapter();

        // when
        var status = adapter.send(null);

        // then
        assertThat(status).isEqualTo(SendingStatus.SUCCESS);
    }
}
