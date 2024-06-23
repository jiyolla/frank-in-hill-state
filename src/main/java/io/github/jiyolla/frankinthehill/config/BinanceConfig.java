package io.github.jiyolla.frankinthehill.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Getter
@Component
public class BinanceConfig {
    @Value("${binance.api.key}")
    private String apiKey;

    @Value("${binance.api.secret}")
    private String apiSecret;
}
