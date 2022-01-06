package com.dbrz.trading.exchange;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "exchange")
@Data
public class ExchangeProperties {

    @NotNull
    private BinanceProperties binance;

    @Data
    public static class BinanceProperties {

        @NotBlank
        private String apiKey;

        @NotBlank
        private String secret;
    }
}
