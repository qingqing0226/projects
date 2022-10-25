package hotelproject;

import java.sql.SQLException;

public class Booking {
  private int bookingNumber;
  private Room room;
  private Customer customer;
  private Date startDate;
  private Date endDate;
  private boolean paid;
  private double totalPrice;

  public Booking(Room room, Customer customer, Date startDate, Date endDate, boolean paid) {
    this.room = room;
    this.customer = customer;
    this.startDate = startDate;
    this.endDate = endDate;
    this.paid = paid;
  }

  public Booking(int bookingNumber, Room room, Customer customer, Date startDate, Date endDate, boolean paid) {
    this.bookingNumber = bookingNumber;
    this.room = room;
    this.customer = customer;
    this.startDate = startDate;
    this.endDate = endDate;
    this.paid = paid;
  }

  public Booking(int bookingNumber, Room room, Customer customer, Date startDate, Date endDate, boolean paid,
      double totalPrice) {
    this.bookingNumber = bookingNumber;
    this.room = room;
    this.customer = customer;
    this.startDate = startDate;
    this.endDate = endDate;
    this.paid = paid;
    this.totalPrice = totalPrice;
  }

  public int getBookingNumber() {
    return this.bookingNumber;
  }

  public void setBookingNumber(int bookingNumber) throws SQLException {
    dbHandeler.deleteBookingDetail(this);
    this.bookingNumber = bookingNumber;
    dbHandeler.addBookingDetail(this);
  }

  public Room getRoom() {
    return this.room;
  }

  public void setRoom(Room room) throws SQLException {
    this.room = room;
    dbHandeler.changeBookingDetail(this);
  }

  public Customer getCustomer() {
    return this.customer;
  }

  public void setCustomer(Customer customer) throws SQLException {
    this.customer = customer;
    dbHandeler.changeBookingDetail(this);
  }

  public Date getStartDate() {
    return this.startDate;
  }

  public void setStartDate(Date startDate) throws SQLException {
    this.startDate = startDate;
    dbHandeler.changeBookingDetail(this);
  }

  public Date getEndDate() {
    return this.endDate;
  }

  public void setEndDate(Date endDate) throws SQLException {
    this.endDate = endDate;
    dbHandeler.changeBookingDetail(this);
  }

  public boolean isPaid() {
    return this.paid;
  }

  public void setPaid(boolean paid) throws SQLException {
    this.paid = paid;
    dbHandeler.changeBookingDetail(this);
  }

  public void bookRoom(Room room, Date dateBegin, Date dateEnd) {
    // TODO:some thing about book a room. no booking db and room db now.
  }

  public String isPaidString() {
    String s;
    if (this.paid == true) {
      s = "Paid";
    } else {
      s = "Not Paid";
    }
    return s;
  }

  public double getTotalPrice() {
    return this.totalPrice;
  }

  public void setTotalPrice(double p) {
    this.totalPrice = p;
  }
}
