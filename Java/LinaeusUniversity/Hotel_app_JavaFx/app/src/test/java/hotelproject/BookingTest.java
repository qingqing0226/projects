/*
 * Booking test.
 * These test cases are by default run by using junit 5
 * To use junit 4, you need to replace some import statements 
 * with equivalent junit 4 statements.
 */
package hotelproject;

import org.junit.jupiter.api.Test; // junit 5
// same as import org.junit.Test;  // junit 4

import static org.junit.jupiter.api.Assertions.assertEquals; // junit 5
import static org.junit.jupiter.api.Assertions.assertTrue;

import hotelproject.Customer.PaymentMethod;
import hotelproject.User.AccessLevel;

import java.sql.SQLException;

public class BookingTest {
  // fill information necessary for mysql to work properly;
  String dbUser = "";
  String dbPassword = "";
  String dbPort = "";

  // method: first constructor
  // it tests if a Booking object can be created correctly
  @Test
  public void testBookingConstructor1() {
    Room room = new Room(12, 1000.0, 50.0, "third floor", "beach view");
    Customer customer = new Customer("Shoeb", "Dhaka", "1111", PaymentMethod.MasterCard);
    Date start = new Date(2021, 5, 13);
    Date end = new Date(2021, 5, 15);
    Booking booking = new Booking(room, customer, start, end, true);
    assertEquals(room.getNumber(), booking.getRoom().getNumber(), "room is not set correctly");
    assertEquals(customer.getPhoneNo(), booking.getCustomer().getPhoneNo(), "customer is not set correctly");
    assertEquals(start, booking.getStartDate(), "start date is not set correctly");
    assertEquals(end, booking.getEndDate(), "end date is not set correctly");
    assertTrue(booking.isPaid());
  }

  // method: second constructor
  // it tests if a Booking object can be created correctly
  @Test
  public void testBookingConstructor2() {
    Room room = new Room(12, 1000.0, 50.0, "third floor", "beach view");
    Customer customer = new Customer("Shoeb", "Dhaka", "1111", PaymentMethod.MasterCard);
    Date start = new Date(2021, 5, 13);
    Date end = new Date(2021, 5, 15);
    Booking booking = new Booking(10, room, customer, start, end, true);
    // evaluate the booking number
    assertEquals(10, booking.getBookingNumber(), "booking number is not set correctly");
  }

  // method: third constructor
  // it tests if a Booking object can be created correctly
  @Test
  public void testBookingConstructor3() {
    Room room = new Room(12, 1000.0, 50.0, "third floor", "beach view");
    Customer customer = new Customer("Shoeb", "Dhaka", "1111", PaymentMethod.MasterCard);
    Date start = new Date(2021, 5, 13);
    Date end = new Date(2021, 5, 15);
    Booking booking = new Booking(10, room, customer, start, end, true, 2000.0);
    // evaluates the total price
    assertEquals(2000.0, booking.getTotalPrice(), "total price is not set correctly");
  }

  // method: setBookingNumber()
  // it tests if the setter for booking number works
  @Test
  public void testSetBookingNumber() throws SQLException {
    dbHandeler db = new dbHandeler(dbUser, dbPassword, dbPort);
    try {
      db.initilizeDatabase();
    } catch (ClassNotFoundException err) {
      err.printStackTrace();
    } catch (SQLException err) {
      err.printStackTrace();
    }

    Room room = new Room(12, 1000.0, 50.0, "third floor", "beach view");
    Customer customer = new Customer("Shoeb", "Dhaka", "1111", PaymentMethod.MasterCard);
    Date start = new Date(2021, 5, 13);
    Date end = new Date(2021, 5, 15);
    Booking booking = new Booking(room, customer, start, end, true);
    dbHandeler.addBookingDetail(booking);
    booking.setBookingNumber(333);
    // tests if the booking number is updated
    assertEquals(333, booking.getBookingNumber(), "booking number is not set correctly");
  }

}
