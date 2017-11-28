package domain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.MoneyFormatter;

public class TransactionWallet {

    private String name;
    private List<Transaction> transactions;

    public TransactionWallet(String name) {
        this.name = name;
        transactions = new ArrayList<>();
    }

    public BigDecimal getBalance() {
        BigDecimal balance = new BigDecimal(0);
        for (Transaction transaction : transactions) {
            balance = balance.add(transaction.getAmount());
        }
        return balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public ObservableList getTransactionOverview() {
        ObservableList<String> result = FXCollections.observableArrayList();

        for (Transaction transaction : transactions) {

            String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(transaction.getDateOfTransaction());

            if (transaction.getTransactionType() == TransactionType.CUSTOM) {
                result.add(MoneyFormatter.formatMoney(transaction.getAmount()) + " - " + formattedDate + " - " + transaction.getAdditionalMessage());
            } else {
                result.add(MoneyFormatter.formatMoney(transaction.getAmount()) + " - " + formattedDate + " - " + transaction.getTransactionType());
            }
        }
        Collections.reverse(result);
        return result;
    }
}
