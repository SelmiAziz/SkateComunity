import model.Wallet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {
    @Test
    public void testDepositCoins(){
        Wallet wallet = new Wallet();
        wallet.depositCoins(100);
        wallet.depositCoins(50);
        assertEquals(150, wallet.getBalance());
    }
}
