package managers;

import domain.Customer;
import domain.Transaction;
import domain.TransactionWallet;

import java.math.BigDecimal;

public class TransactionManager {

    public static void addTransaction(String customerName, String walletName, Transaction transaction)
    {
        for (Customer customer : CustomerManager.getCustomers())
        {
            addTransaction(customer, walletName, transaction);
        }
    }

    public static void addTransaction(Customer customer, String walletName, Transaction transaction)
    {
        for(TransactionWallet wallet : customer.getWallets())
        {
            if (wallet.getName().equals(walletName))
            {
                addTransaction(customer, wallet, transaction);
            }
        }
    }

    public static void addTransaction(Customer customer, TransactionWallet wallet, Transaction transaction)
    {
        wallet.addTransaction(transaction);
        CustomerManager.saveCustomers();
    }


}
