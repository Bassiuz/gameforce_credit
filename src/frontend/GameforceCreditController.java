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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private Label errorLabel;

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
    private ListView transactionView;

    @FXML
    ComboBox walletList;

    private Customer selectedCustomer;
    private TransactionWallet selectedWallet;

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }

    @FXML
    private void selectCustomer(ActionEvent event) {
        selectedCustomer = CustomerManager.getCustomerWithName((String) customerList.getSelectionModel().getSelectedItem());
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
        balanceLabel.setText(MoneyFormatter.formatMoney(transactionWallet.getBalance()));
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
        if (newTransactionAmount.getText() == null || newTransactionAmount.getText().length() < 1) {
            setErrorMessage("No Amount entered");
        } else if (isTransactionType((String) newTransactionType.getSelectionModel().getSelectedItem())) {
            if (newTransactionType.getSelectionModel().getSelectedItem() == TransactionType.CUSTOM) {
                if (newTransactionComment.getText().equals("null")) {
                    setErrorMessage("No comment given");
                    return;
                } else {
                    String amount = newTransactionAmount.getText().replace(",", ".");
                    Double amountDouble = Double.parseDouble(amount);
                    selectedWallet.addTransaction(
                            new Transaction(new BigDecimal(amountDouble),
                                    newTransactionComment.getText()));
                }
            } else {
                String amount = newTransactionAmount.getText().replace(",", ".");
                Double amountDouble = Double.parseDouble(amount);
                selectedWallet.addTransaction(
                        new Transaction(new BigDecimal(amountDouble),
                                getTransactionType((String) newTransactionType.getSelectionModel().getSelectedItem())));
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
        CustomerManager.addCustomer(new Customer(newCustomer.getText(), "", newCustomerWallet.getText()));
        CustomerManager.saveCustomers();
        initCustomerBox();
    }

    @FXML
    private void deleteCustomer(ActionEvent event) {
        String message = CustomerManager.deleteCustomer(selectedCustomer);
        CustomerManager.saveCustomers();
        initCustomerBox();
        setErrorMessage(message);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCustomerBox();
        initTransactionTypeBox();
    }

    private void clearError() {
        errorLabel.setText("");
    }

    private void setErrorMessage(String message) {
        errorLabel.setText(message);
    }

    private void initTransactionWalletBox(Customer customer) {
        walletList.setItems(customer.getWalletNames());
        walletList.getSelectionModel().selectFirst(); //select the first element
        selectWallet(null);
    }

    private void initCustomerBox() {
        if (CustomerManager.getCustomerNames() != null && CustomerManager.getCustomerNames().size() > 0) {
            customerList.setItems(CustomerManager.getCustomerNames());
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
