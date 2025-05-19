package dao;

import model.Wallet;

import java.util.ArrayList;
import java.util.List;

public class WalletDemoDao implements WalletDao {
    List<Wallet> walletList = new ArrayList<>();
    private static WalletDemoDao instance;


    public  static synchronized WalletDemoDao getInstance(){
        if(instance == null){
            instance = new WalletDemoDao();
        }
        return instance;
    }

    @Override
    public Wallet selectWalletById(int walletId) {
        for(Wallet wallet:walletList){
            if(wallet.getWalletId() == walletId){
                return wallet;
            }
        }

        return null;
    }

    @Override
    public void saveWallet(Wallet wallet, String walletOwner) {
        walletList.add(wallet);
    }


    @Override
    public void updateWallet(Wallet wallet) {
        //it is not needed in demo
    }

}
