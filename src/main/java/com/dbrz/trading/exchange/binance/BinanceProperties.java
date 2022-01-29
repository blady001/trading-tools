package com.dbrz.trading.exchange.binance;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "exchange.binance")
@Configuration
@Data
class BinanceProperties {

    @NotBlank
    private String apiKey;

    @NotBlank
    private String secret;
}
