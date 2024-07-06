package io.github.jiyolla.frankinhillstate.backtesting.simple;

import java.math.BigDecimal;

public record Trade(String type, String symbol, BigDecimal price, BigDecimal baseQuantity, BigDecimal quoteQuantity) {
    public static Trade buy(String symbol, BigDecimal price, BigDecimal quantity) {
        return new Trade("buy", symbol, price, quantity, price.multiply(quantity));
    }

    public static Trade sell(String symbol, BigDecimal price, BigDecimal quantity) {
        return new Trade("sell", symbol, price, quantity, price.multiply(quantity));
    }
}