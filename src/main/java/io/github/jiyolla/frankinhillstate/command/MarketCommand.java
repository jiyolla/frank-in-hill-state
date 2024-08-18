package io.github.jiyolla.frankinhillstate.command;

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
    //  order를 만드는 것과 order가 성사되는 것을 구분할 수 있는데, 복잡해져서 FOK로 반드시 성공하는 order만 일단 사용
    // https://binance-docs.github.io/apidocs/spot/en/#new-order-trade
    public String buy(String symbol, BigDecimal quantity, BigDecimal price) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("symbol", symbol);
        parameters.put("quantity", quantity);
        parameters.put("side", "BUY");
        parameters.put("type", "MARKET");
        // parameters.put("type", "LIMIT");
        // parameters.put("timeInForce", "FOK");
        // parameters.put("price", price);
        return spotClientImpl.createTrade().newOrder(parameters);
    }

    public String sell(String symbol, BigDecimal quantity, BigDecimal price) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("symbol", symbol);
        parameters.put("quantity", quantity);
        parameters.put("side", "SELL");
        parameters.put("type", "MARKET");
        // parameters.put("type", "LIMIT");
        // parameters.put("timeInForce", "FOK");
        // parameters.put("price", price);
        return spotClientImpl.createTrade().newOrder(parameters);
    }
}