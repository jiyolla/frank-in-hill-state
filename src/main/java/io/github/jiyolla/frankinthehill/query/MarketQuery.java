package io.github.jiyolla.frankinthehill.query;

import com.binance.connector.client.impl.SpotClientImpl;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MarketQuery {
    private final SpotClientImpl spotClientImpl;

    public String getCurrentPrice(String symbol) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("symbol", symbol);
        return spotClientImpl.createMarket().tickerSymbol(parameters);
    }

    // TODO - [24-06-25][frank.burger]: 파라미터 다양화할 것
    public String getRSI(String symbol) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("symbol", symbol);
        parameters.put("interval", "1h");
        parameters.put("limit", 500);

        String klinesData = spotClientImpl.createMarket().klines(parameters);
        BarSeries series = parseKlines(klinesData);

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        int rsiPeriod = 14;
        RSIIndicator rsi = new RSIIndicator(closePrice, rsiPeriod);
        Num latestRsi = rsi.getValue(series.getEndIndex());

        return "Latest RSI value for " + symbol + ": " + latestRsi;
    }

    private BarSeries parseKlines(String klinesData) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Kline> klines = Arrays.asList(objectMapper.readValue(klinesData, Kline[].class));
            // List<Kline> klines = objectMapper.readValue(klinesData, new TypeReference<List<Kline>>() {});

            BarSeries series = new BaseBarSeries();
            klines.forEach(kline -> {
                ZonedDateTime endTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(kline.getKlineCloseTime()),
                                                                ZoneId.systemDefault());
                series.addBar(new BaseBar(
                        Duration.ofHours(1),
                        endTime,
                        kline.getOpenPrice(),
                        kline.getHighPrice(),
                        kline.getLowPrice(),
                        kline.getClosePrice(),
                        kline.getVolume()
                ));
            });
            return series;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Value
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Kline {
        Long klineOpenTime;
        String openPrice;
        String highPrice;
        String lowPrice;
        String closePrice;
        String volume;
        Long klineCloseTime;
        String quoteAssetVolume;
        String numberOfTrades;
        String takerBuyBaseAssetVolume;
        String takerBuyQuoteAssetVolume;

        @JsonCreator
        public Kline(final List<String> data) {
            this.klineOpenTime = Long.parseLong(data.get(0));
            this.openPrice = data.get(1);
            this.highPrice = data.get(2);
            this.lowPrice = data.get(3);
            this.closePrice = data.get(4);
            this.volume = data.get(5);
            this.klineCloseTime = Long.parseLong(data.get(6));
            this.quoteAssetVolume = data.get(7);
            this.numberOfTrades = data.get(8);
            this.takerBuyBaseAssetVolume = data.get(9);
            this.takerBuyQuoteAssetVolume = data.get(10);
        }
    }
}
