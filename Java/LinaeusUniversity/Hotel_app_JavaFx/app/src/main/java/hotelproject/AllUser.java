package hotelproject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AllUser implements User {
  private String userName = null;
  private String password = null;
  private String firstName = "NULL";
  private String lastName = "NULL";
  protected AccessLevel accessLevel;

  public AllUser(String userName, String password, Boolean alreadyHash) {
    this.userName = userName;
    if (alreadyHash) {
      this.password = password;
    } else {
      this.password = Secure.hash(password);
    }
    this.accessLevel = AccessLevel.None;
  }

  public AllUser(String userName, String password, String firstName, String lastName, Boolean alreadyHash) {
    this.userName = userName;
    if (alreadyHash) {
      this.password = password;
    } else {
      this.password = Secure.hash(password);
    }
    this.firstName = firstName;
    this.lastName = lastName;
    this.accessLevel = AccessLevel.None;
  }

  public String getUserName() {
    return this.userName;
  }

  public String getPassword() {
    return this.password;
  }

  public AccessLevel getAccessLevel() {
    return this.accessLevel;
  }

  public String getStringAccessLevel() {
    AccessLevel u = this.accessLevel;
    String accessLevel = "NULL";
    switch (u) {
      case Admin:
        accessLevel = "Admin";
        break;
      case Staff:
        accessLevel = "Staff";
        break;
      default:
        break;
    }
    return accessLevel;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public String setUserName(String input) {
    try {
      dbHandeler.deleteUserDetail(this);
    } catch (SQLException e1) {
      e1.printStackTrace();
    }
    this.userName = input;
    try {
      dbHandeler.addUserDetail(this);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return this.userName;
  }

  public String setPassword(String input) {
    this.password = Secure.hash(input);
    try {
      dbHandeler.changeUserDetail(this);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return this.password;
  }

  public AccessLevel setAccessLevel(AccessLevel input) {
    this.accessLevel = input;
    try {
      dbHandeler.changeUserDetail(this);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return this.accessLevel;
  }

  public void setFirstName(String input) {
    this.firstName = input;
    try {
      dbHandeler.changeUserDetail(this);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void setLastName(String input) {
    this.lastName = input;
    try {
      dbHandeler.changeUserDetail(this);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<String> userDetail() {
    List<String> userDetail = new ArrayList<String>();
    userDetail.add(this.userName);
    userDetail.add(this.password);
    userDetail.add(this.getStringAccessLevel());
    userDetail.add(this.firstName);
    userDetail.add(this.lastName);
    return userDetail;
  }

  public void setAccessToNone() {
    this.accessLevel = AccessLevel.None;
  }

  public void deleteUser() {
    try {
      dbHandeler.deleteUserDetail(this);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
