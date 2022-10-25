/*
 * Room test.
 * These test cases are by default run by using junit 5
 * To use junit 4, you need to replace some import statements 
 * with equivalent junit 4 statements.
 */
package hotelproject;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test; // junit 5
// same as import org.junit.Test;  // junit 4

import hotelproject.Room.Bed;

//import static org.junit.jupiter.api.Assertions.assertEquals;  // junit 5

public class RoomTest {
  // fill information necessary for mysql to work properly;
  String dbUser = "";
  String dbPassword = "";
  String dbPort = "";

  @Test
  // getNumber(), getPrize(), getBeds(), getBedsString(), getSize(),
  // getLocation(), getComment()
  // test whether all the get method can get things correctly
  public void roomAllGetMethod() {
    ArrayList<Bed> l = new ArrayList<Bed>();
    Room r = new Room(1, 1.2, 2.3, "String location", "String comment");

    assertEquals("number not retrieved correctly.", r.getNumber(), 1);
    assertEquals(1.2, r.getPrice(), 0.0);
    assertEquals("beds not retrieved correctly.", r.getBeds(), l);
    assertEquals("string type beds not retrieved correctly.", r.getBedsString(), "");
    assertEquals(2.3, r.getSize(), 0.0);
    assertEquals("location not retrieved correctly.", r.getLocation(), "String location");
    assertEquals("comment not retrieved correctly.", r.getComment(), "String comment");
  }

  @Test
  // setNumber(), setPrize(), setSize(), setLocation(), setComment()
  // test whether all the set method can set things correctly
  public void roomAllSetMethod() {
    dbHandeler db = new dbHandeler(dbUser, dbPassword, dbPort);
    try {
      db.initilizeDatabase();
    } catch (ClassNotFoundException err) {
      err.printStackTrace();
    } catch (SQLException err) {
      err.printStackTrace();
    }
    Room r = new Room(1, 1.2, 2.3, "String location", "String comment");
    try {
      r.setNumber(4);
      r.setPrice(2.0);
      r.setSize(3.0);
      r.setLocation("location");
      r.setComment("comment");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    assertEquals("number not set correctly.", r.getNumber(), 4);
    assertEquals(2.0, r.getPrice(), 0.0);
    assertEquals(3.0, r.getSize(), 0.0);
    assertEquals("location not set correctly.", r.getLocation(), "location");
    assertEquals("comment not set correctly.", r.getComment(), "comment");
  }

  @Test
  // addBed(), removeBed()
  // test whether bed can be add and remove.
  public void roomAddRemoveBed() {
    dbHandeler db = new dbHandeler(dbUser, dbPassword, dbPort);
    try {
      db.initilizeDatabase();
    } catch (ClassNotFoundException err) {
      err.printStackTrace();
    } catch (SQLException err) {
      err.printStackTrace();
    }
    ArrayList<Bed> l = new ArrayList<Bed>();
    Room r = new Room(90, 1.2, 2.3, "String location", "String comment");
    try {
      r.addBed(Bed.SINGLE);
      l.add(Bed.SINGLE);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    assertEquals("beds not add correctly.", r.getBeds(), l);

    try {
      r.removeBed(Bed.SINGLE);
      l.remove(Bed.SINGLE);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    assertEquals("beds not remove correctly.", r.getBeds(), l);
  }

  @Test
  // bedsToString()
  // test whether beds can be change from a list to string using this method
  public void roomBedToString() {
    dbHandeler db = new dbHandeler(dbUser, dbPassword, dbPort);
    try {
      db.initilizeDatabase();
    } catch (ClassNotFoundException err) {
      err.printStackTrace();
    } catch (SQLException err) {
      err.printStackTrace();
    }
    Room r = new Room(1, 1.2, 2.3, "String location", "String comment");
    try {
      r.addBed(Bed.SINGLE);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    r.bedsToString();
    assertEquals("beds list can not change to string correctly.", r.getBedsString(), "Single ");
  }

  @Test
  // multipleBeds()
  // test whether beds can be change from a string to list using this method
  public void roomMultipleBeds() {
    dbHandeler db = new dbHandeler(dbUser, dbPassword, dbPort);
    try {
      db.initilizeDatabase();
    } catch (ClassNotFoundException err) {
      err.printStackTrace();
    } catch (SQLException err) {
      err.printStackTrace();
    }
    Room r = new Room(90, 1.2, 2.3, "String location", "String comment");
    try {
      r.multipleBeds("Single ");
      r.bedsToString();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    assertEquals("beds string can not change to list correctly.", r.getBedsString(), "Single ");
  }
}
