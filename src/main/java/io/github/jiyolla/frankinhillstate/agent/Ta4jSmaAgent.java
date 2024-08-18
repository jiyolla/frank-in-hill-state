package io.github.jiyolla.frankinhillstate.agent;

import io.github.jiyolla.frankinhillstate.command.MarketCommand;
import io.github.jiyolla.frankinhillstate.query.MarketQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ta4j.core.*;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Component
public class Ta4jSmaAgent {
    private final MarketQuery marketQuery;
    private final MarketCommand marketCommand;
    private final BaseStrategy strategy;
    private final TradingRecord tradingRecord;
    private BarSeries barSeries;

    public Ta4jSmaAgent(MarketQuery marketQuery, MarketCommand marketCommand) {
        this.marketQuery = marketQuery;
        this.marketCommand = marketCommand;
        barSeries = marketQuery.getBars("BTCUSDT", "1s", 30, LocalDateTime.now());

        // Define the moving average indicators
        ClosePriceIndicator closePrice = new ClosePriceIndicator(barSeries);
        SMAIndicator shortSma = new SMAIndicator(closePrice, 12); // Short-term SMA
        SMAIndicator longSma = new SMAIndicator(closePrice, 26); // Long-term SMA

        // Define the entry rule: short SMA crosses above long SMA
        Rule entryRule = new CrossedUpIndicatorRule(shortSma, longSma);

        // Define the exit rule: short SMA crosses below long SMA
        Rule exitRule = new CrossedDownIndicatorRule(shortSma, longSma);

        // Define a trading strategy
        strategy = new BaseStrategy(entryRule, exitRule);
        tradingRecord = new BaseTradingRecord();
    }

    public void signal(LocalDateTime now) {
        updateBarSeries(now);

        int endIndex = barSeries.getEndIndex();
        Bar lastBar = barSeries.getLastBar();
        log.info("Last Bar: {}", lastBar);
        if (strategy.shouldEnter(endIndex)) {
            log.info("Buy signal at {} price {}", lastBar.getEndTime(), lastBar.getClosePrice());
            marketCommand.buy("BTCUSDT", new BigDecimal(DecimalNum.valueOf(0.0001).toString()),
                              new BigDecimal(lastBar.getClosePrice().toString()));
            log.info("Bought at {} price {}", lastBar.getEndTime(), lastBar.getClosePrice());
            // TODO - [24-07-08][frank.burger]: 얼마를 들어갈 것인가
            tradingRecord.enter(endIndex, lastBar.getClosePrice(), DecimalNum.valueOf(0.0001));
        } else if (strategy.shouldExit(endIndex)) {
            log.info("Sell signal at {} price {}", lastBar.getEndTime(), lastBar.getClosePrice());
            marketCommand.sell("BTCUSDT", new BigDecimal(DecimalNum.valueOf(0.0001).toString()),
                               new BigDecimal(lastBar.getClosePrice().toString()));
            log.info("Sold at {} price {}", lastBar.getEndTime(), lastBar.getClosePrice());
            tradingRecord.exit(endIndex, lastBar.getClosePrice(), DecimalNum.valueOf(0.0001));
        }
    }

    private void updateBarSeries(LocalDateTime now) {
        // TODO - [24-07-08][frank.burger]: 주기를 가정하면 안 됨, signal이 어떤 간격으로 호출될지 모름
        BarSeries bars = marketQuery.getBars("BTCUSDT", "1s", 10, now);
        for (int i = 0; i < bars.getBarCount(); i++) {
            if (bars.getBar(i).getEndTime().equals(barSeries.getLastBar().getEndTime())) {
                // update Last bar
                barSeries.addPrice(bars.getBar(i).getClosePrice());
                for (int j = i + 1; j < bars.getBarCount(); j++) {
                    barSeries.addBar(bars.getBar(j));
                }
                break;
            }
        }
    }
}
