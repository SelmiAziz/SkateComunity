package dao;


import model.Wallet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WalletFileSystemDao implements WalletDao{
    private static WalletFileSystemDao instance;
    private List<Wallet> walletList ;
    private String fd = "src/main/resources/csv/wallets.csv";

    WalletFileSystemDao(){
        this.walletList = new ArrayList<>();
    }

    public static synchronized WalletFileSystemDao getInstance(){
        if(instance == null){
            instance = new WalletFileSystemDao();
        }
        return instance;
    }

    @Override
    public Wallet selectWalletById(int walletId) {
        for(Wallet wallet: walletList){
            if(wallet.getWalletId() == walletId){
                return wallet;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fd))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] arr = line.split(",");
                if(Integer.parseInt(arr[0])== walletId){
                    return new Wallet(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]) );
                }
            }

        }catch(IOException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateWallet(Wallet wallet) {
        File tempFile = new File("tempFile.csv"); // Creiamo un file temporaneo
        File originalFile = new File(fd); // Il file originale
        boolean walletUpdated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] arr = line.split(",");
                int walletId = Integer.parseInt(arr[0]);

                // Se troviamo il wallet corrispondente, aggiorniamo il saldo
                if (walletId == wallet.getWalletId()) {
                    line = arr[0] + "," + wallet.getBalance() + "," + arr[2]; // Aggiorna il saldo
                    walletUpdated = true;
                }

                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ora sostituiamo il file originale con quello temporaneo
        if (walletUpdated) {
            if (!originalFile.delete()) {
            }
            if (!tempFile.renameTo(originalFile)) {
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
                maxId = Integer.parseInt(arr[0]) > maxId ? Integer.parseInt(arr[0]) : maxId;
            }
            maxId++;
            wallet.setWalletId(maxId);

        }catch(IOException e){
            throw new RuntimeException(e);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fd, true))) {
            writer.write(wallet.toCsvString()+","+walletOwner);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
