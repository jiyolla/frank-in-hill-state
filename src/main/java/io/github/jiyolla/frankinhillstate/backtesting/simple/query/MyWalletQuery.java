package io.github.jiyolla.frankinhillstate.backtesting.simple.query;

import io.github.jiyolla.frankinhillstate.backtesting.simple.Wallet;

/**
 * {@link io.github.jiyolla.frankinhillstate.backtesting.simple.command.GuaranteedSuccessMarketCommand}와 연동되어야 함
 */
public class MyWalletQuery extends io.github.jiyolla.frankinhillstate.query.MyWalletQuery {
    private final Wallet wallet;

    public MyWalletQuery(Wallet wallet) {
        super(null);
        this.wallet = wallet;
    }

    @Override
    public String getAssets() {
        return wallet.getAssets().toString();
    }

    @Override
    public String getTrades(String symbol) {
        return wallet.getTrades().toString();
    }
}
