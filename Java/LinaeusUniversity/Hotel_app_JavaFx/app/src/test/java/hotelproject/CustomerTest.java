/*
 * Customer test.
 * These test cases are by default run by using junit 5
 * To use junit 4, you need to replace some import statements 
 * with equivalent junit 4 statements.
 */
package hotelproject;

import org.junit.Test; // junit 5
// same as import org.junit.Test;  // junit 4

import hotelproject.Customer.PaymentMethod;

//import static org.junit.jupiter.api.Assertions.assertEquals;  // junit 5

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerTest {
  // fill information necessary for mysql to work properly;
  String dbUser = "";
  String dbPassword = "";
  String dbPort = "";

  @Test
  // methods: getname()
  // it tests: whether the name is being retrieved correctly
  public void customerNameGet() {
    Customer c = new Customer("Hello", "address", "phoneNo", PaymentMethod.MasterCard);
    assertEquals("Name not retrieved correctly.", c.getName(), "Hello");
  }

  @Test
  // methods: setname()
  // it tests: whether the name is being set correctly
  public void customerNameSet() throws SQLException {
    dbHandeler db = new dbHandeler(dbUser, dbPassword, dbPort);
    try {
      db.initilizeDatabase();
    } catch (ClassNotFoundException err) {
      err.printStackTrace();
    } catch (SQLException err) {
      err.printStackTrace();
    }

    Customer c = new Customer("Pineapple", "Hawaii", "1234", PaymentMethod.Mobile);
    c.setName("Purple");
    assertEquals("Name not set correctly.", c.getName(), "Purple");
  }

  @Test
  // methods: getAddress()
  // it tests: whether the address is being retrieved correctly
  public void customerAddressGet() throws SQLException {
    Customer c = new Customer("Hello", "address", "phoneNo", PaymentMethod.MasterCard);
    assertEquals("address not retrieved correctly.", c.getAddress(), "address");
  }

  @Test
  // methods: getAddress()
  // it tests: whether the address is being set correctly
  public void customerAddressSet() throws SQLException {
    dbHandeler db = new dbHandeler(dbUser, dbPassword, dbPort);
    try {
      db.initilizeDatabase();
    } catch (ClassNotFoundException err) {
      err.printStackTrace();
    } catch (SQLException err) {
      err.printStackTrace();
    }

    Customer c = new Customer("Pineapple", "Hawaii", "1234", PaymentMethod.Mobile);
    c.setAddress("Narnia");
    assertEquals("address not set correctly.", c.getAddress(), "Narnia");
  }

  @Test
  // methods: getAddress()
  // it tests: whether the address is being set correctly
  public void customerPhoneGet() throws SQLException {
    Customer c = new Customer("Hello", "address", "phoneNo", PaymentMethod.MasterCard);
    assertEquals("Phone Number not retrieved correctly.", c.getPhoneNo(), "phoneNo");
  }

  @Test
  // methods: getAddress()
  // it tests: whether the address is being set correctly
  public void customerPhoneSet() throws SQLException {
    dbHandeler db = new dbHandeler(dbUser, dbPassword, dbPort);
    try {
      db.initilizeDatabase();
    } catch (ClassNotFoundException err) {
      err.printStackTrace();
    } catch (SQLException err) {
      err.printStackTrace();
    }

    Customer c = new Customer("Pineapple", "Hawaii", "1234", PaymentMethod.Mobile);
    c.setPhoneNo("123");
    assertEquals("Phone Number not set correctly", c.getPhoneNo(), "123");
  }

  @Test
  // methods: getAddress()
  // it tests: whether the address is being set correctly
  public void customerPaymentSet() throws SQLException {
    dbHandeler db = new dbHandeler(dbUser, dbPassword, dbPort);
    try {
      db.initilizeDatabase();
    } catch (ClassNotFoundException err) {
      err.printStackTrace();
    } catch (SQLException err) {
      err.printStackTrace();
    }

    Customer c = new Customer("Pineapple", "Hawaii", "1234", PaymentMethod.Mobile);
    c.setPaymentMethod(PaymentMethod.Cash);
    assertEquals("Payment method not set correctly ", c.getPaymentMethod(), PaymentMethod.Cash);
  }

  @Test
  // methods: getAddress()
  // it tests: whether the address is being set correctly
  public void customerPaymentGet() throws SQLException {
    Customer c = new Customer("Hello", "address", "phoneNo", PaymentMethod.MasterCard);
    assertEquals("Payment method not retrieved correctly ", c.getPaymentMethod(), PaymentMethod.MasterCard);
  }

  @Test
  // methods: getAddress()
  // it tests: whether the address is being set correctly
  public void customerDetails() throws SQLException {
    dbHandeler db = new dbHandeler(dbUser, dbPassword, dbPort);
    try {
      db.initilizeDatabase();
    } catch (ClassNotFoundException err) {
      err.printStackTrace();
    } catch (SQLException err) {
      err.printStackTrace();
    }

    Customer c = new Customer("Pineapple", "Hawaii", "1234", PaymentMethod.Mobile);
    List<String> customerDetail = new ArrayList<String>();
    customerDetail.add(c.getPhoneNo());
    customerDetail.add(c.getName());
    customerDetail.add(c.getAddress());
    customerDetail.add(c.getStringpaymentMethod());
    assertEquals("Customer details not set correctly ", c.customerDetail(), customerDetail);
  }

  @Test
  // methods: getAddress()
  // it tests: whether the address is being set correctly
  public void customerPayment() throws SQLException {
    Customer c = new Customer("Hello", "address", "phoneNo", PaymentMethod.MasterCard);
    assertEquals("Payment method not retrieved correctly ", c.getStringpaymentMethod(), "MasterCard");
  }

  @Test
  // methods: getAddress()
  // it tests: whether the address is being set correctly
  public void customerPaymentNone() throws SQLException {
    Customer c = new Customer("Hello", "address", "phoneNo", PaymentMethod.MasterCard);
    c.setPaymentMethodToNone();
    assertEquals("Payment method not retrieved correctly ", c.getStringpaymentMethod(), "NULL");
  }

}
