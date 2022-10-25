/*
 * AllUser test.
 * These test cases are by default run by using junit 5
 * To use junit 4, you need to replace some import statements 
 * with equivalent junit 4 statements.
 */
package hotelproject;

import org.junit.jupiter.api.Test; // junit 5
// same as import org.junit.Test;  // junit 4

import static org.junit.jupiter.api.Assertions.assertEquals; // junit 5

import hotelproject.User.AccessLevel;

import java.sql.SQLException;

public class AllUserTest {
  // NOTE!
  // AllUser is an abstract class which cannot be instantiated
  // therefore, we cannot test it directly
  // instead, we can do it by testing its subclasses,namely, ReceptionStaff &
  // Administrator

  // fill information necessary for mysql to work properly;
  String dbUser = "";
  String dbPassword = "";
  String dbPort = "";

  @Test
  public void testCeateNewUser() throws SQLException {
    String u = "abc";
    String p = "def";
    AccessLevel t = AccessLevel.Staff;
    DummyUser user = new DummyUser(u, p);
    assertEquals(u, user.getUserName(), "user name not set correctly.");
    // NOTE that getPassword() will return hash instead of real password,
    // assertEquals will always fail
    // assertEquals(p, user.getPassword(), "password not set correctly.");
    assertEquals(t, user.getAccessLevel(), "user type not set correctly.");
  }

  @Test
  public void testReSetUserNameAndPassword() throws SQLException, ClassNotFoundException {
    DummyDbHandeler ddbh = new DummyDbHandeler();
    ddbh.initilizeDatabase();

    String u = "abc";
    String p = "def";
    String nu = "newname";
    String np = "newpassword";
    AccessLevel nt = AccessLevel.Admin;

    DummyUser user = new DummyUser(u, p);
    user.setUserName(nu);
    user.setPassword(np);
    user.setAccessLevel(nt);

    assertEquals(nu, user.getUserName(), "user name not re-set correctly(origin).");
    assertEquals(nu, dbHandeler.getUserDetail(user.getUserName()).getUserName(),
        "user name not re-set correctly(database).");
    // assertEquals(np, user.getPassword(), "password not re-set
    // correctly(origin).");
    assertEquals(Secure.hash(np), dbHandeler.getUserDetail(user.getUserName()).getPassword(),
        "password not re-set correctly(database).");
    assertEquals(nt, user.getAccessLevel(), "accessLevel not re-set correctly(origin).");
    assertEquals(nt, dbHandeler.getUserDetail(user.getUserName()).getAccessLevel(),
        "accessLevel not re-set correctly(database).");

    ddbh.deleteDb();
    ddbh.closeDb();
  }

  class DummyUser extends AllUser {
    DummyUser(String userName, String password) throws SQLException {
      super(userName, password, false);
      this.accessLevel = AccessLevel.Staff;
    }
  }

  class DummyDbHandeler extends dbHandeler {

    public DummyDbHandeler() throws SQLException {
      super(dbUser, dbPassword, dbPort);
    }

    public void deleteDb() throws SQLException {
      String s = "DROP DATABASE hotel;";
      stmt.execute(s);
    }
  }
}
