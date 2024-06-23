package io.github.jiyolla.frankinthehill.query;

import com.binance.connector.client.impl.SpotClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class Market implements Query {
    private final SpotClientImpl spotClientImpl;

    public String getCurrentPrice(String symbol) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("symbol", symbol);
        return spotClientImpl.createMarket().tickerSymbol(parameters);
    }

    public String getRSI(String symbol) {
        // TODO - [24-06-23][frank.burger]: Implement RSI calculation
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("symbol", symbol);
        parameters.put("interval", "1h");
        return spotClientImpl.createMarket().klines(parameters);
    }
}
