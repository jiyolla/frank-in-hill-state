package io.github.jiyolla.frankinhillstate;

import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Wallet;
import io.github.jiyolla.frankinhillstate.backtesting.ta4j.Ta4jBacktesting;
import io.github.jiyolla.frankinhillstate.command.MarketCommand;
import io.github.jiyolla.frankinhillstate.query.MarketQuery;
import io.github.jiyolla.frankinhillstate.query.MyWalletQuery;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.ta4j.core.*;
import org.ta4j.core.analysis.cost.LinearTransactionCostModel;
import org.ta4j.core.analysis.cost.ZeroCostModel;
import org.ta4j.core.backtest.BarSeriesManager;
import org.ta4j.core.criteria.PositionsRatioCriterion;
import org.ta4j.core.criteria.ReturnOverMaxDrawdownCriterion;
import org.ta4j.core.criteria.VersusEnterAndHoldCriterion;
import org.ta4j.core.criteria.pnl.ProfitLossCriterion;
import org.ta4j.core.criteria.pnl.ReturnCriterion;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest
class PlayGroundTest {

    @Autowired
    private Ta4jBacktesting ta4jBacktesting;
    @Autowired
    private SpotClientImpl spotClientImpl;
    @Autowired
    private MyWalletQuery myWalletQuery;
    @Autowired
    private MarketQuery marketQuery;
    @Autowired
    private MarketCommand marketCommand;

    @Test
    void test() {
        Wallet wallet = spotClientImpl.createWallet();
        spotClientImpl.createMarket().exchangeInfo(new HashMap<>());
        marketQuery.getBarsFrom("BTCUSDT", "1s", 100, LocalDateTime.now().minusDays(1));


        log.info(marketQuery.getCurrentPrice("BTCUSDT"));
        log.info(marketQuery.getRSI("BTCUSDT"));
        log.info(
                marketQuery.getSMA("BTCUSDT", "1h", 20, LocalDateTime.of(2024, 7, 6, 12, 0).plusSeconds(1)).toString());

        log.info(myWalletQuery.getTrades("BTCUSDT"));
        log.info(myWalletQuery.getAssets());
        log.info(wallet.apiPermission(null));
    }

    @Test
    void test2() {
        // TODO - [24-06-25][frank.burger]: Min notional를 넘고, FOK로 expire하도록 가격을 설정해둠. 실제 거래를 시도하기에 가격 확인하고 조심히 실행할 것
        log.info(marketCommand.sell("TRXUSDT", new BigDecimal(25), new BigDecimal("0.2")));
        log.info(marketCommand.buy("TRXUSDT", new BigDecimal(50), new BigDecimal("0.1")));
    }

    @Test
    void test3() {
        BarSeries barSeries = marketQuery.getBarsFrom("BTCUSDT", "15m", 1440,
                                                      LocalDateTime.of(2024, 8, 16, 2, 0, 0));

        // Define the moving average indicators
        ClosePriceIndicator closePrice = new ClosePriceIndicator(barSeries);
        SMAIndicator shortSma = new SMAIndicator(closePrice, 12); // Short-term SMA
        SMAIndicator longSma = new SMAIndicator(closePrice, 26); // Long-term SMA

        // Define the entry rule: short SMA crosses above long SMA
        Rule entryRule = new CrossedUpIndicatorRule(shortSma, longSma);
        // Rule entryRule = new CrossedDownIndicatorRule(shortSma, longSma);

        // Define the exit rule: short SMA crosses below long SMA
        Rule exitRule = new CrossedDownIndicatorRule(shortSma, longSma);
        // Rule exitRule = new CrossedUpIndicatorRule(shortSma, longSma);

        // Define a trading strategy
        BaseStrategy strategy = new BaseStrategy(entryRule, exitRule);
        BarSeriesManager barSeriesManager = new BarSeriesManager(barSeries,
                                                                 // new ZeroCostModel(),
                                                                 new LinearTransactionCostModel(0.001),
                                                                 new ZeroCostModel());
        TradingRecord tradingRecord = barSeriesManager.run(strategy);
        log.info(tradingRecord.toString());

        // Getting the winning positions ratio
        AnalysisCriterion winningPositionsRatio = new PositionsRatioCriterion(AnalysisCriterion.PositionFilter.PROFIT);
        Num winningPositionRatio = winningPositionsRatio.calculate(barSeries, tradingRecord);
        log.info("Winning positions ratio: {}", winningPositionRatio);

        // Getting a risk-reward ratio
        AnalysisCriterion romad = new ReturnOverMaxDrawdownCriterion();
        Num ristRewardRatio = romad.calculate(barSeries, tradingRecord);
        log.info("Return over Max Drawdown: {}", ristRewardRatio);

        // Total return of our strategy vs total return of a buy-and-hold strategy
        AnalysisCriterion vsBuyAndHold = new VersusEnterAndHoldCriterion(new ReturnCriterion());
        Num totalReturn = vsBuyAndHold.calculate(barSeries, tradingRecord);
        log.info("Our return vs buy-and-hold return: {}", totalReturn);

        AnalysisCriterion profitCriterion = new ProfitLossCriterion();
        Num profit = profitCriterion.calculate(barSeries, tradingRecord);
        List<Num> collect = tradingRecord.getPositions().stream().map(
                position -> position.getExit().getNetPrice().minus(position.getEntry().getNetPrice())).collect(
                Collectors.toList());
        log.info("Profit: {}", profit);

        Num roi = new ReturnCriterion().calculate(barSeries, tradingRecord);
        log.info("ROI: {}", roi);
    }
}
