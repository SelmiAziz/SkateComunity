package WalletTest;

import model.Wallet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WalletTest {
    @Test
    void testDepositCoins(){
        Wallet wallet = new Wallet();
        wallet.depositCoins(100);
        wallet.depositCoins(50);
        assertEquals(150, wallet.getBalance());
    }
}
