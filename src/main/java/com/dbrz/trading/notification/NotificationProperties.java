package com.dbrz.trading.notification;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "notification")
@Data
class NotificationProperties {

    @NotNull
    private PushoverProperties pushoverProperties;

    @Data
    static class PushoverProperties {

        @NotBlank
        private String apiUrl;

        @NotBlank
        private String apiKey;

        @NotBlank
        private String userKey;
    }
}
