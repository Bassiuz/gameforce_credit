package domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class Transaction {

    private BigDecimal amount;
    private Date dateOfTransaction;
    private TransactionType transactionType;
    private String additionalMessage;

    public Transaction(BigDecimal amount,String additionalMessage) {
        this.amount = amount;
        dateOfTransaction = new Date();
        this.transactionType = TransactionType.CUSTOM;
        this.additionalMessage = additionalMessage;
    }

    public Transaction(BigDecimal amount, TransactionType transactionType) {
        this.amount = amount;
        dateOfTransaction = new Date();
        this.transactionType = transactionType;
        this.additionalMessage = additionalMessage;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.HALF_DOWN);
    }

    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(Date dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getAdditionalMessage() {
        return additionalMessage;
    }

    public void setAdditionalMessage(String additionalMessage) {
        this.additionalMessage = additionalMessage;
    }
}
