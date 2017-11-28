/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import domain.Customer;
import domain.Transaction;
import domain.TransactionType;
import domain.TransactionWallet;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import managers.CustomerManager;
import utils.MoneyFormatter;

/**
 * FXML Controller class
 *
 * @author Bas de Vaan
 */
public class GameforceCreditController implements Initializable {

    @FXML
    private Label balanceLabel;

    @FXML
    private Label cardCreditLabel;
    
     @FXML
    private Label storeCreditLabel;

    @FXML
    private TextField newCustomer;

    @FXML
    private TextField newCustomerWallet;

    @FXML
    private TextField newWallet;

    @FXML
    private TextField newTransactionAmount;

    @FXML
    private TextArea newTransactionComment;

    @FXML
    private ComboBox newTransactionType;

    @FXML
    private ComboBox customerList;

    @FXML
    private ComboBox customerList2;

    @FXML
    private ComboBox customerList3;

    @FXML
    private ListView transactionView;

    @FXML
    ComboBox walletList;
    
     @FXML
    private ListView walletListView;
    

    private Customer selectedCustomer;
    private TransactionWallet selectedWallet;

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }

    @FXML
    private void selectCustomer(ActionEvent event) {
        if (event != null) {
            selectedCustomer = CustomerManager.getCustomerWithName((String) ((ComboBox) event.getSource()).getSelectionModel().getSelectedItem());

        } else {
            selectedCustomer = CustomerManager.getCustomerWithName((String) customerList.getSelectionModel().getSelectedItem());

        }
        initTransactionWalletBox(selectedCustomer);
       
    }

    @FXML
    private void selectWallet(ActionEvent event) {
        selectedWallet = selectedCustomer.getWalletWithName((String) walletList.getSelectionModel().getSelectedItem());
        if (selectedWallet != null) {
            setBalance(selectedWallet);
            transactionView.setItems(selectedWallet.getTransactionOverview());
        }

    }


    private void setBalance(TransactionWallet transactionWallet) {
        initWalletListView();
    }

    @FXML
    private void addWallet(ActionEvent event) {
        selectedCustomer.addWallet(newWallet.getText());
        selectCustomer(null);
        CustomerManager.saveCustomers();
        initTransactionWalletBox(selectedCustomer);
    }

    @FXML
    private void addTransaction(ActionEvent event) {
        doTransaction(false);
    }

    @FXML
    private void removeTransaction(ActionEvent event) {
        doTransaction(true);
    }

    private void doTransaction(boolean negative) {
        if (newTransactionAmount.getText() == null || newTransactionAmount.getText().length() < 1) {
            setErrorMessage("No Amount entered");
        } else if (isTransactionType((String) newTransactionType.getSelectionModel().getSelectedItem())) {
            if (newTransactionType.getSelectionModel().getSelectedItem().equals(TransactionType.CUSTOM.toString())) {
                if (newTransactionComment.getText() == null
                        || newTransactionComment.getText().equals("null")
                        || newTransactionComment.getText().length() < 1) {
                    setErrorMessage("No comment given");
                    return;
                } else {
                    String amount = newTransactionAmount.getText().replace(",", ".").replace("-", "");
                    if (negative) {
                        amount = "-" + amount;
                    }
                    Double amountDouble = Double.parseDouble(amount);
                    selectedWallet.addTransaction(
                            new Transaction(new BigDecimal(amountDouble),
                                    newTransactionComment.getText()));
                    newTransactionAmount.setText("");
                    newTransactionComment.setText("");
                }
            } else {
                String amount = newTransactionAmount.getText().replace(",", ".").replace("-", "");
                if (negative) {
                    amount = "-" + amount;
                }
                Double amountDouble = Double.parseDouble(amount);

                selectedWallet.addTransaction(
                        new Transaction(new BigDecimal(amountDouble),
                                getTransactionType((String) newTransactionType.getSelectionModel().getSelectedItem())));
                                    newTransactionAmount.setText("");

            }
            selectWallet(null);
            CustomerManager.saveCustomers();
        } else {
            setErrorMessage("No Transaction Type Selected");
        }
    }

    private boolean isTransactionType(String input) {
        for (TransactionType type : TransactionType.values()) {
            if (type.name().equals(input)) {
                return true;
            }
        }

        return false;
    }

    private TransactionType getTransactionType(String input) {
        for (TransactionType type : TransactionType.values()) {
            if (type.name().equals(input)) {
                return type;
            }
        }
        return null;

    }

    @FXML
    private void addCustomer(ActionEvent event) {
        if (CustomerManager.getCustomerWithName(newCustomer.getText()) == null){
        CustomerManager.addCustomer(new Customer(newCustomer.getText(), ""));
        CustomerManager.saveCustomers();
        initCustomerBox();
        setInfoMessage(newCustomer.getText() + " is toegevoegd");
        newCustomer.setText("");
        }
        else
        {
            setErrorMessage(newCustomer.getText() + " bestaat al!");
        }
    }

    @FXML
    private void deleteCustomer(ActionEvent event) {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Klant verwijderen");
        alert.setHeaderText("Je bent een klant aan het verwijderen");
        alert.setContentText("Weet je zeker dat je " + selectedCustomer.getName() + " wilt verwijderen?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            String message = CustomerManager.deleteCustomer(selectedCustomer);
            CustomerManager.saveCustomers();
            initCustomerBox();
            setInfoMessage(message);
        } else {
            // ... user chose CANCEL or closed the dialog
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCustomerBox();
        initTransactionTypeBox();
    }


    private void setErrorMessage(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Er is iets fout gegaan");
        alert.setHeaderText("Er is iets fout gegaan");
        alert.setContentText(message);

        alert.showAndWait();
    }
    
       private void setInfoMessage(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Er is iets gebeurd");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    
     private void initTransactionWalletBox(Customer customer) {
        walletList.setItems(customer.getWalletNames());
        walletList.getSelectionModel().selectFirst(); //select the first element        
        selectWallet(null);
        
        //init walletlistview
        initWalletListView();
    }
    
     private void initWalletListView()
     {
                     walletListView.setItems(selectedCustomer.getWalletOverview());

     }

    private void initCustomerBox() {
        if (CustomerManager.getCustomerNames() != null && CustomerManager.getCustomerNames().size() > 0) {
            customerList.setItems(CustomerManager.getCustomerNames());
            customerList2.setItems(CustomerManager.getCustomerNames());
            customerList3.setItems(CustomerManager.getCustomerNames());
            new ComboBoxAutoComplete<String>(customerList);
            customerList.getSelectionModel().selectFirst(); //select the first element
            selectCustomer(null);
        }
    }

    private void initTransactionTypeBox() {
        ObservableList<String> result = FXCollections.observableArrayList();
        for (TransactionType type : TransactionType.values()) {
            result.add(type.name());
        }
        newTransactionType.setItems(result);
        customerList.getSelectionModel().selectFirst(); //select the first element
    }

    private void enableControls() {
        // enable the controlls here
    }

}
