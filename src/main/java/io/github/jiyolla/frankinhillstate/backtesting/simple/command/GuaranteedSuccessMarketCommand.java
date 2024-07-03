package io.github.jiyolla.frankinhillstate.backtesting.simple.command;

import io.github.jiyolla.frankinhillstate.backtesting.simple.Wallet;
import io.github.jiyolla.frankinhillstate.command.MarketCommand;

import java.math.BigDecimal;

import static io.github.jiyolla.frankinhillstate.backtesting.simple.Trade.buy;
import static io.github.jiyolla.frankinhillstate.backtesting.simple.Trade.sell;

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
    public String placeBuyOrder(String symbol, BigDecimal quantity, BigDecimal price) {
        wallet.addTrade(buy(symbol, price, quantity));
        return "success";
    }

    @Override
    public String placeSellOrder(String symbol, BigDecimal quantity, BigDecimal price) {
        wallet.addTrade(sell(symbol, price, quantity));
        return "success";
    }
}
