package io.github.jiyolla.frankinhillstate.backtesting.ta4j;

import io.github.jiyolla.frankinhillstate.query.MarketQuery;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.backtest.BarSeriesManager;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

import java.time.LocalDateTime;


@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class Ta4jBacktesting {
    private final MarketQuery marketQuery;

    public TradingRecord sma() {
        BarSeries barSeries = marketQuery.getBarsFrom("BTCUSDT", "1s", 30, LocalDateTime.now());

        // Define the moving average indicators
        ClosePriceIndicator closePrice = new ClosePriceIndicator(barSeries);
        SMAIndicator shortSma = new SMAIndicator(closePrice, 12); // Short-term SMA
        SMAIndicator longSma = new SMAIndicator(closePrice, 26); // Long-term SMA

        // Define the entry rule: short SMA crosses above long SMA
        Rule entryRule = new CrossedUpIndicatorRule(shortSma, longSma);

        // Define the exit rule: short SMA crosses below long SMA
        Rule exitRule = new CrossedDownIndicatorRule(shortSma, longSma);

        // Define a trading strategy
        BaseStrategy strategy = new BaseStrategy(entryRule, exitRule);
        return new BarSeriesManager(barSeries).run(strategy);
    }
}
