package io.github.jiyolla.frankinhillstate.backtesting.simple.command;

import io.github.jiyolla.frankinhillstate.command.MarketCommand;

import java.math.BigDecimal;

/**
 * buy and sell order always success regardless of the market condition
 * and does not affect the market
 * but do update the account balance(asset)
 */
public class GuaranteedSuccessMarketCommand extends MarketCommand {
    public GuaranteedSuccessMarketCommand() {
        super(null);
    }

    @Override
    public String placeBuyOrder(String symbol, BigDecimal quantity, BigDecimal price) {
        // TODO - [24-06-26][frank.burger]: update account balance
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public String placeSellOrder(String symbol, BigDecimal quantity, BigDecimal price) {
        // TODO - [24-06-26][frank.burger]: update account balance
        throw new UnsupportedOperationException("not implemented");
    }
}
