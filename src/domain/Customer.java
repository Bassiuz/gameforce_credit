package domain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static managers.CustomerManager.loadCustomers;
import utils.MoneyFormatter;

public class Customer implements Comparable{

    private String name;
    private String extraMessage;

    private List<TransactionWallet> wallets;

    public Customer(String name, String extraMessage, String walletName) {
        this.name = name;
        this.extraMessage = extraMessage;
        this.wallets = new ArrayList<>();
        this.wallets.add(new TransactionWallet(walletName));
    }
    
    public Customer(String name, String extraMessage) {
        this.name = name;
        this.extraMessage = extraMessage;
        this.wallets = new ArrayList<>();
        this.wallets.add(new TransactionWallet("Card Credit"));
        this.wallets.add(new TransactionWallet("Store Credit"));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtraMessage() {
        return extraMessage;
    }

    public void setExtraMessage(String extraMessage) {
        this.extraMessage = extraMessage;
    }

    public ObservableList<String> getWalletNames() {
        ObservableList<String> result = FXCollections.observableArrayList();
        for (TransactionWallet wallet : getWallets())
        {
            result.add(wallet.getName());
        }
        return result;
    }

    public List<TransactionWallet> getWallets() {
        return wallets;
    }

    public TransactionWallet getWalletWithName(String name) {
        if (name != null){
        for (TransactionWallet wallet : wallets) {
            if (wallet.getName().toUpperCase().equals(name.toUpperCase())) {
                return wallet;
            }
        }
        }
        
        return null;
    }

    public void addWallet(String name) {
        this.wallets.add(new TransactionWallet(name));
    }

    public void setWallets(List<TransactionWallet> wallets) {
        this.wallets = wallets;
    }

    public ObservableList getWalletOverview() {
         ObservableList<String> result = FXCollections.observableArrayList();

        for (TransactionWallet wallet : getWallets()) {

           result.add(wallet.getName() + " - " + MoneyFormatter.formatMoney(wallet.getBalance()));
        }
        Collections.reverse(result);
        return result;
    }
    
    public BigDecimal getTotalMoney()
    {
        BigDecimal total = new BigDecimal(0);
        for (TransactionWallet wallet : wallets)
        {
            total = total.add(wallet.getBalance());
        }
        return total;
        
    }

    @Override
    public int compareTo(Object o) {
        return this.getName().compareTo(((Customer)o).getName());
    }
}
