package dao;

import exceptions.DataAccessException;
import model.Wallet;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class WalletFileSystemDao implements  WalletDao {

    private static final String FILE_PATH = "src/main/resources/csv/wallets.csv";
    private static WalletFileSystemDao instance;
    private List<Wallet> walletList;

    private WalletFileSystemDao() {
        this.walletList = new ArrayList<>();
    }

    public static synchronized WalletFileSystemDao getInstance() {
        if (instance == null) {
            instance = new WalletFileSystemDao();
        }
        return instance;
    }

    private List<String> readFile() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private void writeFile(List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    @Override
    public Wallet selectWalletById(int walletId) throws DataAccessException {
            for (Wallet wallet : walletList) {
                if (wallet.getWalletId() == walletId) {
                    return wallet;
                }
            }

        List<String> lines = null;
        try {
            lines = readFile();
            for (String line : lines) {
                String[] arr = line.split(",");
                if (Integer.parseInt(arr[0]) == walletId) {
                    return new Wallet(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
                }
            }
        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    public void updateWallet(Wallet wallet) throws  DataAccessException {

        List<String> lines = null;
        try {
            lines = readFile();
            boolean walletUpdated = false;
            for (int i = 0; i < lines.size(); i++) {
                String[] arr = lines.get(i).split(",");
                int walletId = Integer.parseInt(arr[0]);
                if (walletId == wallet.getWalletId()) {
                    lines.set(i, arr[0] + "," + wallet.getBalance() + "," + arr[2]);
                    walletUpdated = true;
                }
            }
            if (walletUpdated) {
                writeFile(lines);
            }
        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void saveWallet(Wallet wallet, String walletOwner) throws DataAccessException {
        List<String> lines = null;
        try {
            lines = readFile();
            int maxId = lines.stream()
                    .map(line -> Integer.parseInt(line.split(",")[0]))
                    .max(Integer::compareTo)
                    .orElse(0) + 1;
            wallet.setWalletId(maxId);
            lines.add(wallet.toCsvString() + "," + walletOwner);
            writeFile(lines);
        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }


    }
}
