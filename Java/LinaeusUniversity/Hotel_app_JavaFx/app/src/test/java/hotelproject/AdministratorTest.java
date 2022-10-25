/*
 * Administrator test.
 */
package hotelproject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import hotelproject.User.AccessLevel;

public class AdministratorTest {
  // fill information necessary for mysql to work properly;
  String dbUser = "";
  String dbPassword = "";
  String dbPort = "";

  // method: first constructor
  // it tests if the Administrator object can be created successfully
  @Test
  public void testAdministratorConstructor1() {
    AccessLevel admin = AccessLevel.Admin;
    Administrator administrator = new Administrator("username", "password", false);
    assertEquals("username", administrator.getUserName(), "username is not set correctly");
    assertEquals(Secure.hash("password"), administrator.getPassword(), "password is not set correctly");
    assertEquals(admin, administrator.getAccessLevel(), "access level is not set correctly");
  }

  // method: second constructor
  // it tests if the Administrator object can be created successfully
  @Test
  public void testAdministratorConstructor2() {
    AccessLevel admin = AccessLevel.Admin;
    Administrator administrator = new Administrator("username", "password", "firstname", "lastname", false);
    assertEquals("username", administrator.getUserName(), "username is not set correctly");
    assertEquals(Secure.hash("password"), administrator.getPassword(), "password is not set correctly");
    assertEquals(admin, administrator.getAccessLevel(), "access level is not set correctly");
    assertEquals("firstname", administrator.getFirstName(), "firstname is not set correctly");
    assertEquals("lastname", administrator.getLastName(), "lastname is not set correctly");
  }

  // method: setUserName(), setPassword(), setAccessLevel(), setFirstname(),
  // setLastname()
  // it tests all setters
  @Test
  public void testSetters() {
    dbHandeler db = new dbHandeler(dbUser, dbPassword, dbPort);
    try {
      db.initilizeDatabase();
    } catch (ClassNotFoundException err) {
      err.printStackTrace();
    } catch (SQLException err) {
      err.printStackTrace();
    }

    AccessLevel staff = AccessLevel.Staff;
    Administrator administrator = new Administrator("username", "password", "firstname", "lastname", false);
    administrator.setUserName("another user");
    administrator.setPassword("1234");
    administrator.setFirstName("Hello");
    administrator.setLastName("world");
    administrator.setAccessLevel(staff);
    assertEquals("another user", administrator.getUserName(), "username is not set correctly");
    assertEquals(Secure.hash("1234"), administrator.getPassword(), "password is not set correctly");
    assertEquals(staff, administrator.getAccessLevel(), "access level is not set correctly");
    assertEquals("Hello", administrator.getFirstName(), "firstname is not set correctly");
    assertEquals("world", administrator.getLastName(), "lastname is not set correctly");
  }

}
