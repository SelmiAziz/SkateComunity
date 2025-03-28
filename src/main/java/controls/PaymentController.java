package controls;

import dao.CustomerDao;
import dao.WalletDao;
import dao.patternAbstractFactory.DaoFactory;
import exceptions.InsufficientCoinsException;
import model.Wallet;

public class PaymentController {
    private WalletDao walletDao = DaoFactory.getInstance().createWalletDao();



    //This is an INCLUDE for singEventController and CommissionController

    //NOTE: this is an INCLUDE so it has parameters as entity and not beans !!!!
    public void payWithCoins (Wallet wallet, int coinsPayed) throws InsufficientCoinsException {
        if(wallet.getBalance()- coinsPayed < 0){
            throw new InsufficientCoinsException("Numero di coins insufficiente!!!");
        }
        wallet.payCoins(coinsPayed);
        walletDao.updateWallet(wallet);
    }
}
