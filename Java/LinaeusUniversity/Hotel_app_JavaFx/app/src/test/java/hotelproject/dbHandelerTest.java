/*
 * dbHandeler test.
 * These test cases are by default run by using junit 5
 * To use junit 4, you need to replace some import statements 
 * with equivalent junit 4 statements.
 */
package hotelproject;

import org.junit.jupiter.api.Test; // junit 5
// same as import org.junit.Test;  // junit 4

import static org.junit.jupiter.api.Assertions.assertEquals; // junit 5
import static org.junit.jupiter.api.Assertions.assertTrue;

import hotelproject.User.AccessLevel;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class dbHandelerTest {
  // fill information necessary for mysql to work properly;
  String dbUser = "";
  String dbPassword = "";
  String dbPort = "";

  // methods: Alluser.deleteUser(), dbHandeler.deleteUserDetail()
  // it tests if a user is deleted from the database
  @Test
  public void testDeleteUserDetail() throws SQLException {
    dbHandeler db = new dbHandeler(dbUser, dbPassword, dbPort);
    try {
      db.initilizeDatabase();
    } catch (ClassNotFoundException err) {
      err.printStackTrace();
    } catch (SQLException err) {
      err.printStackTrace();
    }
    ReceptionStaff staff = new ReceptionStaff("userName", "password", "firstName", "lastName", false);
    dbHandeler.addUserDetail(staff);
    assertTrue(dbHandeler.deleteUserDetail(staff));
  }

  @Test
  public void testForCreate() throws SQLException, ClassNotFoundException {
    // you need to add assert statements here:
    DummyDbHandeler ddbh = new DummyDbHandeler();
    System.out.println("database connect well");
    ddbh.initilizeDatabase();
    System.out.println("database can be initilize.");
    ddbh.deleteDb();
    ddbh.closeDb();
  }

  @Test
  public void testForfunctionsAboutUser() throws SQLException, ClassNotFoundException {
    DummyDbHandeler ddbh = new DummyDbHandeler();
    ddbh.initilizeDatabase();

    User u = new Administrator("databasetest", "databasetest", "databasetest", "databasetest", false);
    User u_origin = new Administrator("databasetest", Secure.hash("databasetest"), "databasetest", "databasetest",
        false);
    dbHandeler.addUserDetail(u);
    User u_database = dbHandeler.getUserDetail("databasetest");
    // assertEquals(u_origin.userDetail(), u_database.userDetail(), "not get right
    // user");

    User u_change = new ReceptionStaff("databasetest", "databasetest_n", "databasetest_n", "databasetest_n", false);
    u_origin = new ReceptionStaff("databasetest", Secure.hash("databasetest_n"), "databasetest_n", "databasetest_n",
        false);
    dbHandeler.changeUserDetail(u_change);
    u_database = dbHandeler.getUserDetail("databasetest");
    // assertEquals(u_origin.userDetail(), u_database.userDetail(), "after change,
    // not get right user");

    Boolean f = dbHandeler.deleteUserDetail(u);
    Boolean s = dbHandeler.deleteUserDetail(u);
    assertEquals(true, f, "delete user not work");
    assertEquals(false, s, "delete a user twice is not right");

    ddbh.deleteDb();
    ddbh.closeDb();
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
