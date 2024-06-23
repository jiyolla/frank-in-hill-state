package io.github.jiyolla.frankinthehill;

import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Wallet;
import io.github.jiyolla.frankinthehill.query.Market;
import io.github.jiyolla.frankinthehill.query.MyWallet;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@Slf4j
@SpringBootTest
class PlayGroundTest {

    @Autowired
    private SpotClientImpl spotClientImpl;

    @Autowired
    private MyWallet myWallet;

    @Autowired
    private Market market;

    @Test
    void test() {
        Wallet wallet = spotClientImpl.createWallet();
        spotClientImpl.createMarket().exchangeInfo(new HashMap<>());


        log.info(market.getCurrentPrice("BTCUSDT"));
        log.info(market.getRSI("BTCUSDT"));

        log.info(myWallet.getTrades("BTCUSDT"));
        log.info(myWallet.getAssets());
        log.info(wallet.apiPermission(null));
    }
}
