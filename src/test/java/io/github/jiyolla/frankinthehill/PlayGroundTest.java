package io.github.jiyolla.frankinthehill;

import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Wallet;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class PlayGroundTest {

    @Autowired
    private SpotClientImpl spotClientImpl;

    @Test
    void test() {
        Wallet wallet = spotClientImpl.createWallet();
        log.info(wallet.apiPermission(null));
    }
}
