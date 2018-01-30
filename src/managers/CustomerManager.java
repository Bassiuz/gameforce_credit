package managers;

import domain.Customer;
import domain.TransactionWallet;
import utils.CustomerJSONWriter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerManager {
    private static List<Customer> customers;

    public static void loadCustomers()
    {
        customers = CustomerJSONWriter.readFromJson();
    }

    public static void saveCustomers()
    {
       CustomerJSONWriter.writeToJSON(customers);
    }

    public static List<Customer> getCustomers() {
        return customers;
    }
    
     public static ObservableList<String> getCustomerNames() {
        loadCustomers();
        if (customers == null){
            return null;
        }
        ObservableList<String> result =  FXCollections.observableArrayList();
        
        for (Customer customer : customers)
        {
            result.add(customer.getName());
        }
        return result;
    }

    public static void setCustomers(List<Customer> customers) {
        CustomerManager.customers = customers;
        saveCustomers();
    }

    public static List<String> getUsedWalletNames()
    {
        ArrayList<String> names = new ArrayList<>();

        for(Customer customer : customers)
        {
            for(TransactionWallet wallet : customer.getWallets())
            {
                if (!names.contains(wallet.getName()))
                {
                    names.add(wallet.getName());
                }
            }
        }

        return names;
    }

    public static TransactionWallet addWalletToCustomer(Customer customer, String walletName)
    {
        if(customer.getWalletWithName(walletName) == null)
        {
            customer.addWallet(walletName);
        }

        saveCustomers();
        return customer.getWalletWithName(walletName);
    }

    public static boolean customerAlreadyKnown(String name)
    {
        if (customers == null)
        {
            return false;
        }
        for(Customer customer : customers)
        {
            if (customer.getName().equals(name))
            {
                return true;
            }
        }
        return false;
    }

    public static Customer addCustomer(Customer customer) {
        if (customerAlreadyKnown(customer.getName())) {
            return null;
        }
        if (customers == null)
        {
            customers = new ArrayList<Customer>();
        }

        customers.add(customer);
        saveCustomers();
        return customer;
    }

    public static Customer getCustomerWithName(String name) {
        for (Customer customer : customers)
        {
            if (customer.getName().equals(name))
            {
                return customer;
            }
        }
        return null;
    }

    public static String deleteCustomer(Customer customer) {
        boolean hasBalance = false;
        for (TransactionWallet wallet : customer.getWallets())
        {
            if (!wallet.getBalance().setScale(2, RoundingMode.HALF_DOWN).equals(new BigDecimal(0).setScale(2,  RoundingMode.HALF_DOWN)))
            {
                hasBalance = true;
            }
        }
        if (hasBalance)
        {
            return customer.getName() + " still has balance on his account!";
        }
        else
        {
            customers.remove(customer);
            saveCustomers();
            return customer.getName() + " has been deleted";
        }
    }

    public static String editCustomerName(Customer selectedCustomer, String text) {
        if (getCustomerWithName(text) != null)
        {
            return "Naam is al in gebruik";
        }
        selectedCustomer.setName(text);
        saveCustomers();
        return "De naam van " + text + "is gewijzigd";
    }
}
