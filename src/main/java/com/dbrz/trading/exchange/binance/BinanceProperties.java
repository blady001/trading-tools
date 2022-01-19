package com.dbrz.trading.exchange.binance;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "exchange.binance")
@Data
class BinanceProperties {

    @NotBlank
    private String apiKey;

    @NotBlank
    private String secret;
}
