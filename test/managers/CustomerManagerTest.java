/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import domain.Customer;
import domain.Transaction;
import domain.TransactionType;
import domain.TransactionWallet;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Bas de Vaan
 */
public class CustomerManagerTest {

    @Before
    public void initTests() {
        ArrayList<Customer> customers = new ArrayList<>();
        customers.add(new Customer("Harry", "Bladzijde 40 van het boekje", "Winkeltegoed"));
        customers.add(new Customer("Peter", "Bladzijde 41 van het boekje", "Kaartcrediet"));

        CustomerManager.setCustomers(customers);
    }

    @Test
    public void addCustomer() {
        assertEquals("Has one customer from initialization",
                2,
                CustomerManager.getCustomers().size());

        Customer willem = CustomerManager.addCustomer(new Customer("Willem",
                "Heeft een bestelling liggen achter de balie",
                "Winkeltegoed"));

        assertEquals("Has one extra customer from the addition",
                3,
                CustomerManager.getCustomers().size());
        assertTrue("Willem is created",
                willem != null);

        Customer willemClone = CustomerManager.addCustomer(new Customer("Willem",
                "We gaan willem nog eens toevoegen",
                "Winkeltegoed"));

        assertEquals("Willem is not added again",
                3,
                CustomerManager.getCustomers().size());
        assertTrue("Willem2 is created",
                willemClone == null);

    }

    @Test
    public void addWalletToCustomer() {
        Customer harry = CustomerManager.getCustomerWithName("Harry");
        assertEquals("Harry has one wallet",
                1,
                harry.getWallets().size());
        CustomerManager.addWalletToCustomer(harry, "Magic inruil");
        assertEquals("Harry has an extra wallet",
                2,
                harry.getWallets().size());

        CustomerManager.addWalletToCustomer(harry, "Winkeltegoed");
        assertEquals("Harry already had a Winkeltegoed wallet",
                2,
                harry.getWallets().size());

    }

    @Test
    public void deleteCustomer() {
        Customer harry = CustomerManager.getCustomerWithName("Harry");
        TransactionManager.addTransaction(harry, "Winkeltegoed", new Transaction(new BigDecimal(9.99), TransactionType.WINNINGS));
        assertEquals("Harry's balance is ten euros",
                harry.getWalletWithName("Winkeltegoed").getBalance(),
                new BigDecimal(9.99));

        String message = CustomerManager.deleteCustomer(harry);

        assertTrue("Harry is still in the system",
                CustomerManager.getCustomerWithName("Harry") != null);
        assertEquals("The message is that Harry cannot be deleted",
                "Harry still has balance on his account!",
                message);

        TransactionManager.addTransaction(harry, "Winkeltegoed", new Transaction(new BigDecimal(-9.99), TransactionType.SALE_CARDS));

        message = CustomerManager.deleteCustomer(harry);
        assertTrue("Harry has been deleted",
                CustomerManager.getCustomerWithName("Harry") == null);
        assertEquals("The message is that harry is deleted",
                "Harry has been deleted",
                message);

    }

    @Test
    public void checkIfJsonIsLoaded() {
        Customer harry = CustomerManager.getCustomerWithName("Harry");
        TransactionManager.addTransaction(harry, "Winkeltegoed", new Transaction(new BigDecimal(10), TransactionType.WINNINGS));

        assertEquals("Has one customer from initialization",
                2,
                CustomerManager.getCustomers().size());

        harry.setName("trolololololololol");

        assertTrue("trolololololololol is in the system",
                CustomerManager.getCustomerWithName("trolololololololol") != null);

        CustomerManager.loadCustomers();
        assertEquals("Has one customer from initialization",
                2,
                CustomerManager.getCustomers().size());
        assertTrue("trolololololololol is not the system",
                CustomerManager.getCustomerWithName("trolololololololol") == null);
        assertTrue("Harry is in the system",
                CustomerManager.getCustomerWithName("Harry") != null);

    }

}
