package io.github.jiyolla.frankinhillstate.backtesting.simple.query;

import com.binance.connector.client.impl.SpotClientImpl;

// TODO - [24-06-26][frank.burger]: 백테스팅에거 과거 데이터만 사용할 거라 캐싱 적용 가능할 듯 일단한 가장 단순한 재사용
public class MarketQuery extends io.github.jiyolla.frankinhillstate.query.MarketQuery {
    public MarketQuery(SpotClientImpl spotClientImpl) {
        super(spotClientImpl);
    }
}
