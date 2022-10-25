package hotelproject;

public class ReceptionStaff extends AllUser {

  public ReceptionStaff(String userName, String password, Boolean alreadyHash) {
    super(userName, password, alreadyHash);
    this.accessLevel = AccessLevel.Staff;
  }

  public ReceptionStaff(String userName, String password, String firstName, String lastName, Boolean alreadyHash) {
    super(userName, password, firstName, lastName, alreadyHash);
    this.accessLevel = AccessLevel.Staff;
  }
}
