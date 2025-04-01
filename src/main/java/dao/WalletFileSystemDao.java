package dao;

import model.Wallet;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WalletFileSystemDao implements WalletDao {
    private static WalletFileSystemDao instance;
    private List<Wallet> walletList;
    private final String fd = "src/main/resources/csv/wallets.csv";

    WalletFileSystemDao() {
        this.walletList = new ArrayList<>();
    }

    public static synchronized WalletFileSystemDao getInstance() {
        if (instance == null) {
            instance = new WalletFileSystemDao();
        }
        return instance;
    }

    @Override
    public Wallet selectWalletById(int walletId) {
        for (Wallet wallet : walletList) {
            if (wallet.getWalletId() == walletId) {
                return wallet;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fd))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] arr = line.split(",");
                if (Integer.parseInt(arr[0]) == walletId) {
                    return new Wallet(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateWallet(Wallet wallet) {
        File tempFile = new File("tempFile.csv");
        File originalFile = new File(fd);
        boolean walletUpdated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] arr = line.split(",");
                int walletId = Integer.parseInt(arr[0]);

                if (walletId == wallet.getWalletId()) {
                    line = arr[0] + "," + wallet.getBalance() + "," + arr[2];
                    walletUpdated = true;
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (walletUpdated) {
            if (!originalFile.delete()) {
                throw new RuntimeException("Failed to delete original file");
            }
            if (!tempFile.renameTo(originalFile)) {
                throw new RuntimeException("Failed to rename temp file");
            }
        }
    }

    @Override
    public void addWallet(Wallet wallet, String walletOwner) {
        walletList.add(wallet);
        try (BufferedReader reader = new BufferedReader(new FileReader(fd))) {
            String line;
            int maxId = 0;
            while ((line = reader.readLine()) != null) {
                String[] arr = line.split(",");
                maxId = Math.max(maxId, Integer.parseInt(arr[0]));
            }
            maxId++;
            wallet.setWalletId(maxId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fd, true))) {
            writer.write(wallet.toCsvString() + "," + walletOwner);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
