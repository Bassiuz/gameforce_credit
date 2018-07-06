package managers;

import domain.Customer;
import domain.TransactionWallet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import utils.CustomerJSONWriter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerManager {

    private static List<Customer> customers;

    public static void loadCustomers() {
        customers = CustomerJSONWriter.readFromJson();
    }

    public static void saveCustomers() {
        Collections.sort(customers);
        CustomerJSONWriter.writeToJSON(customers);
    }

    public static List<Customer> getCustomers() {
        return customers;
    }

    public static ObservableList<String> getCustomerNames() {
        loadCustomers();
        if (customers == null) {
            return null;
        }
        ObservableList<String> result = FXCollections.observableArrayList();

        for (Customer customer : customers) {
            result.add(customer.getName());
        }
        return result;
    }

    public static void setCustomers(List<Customer> customers) {
        CustomerManager.customers = customers;
        saveCustomers();
    }

    public static List<String> getUsedWalletNames() {
        ArrayList<String> names = new ArrayList<>();

        for (Customer customer : customers) {
            for (TransactionWallet wallet : customer.getWallets()) {
                if (!names.contains(wallet.getName())) {
                    names.add(wallet.getName());
                }
            }
        }

        return names;
    }

    public static TransactionWallet addWalletToCustomer(Customer customer, String walletName) {
        if (customer.getWalletWithName(walletName) == null) {
            customer.addWallet(walletName);
        }

        saveCustomers();
        return customer.getWalletWithName(walletName);
    }

    public static boolean customerAlreadyKnown(String name) {
        if (customers == null) {
            return false;
        }
        for (Customer customer : customers) {
            if (customer.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static Customer addCustomer(Customer customer) {
        if (customerAlreadyKnown(customer.getName())) {
            return null;
        }
        if (customers == null) {
            customers = new ArrayList<Customer>();
        }

        customers.add(customer);
        saveCustomers();
        return customer;
    }

    public static Customer getCustomerWithName(String name) {
        for (Customer customer : customers) {
            if (customer.getName().equals(name)) {
                return customer;
            }
        }
        return null;
    }

    public static String deleteCustomer(Customer customer) {
        boolean hasBalance = false;
        for (TransactionWallet wallet : customer.getWallets()) {
            if (!wallet.getBalance().setScale(2, RoundingMode.HALF_DOWN).equals(new BigDecimal(0).setScale(2, RoundingMode.HALF_DOWN))) {
                hasBalance = true;
            }
        }
        if (hasBalance) {
            return customer.getName() + " still has balance on his account!";
        } else {
            customers.remove(customer);
            saveCustomers();
            return customer.getName() + " has been deleted";
        }
    }

    public static String editCustomerName(Customer selectedCustomer, String text) {
        if (getCustomerWithName(text) != null) {
            return "Naam is al in gebruik";
        }
        selectedCustomer.setName(text);
        saveCustomers();
        return "De naam van " + text + "is gewijzigd";
    }

    public static File createExport() {

        List<String> walletNames = new ArrayList<>();
        int amountOfWallets = 0;
        for(Customer customer : customers)
        {
            if (customer.getWallets().size() > amountOfWallets)
            {
                amountOfWallets = customer.getWallets().size();
                walletNames = customer.getWalletNames();
            }
        }

        PrintWriter pw = null;
        try {
            File file = new File("Gameforce Customer Export.csv");
            pw = new PrintWriter(file);
            StringBuilder sb = new StringBuilder();
            sb.append("sep=;");
            sb.append('\n');
            sb.append("Klantnaam");
            sb.append(';');
            sb.append("Opmerking");
            sb.append(';');
            for(String walletName : walletNames)
            {
                sb.append(walletName);
                sb.append(';');
            }
             sb.append("Totaal");
            sb.append(';');
            sb.append('\n');
            for (Customer customer : customers) {

                sb.append(customer.getName());
                sb.append(';');
                sb.append(customer.getExtraMessage());
                sb.append(';');
                int i = 0;
                while (i < amountOfWallets) {
                    if (customer.getWallets().size() > i) {
                        sb.append("=\"" + formatMoney(customer.getWallets().get(i).getBalance()) + "\"");
                    } else {
                        sb.append("");
                    }
                    sb.append(';');
                    i++;
                }
                 sb.append(formatMoney(customer.getTotalMoney()));
                sb.append(';');
                sb.append('\n');
            }

            String string = sb.toString();
            System.out.println(string);
            pw.write(string);
            pw.close();
            return file;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CustomerManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pw.close();
        }
        return null;
    }

    public static String formatMoney(BigDecimal amount) {
        BigDecimal fracBd = amount.subtract(new BigDecimal(amount.toBigInteger()));
        String fracString = fracBd.toPlainString();
        if (fracBd.toPlainString().length() < 4) {
            fracString = "0.0000";
        }
        return amount.toBigInteger().toString() + "," + fracString.substring(fracString.length() - 4);

    }
}
