package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Member class.
 */
public class Member {
  private String firstName;
  private String lastName;
  private String personalNo;
  private String memberid;
  private List<Boat> boats;

  /**
   * constructor.
   */
  public Member(String firstName, String lastName, String personalNo, String memberid) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.personalNo = personalNo;
    this.memberid = memberid;
    this.boats = new ArrayList<>();
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPersonalNo() {
    return this.personalNo;
  }

  public void setPersonalNo(String personalNumber) {
    this.personalNo = personalNumber;
  }

  public String getMemberId() {
    return this.memberid;
  }

  public void setMemberId(String memberId) {
    this.memberid = memberId;
  }

  public List<Boat> getBoats() {
    return this.boats;
  }

  public void addBoat(Boat b) {
    boats.add(b);
  }

  public void removeBoat(Boat b) {
    boats.remove(b);
  }

}
