package io.github.jiyolla.frankinthehill.query;

import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Trade;
import com.binance.connector.client.impl.spot.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class MyWalletQuery {
    private final SpotClientImpl spotClientImpl;

    public String getAssets() {
        Wallet wallet = spotClientImpl.createWallet();
        return wallet.getUserAsset(null);
    }

    public String getTrades(String symbol) {
        Trade trade = spotClientImpl.createTrade();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("symbol", symbol);
        return trade.myTrades(parameters);
    }
}
