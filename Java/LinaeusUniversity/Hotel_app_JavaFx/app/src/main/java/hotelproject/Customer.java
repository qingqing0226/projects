package hotelproject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Customer {
  private String name;
  private String address;
  private String phoneNo; // not sure if it should be a string or an int

  public enum PaymentMethod {
    Mobile, MasterCard, Visa, Cash, None,
  }

  protected PaymentMethod paymentMethod;

  public Customer(String name, String address, String phoneNo, PaymentMethod paymentMethod) {
    this.name = name;
    this.address = address;
    this.phoneNo = phoneNo;
    this.paymentMethod = paymentMethod;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) throws SQLException {
    this.name = name;
    dbHandeler.changeCustomerDetail(this);
  }

  public String getAddress() {
    return this.address;
  }

  public void setAddress(String address) throws SQLException {
    this.address = address;
    dbHandeler.changeCustomerDetail(this);
  }

  public String getPhoneNo() {
    return this.phoneNo;
  }

  public void setPhoneNo(String phoneNo) throws SQLException {
    dbHandeler.deleteCustomerDetail(this);
    this.phoneNo = phoneNo;
    dbHandeler.addCustomer(this);
  }

  public PaymentMethod getPaymentMethod() {
    return this.paymentMethod;
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) throws SQLException {
    this.paymentMethod = paymentMethod;
    dbHandeler.changeCustomerDetail(this);
  }

  public List<String> customerDetail() {
    List<String> customerDetail = new ArrayList<String>();
    customerDetail.add(this.phoneNo);
    customerDetail.add(this.name);
    customerDetail.add(this.address);
    customerDetail.add(this.getStringpaymentMethod());
    return customerDetail;
  }

  public String getStringpaymentMethod() {
    PaymentMethod u = this.paymentMethod;
    String paymentMethod = "NULL";
    switch (u) {
      case Mobile:
        paymentMethod = "Mobile";
        break;
      case MasterCard:
        paymentMethod = "MasterCard";
        break;
      case Visa:
        paymentMethod = "Visa";
        break;
      case Cash:
        paymentMethod = "Cash";
        break;
      default:
        break;
    }
    return paymentMethod;
  }

  public void setPaymentMethodToNone() {
    this.paymentMethod = PaymentMethod.None;
  }
}
