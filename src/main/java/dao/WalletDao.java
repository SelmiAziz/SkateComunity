package dao;

import model.Wallet;

public interface WalletDao {
    void addWallet(Wallet wallet, String CustomerUsername);
    Wallet selectWalletById(int walletId);
    void updateWallet(Wallet wallet);
}
