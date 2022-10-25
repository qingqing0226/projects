package hotelproject;

/**
 * A User class with extra privilage that can modify and delete rooms along with
 * other additional functionalities.
 */
public class Administrator extends AllUser {

  /**
   * Constructer
   * 
   * @throws SQLException
   */
  public Administrator(String userName, String password, Boolean alreadyHash) {
    super(userName, password, alreadyHash);
    this.accessLevel = AccessLevel.Admin;
  }

  public Administrator(String userName, String password, String firstName, String lastName, Boolean alreadyHash) {
    super(userName, password, firstName, lastName, alreadyHash);
    this.accessLevel = AccessLevel.Admin;
  }
}
