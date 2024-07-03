package io.github.jiyolla.frankinhillstate.backtesting.simple;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Wallet {
    private final Map<String, BigDecimal> assets = new HashMap<>();
    private final Map<String, List<Trade>> trades = new HashMap<>();

    public void addTrade(Trade trade) {
        trades.getOrDefault(trade.symbol(), new ArrayList<>()).add(trade);
    }
}
