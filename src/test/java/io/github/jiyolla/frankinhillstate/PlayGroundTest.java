package io.github.jiyolla.frankinhillstate;

import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Wallet;
import io.github.jiyolla.frankinhillstate.command.MarketCommand;
import io.github.jiyolla.frankinhillstate.query.MarketQuery;
import io.github.jiyolla.frankinhillstate.query.MyWalletQuery;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.HashMap;

@Slf4j
@SpringBootTest
class PlayGroundTest {

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


        log.info(marketQuery.getCurrentPrice("BTCUSDT"));
        log.info(marketQuery.getRSI("BTCUSDT"));

        log.info(myWalletQuery.getTrades("BTCUSDT"));
        log.info(myWalletQuery.getAssets());
        log.info(wallet.apiPermission(null));
    }

    @Test
    void test2() {
        // TODO - [24-06-25][frank.burger]: Min notional를 넘고, FOK로 expire하도록 가격을 설정해둠. 실제 거래를 시도하기에 가격 확인하고 조심히 실행할 것
        log.info(marketCommand.placeSellOrder("TRXUSDT", new BigDecimal(25), new BigDecimal("0.2")));
        log.info(marketCommand.placeBuyOrder("TRXUSDT", new BigDecimal(50), new BigDecimal("0.1")));
    }
}
