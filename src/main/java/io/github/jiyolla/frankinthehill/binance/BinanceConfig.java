package io.github.jiyolla.frankinthehill.binance;

import com.binance.connector.client.impl.SpotClientImpl;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Getter
@Configuration
public class BinanceConfig {
    @Value("${binance.api.key}")
    private String apiKey;

    @Value("${binance.api.secret}")
    private String apiSecret;

    @Bean
    public SpotClientImpl spotClientImpl() {
        return new SpotClientImpl(apiKey, apiSecret);
    }
}
