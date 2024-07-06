package io.github.jiyolla.frankinhillstate.backtesting.simple;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Wallet {
    // Currency -> Amount
    private final Map<String, BigDecimal> assets = new HashMap<>();
    private final Map<String, List<Trade>> trades = new HashMap<>();

    public void buy(String symbol, BigDecimal price, BigDecimal quantity) {
        String baseCurrency = symbol.split("/")[0];
        String quoteCurrency = symbol.split("/")[1];
        // TODO - [24-07-06][frank.burger]: 수수료 계산 포함시키기
        BigDecimal quoteQuantity = price.multiply(quantity);

        BigDecimal quoteCurrencyAfterBuy = assets.getOrDefault(quoteCurrency, BigDecimal.ZERO).subtract(quoteQuantity);
        if (quoteCurrencyAfterBuy.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Not enough quote currency");
        }

        assets.put(quoteCurrency, quoteCurrencyAfterBuy);
        assets.merge(baseCurrency, quantity, BigDecimal::add);
        trades.getOrDefault(symbol, new ArrayList<>()).add(Trade.buy(symbol, price, quantity));
    }

    public void sell(String symbol, BigDecimal price, BigDecimal quantity) {
        String baseCurrency = symbol.split("/")[0];
        String quoteCurrency = symbol.split("/")[1];
        // TODO - [24-07-06][frank.burger]: 수수료 계산 포함시키기
        BigDecimal quoteQuantity = price.multiply(quantity);

        BigDecimal baseCurrencyAfterSell = assets.getOrDefault(baseCurrency, BigDecimal.ZERO).subtract(quantity);
        if (baseCurrencyAfterSell.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Not enough base currency");
        }

        assets.put(baseCurrency, baseCurrencyAfterSell);
        assets.merge(quoteCurrency, quoteQuantity, BigDecimal::add);
        trades.getOrDefault(symbol, new ArrayList<>()).add(Trade.sell(symbol, price, quantity));
    }
}
