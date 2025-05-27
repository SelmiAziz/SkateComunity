package dao;

import exceptions.DataAccessException;
import model.Wallet;

import java.io.IOException;

public interface WalletDao {
    void saveWallet(Wallet wallet, String walletOwner) throws DataAccessException;
    Wallet selectWalletById(int walletId) throws DataAccessException;
    void updateWallet(Wallet wallet) throws DataAccessException;
}
