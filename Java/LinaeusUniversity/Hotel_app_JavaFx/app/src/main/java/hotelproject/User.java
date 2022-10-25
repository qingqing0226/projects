package hotelproject;

import java.sql.SQLException;
import java.util.List;

/**
 * interface class User.
 */
public interface User {
  enum AccessLevel {
    Staff, Admin, None
  }

  public String getUserName();

  public String getPassword();

  public AccessLevel getAccessLevel();

  public String getStringAccessLevel();

  public String getFirstName();

  public String getLastName();

  public String setUserName(String input) throws SQLException;

  public String setPassword(String input) throws SQLException;

  public AccessLevel setAccessLevel(AccessLevel input) throws SQLException;

  public void setFirstName(String input) throws SQLException;

  public void setLastName(String input) throws SQLException;

  public List<String> userDetail();

  public void setAccessToNone();

  public void deleteUser();
}
