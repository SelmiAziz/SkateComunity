package dao;

import model.Wallet;

import java.io.IOException;

public interface WalletDao {
    void saveWallet(Wallet wallet, String walletOwner) throws IOException;
    Wallet selectWalletById(int walletId) throws IOException;
    void updateWallet(Wallet wallet) throws IOException;
}
