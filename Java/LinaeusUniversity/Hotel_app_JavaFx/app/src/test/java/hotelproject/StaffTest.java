/*
 * Staff test.
 * These test cases are by default run by using junit 5
 * To use junit 4, you need to replace some import statements 
 * with equivalent junit 4 statements.
 */
package hotelproject;

//import org.junit.jupiter.api.Test;  // junit 5
import org.junit.Test; // junit 4

//import static org.junit.jupiter.api.Assertions.assertEquals;  // junit 5

import hotelproject.User.AccessLevel;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StaffTest {
  // method: Constructor1
  // it tests if the accessLevel is correctly set
  @Test
  public void testReceptionStaffConstructor1() {
    AccessLevel t = AccessLevel.Staff;
    ReceptionStaff staff = new ReceptionStaff("u", "p", false);
    assertEquals("u", staff.getUserName(), "user name is not set correctly");
    assertEquals(Secure.hash("p"), staff.getPassword(), "password is not correctly set");
    assertEquals(t, staff.getAccessLevel(), "user type not set correctly.");
  }

  // method: Constructor2
  // it tests if the accessLevel is correctly set
  @Test
  public void testReceptionStaffConstructor2() {
    AccessLevel t = AccessLevel.Staff;
    ReceptionStaff staff = new ReceptionStaff("u", "p", "firstname", "lastname", false);
    assertEquals(t, staff.getAccessLevel(), "user type not set correctly.");
    assertEquals("u", staff.getUserName(), "user name is not set correctly");
    assertEquals(Secure.hash("p"), staff.getPassword(), "password is not correctly set");
    assertEquals("firstname", staff.getFirstName(), "firstname is not set correctly");
    assertEquals("lastname", staff.getLastName(), "lastname is not set correctly");
  }

  // method: getStringAccessLevel()
  // it tests if the access level is correctly set
  @Test
  public void testGetStringAccessLevel() {
    String s = "Staff";
    ReceptionStaff staff = new ReceptionStaff("u", "p", false);
    assertEquals(s, staff.getStringAccessLevel(), "user type not set correctly.");
  }

  // method: userDetail()
  // it tests if a list strings of user details are obtained
  @Test
  public void testUserDetail() {
    ReceptionStaff staff = new ReceptionStaff("u", "p", "firstname", "lastname", false);
    List<String> details = staff.userDetail();
    assertEquals("u", details.get(0), "user name is not set correctly");
    assertEquals(Secure.hash("p"), details.get(1), "password is not set correctly");
    assertEquals("Staff", details.get(2), "access level is not set correctly");
    assertEquals("firstname", details.get(3), "firstname is not set correctly");
    assertEquals("lastname", details.get(4), "lastname is not set correctly");
  }

  @Test
  public void isAStaff() throws SQLException {
    AccessLevel t = AccessLevel.Staff;
    DummyUser user = new DummyUser("u", "p");
    assertEquals(t, user.getAccessLevel(), "user type not set correctly.");
  }

  class DummyUser extends ReceptionStaff {
    DummyUser(String userName, String password) throws SQLException {
      super(userName, password, false);
    }
  }
}
