package controls;

import dao.WalletDao;
import dao.patternabstractfactory.DaoFactory;
import exceptions.DataAccessException;
import exceptions.InsufficientCoinsException;
import model.Wallet;

import java.io.IOException;

public class PaymentController {
    private WalletDao walletDao = DaoFactory.getInstance().createWalletDao();



    //This is an INCLUDE for singEventController and CommissionController

    //NOTE: this is an INCLUDE so it has parameters as entity and not beans !!!!
    public void payWithCoins (Wallet wallet, int coinsPayed) throws InsufficientCoinsException, DataAccessException {
        if(wallet.getBalance()- coinsPayed < 0){
            throw new InsufficientCoinsException("Numero di coins insufficiente!!!");
        }
        wallet.payCoins(coinsPayed);
        walletDao.updateWallet(wallet);
    }
}
