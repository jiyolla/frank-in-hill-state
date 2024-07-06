package io.github.jiyolla.frankinhillstate.backtesting.simple.command;

import io.github.jiyolla.frankinhillstate.backtesting.simple.Wallet;
import io.github.jiyolla.frankinhillstate.command.MarketCommand;

import java.math.BigDecimal;

/**
 * buy and sell order always success regardless of the market condition
 * and does not affect the market
 * but do update the account balance(asset)
 */
public class GuaranteedSuccessMarketCommand extends MarketCommand {
    private final Wallet wallet;

    public GuaranteedSuccessMarketCommand(Wallet wallet) {
        super(null);
        this.wallet = wallet;
    }

    @Override
    public String buy(String symbol, BigDecimal quantity, BigDecimal price) {
        wallet.buy(symbol, price, quantity);
        return "success";
    }

    @Override
    public String sell(String symbol, BigDecimal quantity, BigDecimal price) {
        wallet.sell(symbol, price, quantity);
        return "success";
    }
}
