package io.github.jiyolla.frankinhillstate.backtesting.simple.query;

/**
 * {@link io.github.jiyolla.frankinhillstate.backtesting.simple.command.GuaranteedSuccessMarketCommand}와 연동되어야 함
 */
public class MyWalletQuery extends io.github.jiyolla.frankinhillstate.query.MyWalletQuery {

    public MyWalletQuery() {
        super(null);
    }

    @Override
    public String getAssets() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public String getTrades(String symbol) {
        throw new UnsupportedOperationException("not implemented");
    }
}
