package dao;


import exceptions.DataAccessException;
import model.Wallet;
import utils.DbsConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WalletDbmsDao implements  WalletDao{
    private List<Wallet> walletList = new ArrayList<>();
    private static WalletDbmsDao instance;


    public  static synchronized WalletDbmsDao getInstance(){
        if(instance == null){
            instance = new WalletDbmsDao();
        }
        return instance;
    }


    @Override
    public void updateWallet(Wallet wallet) throws DataAccessException{
            String query = "UPDATE wallets SET balance = ? WHERE walletId = ?";
            Connection connection = DbsConnector.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, wallet.getBalance());
                preparedStatement.setInt(2, wallet.getWalletId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
               throw new DataAccessException(e.getMessage());
            }
    }


    @Override public Wallet selectWalletById(int walletId) throws DataAccessException{
        for(Wallet wallet:walletList){
            if(wallet.getWalletId() == walletId){
                return wallet;
            }
        }

        String sql = "SELECT w.balance " +
                "FROM wallets w " +
                "WHERE w.walletId = ?";

        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, walletId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int balance = rs.getInt("balance");

                return new Wallet(walletId, balance);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

        return null;
    }

    @Override
    public void saveWallet(Wallet wallet, String walletOwner) throws DataAccessException{
        walletList.add(wallet);


        String sql = "INSERT INTO wallets (balance,walletOwner) " +
                "VALUES (?,?)";


        Connection conn = DbsConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, wallet.getBalance());
            stmt.setString(2,walletOwner);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int walletId = generatedKeys.getInt(1);
                        wallet.setWalletId(walletId);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
