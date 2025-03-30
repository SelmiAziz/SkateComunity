package dao;

import model.Wallet;

public interface WalletDao {
    void addWallet(Wallet wallet, String walletOwner);
    Wallet selectWalletById(int walletId);
    void updateWallet(Wallet wallet);
}
