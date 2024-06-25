package io.github.jiyolla.frankinthehill.command;

import com.binance.connector.client.impl.SpotClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class MarketCommand {
    private final SpotClientImpl spotClientImpl;

    // TODO - [24-06-25][frank.burger]: 구매 방식이 매우 많음
    // https://binance-docs.github.io/apidocs/spot/en/#new-order-trade
    public String placeBuyOrder(String symbol, BigDecimal quantity, BigDecimal price) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("symbol", symbol);
        parameters.put("side", "BUY");
        parameters.put("type", "LIMIT");
        parameters.put("timeInForce", "FOK");
        parameters.put("quantity", quantity);
        parameters.put("price", price);
        return spotClientImpl.createTrade().newOrder(parameters);
    }

    public String placeSellOrder(String symbol, BigDecimal quantity, BigDecimal price) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("symbol", symbol);
        parameters.put("side", "SELL");
        parameters.put("type", "LIMIT");
        parameters.put("timeInForce", "FOK");
        parameters.put("quantity", quantity);
        parameters.put("price", price);
        return spotClientImpl.createTrade().newOrder(parameters);
    }
}