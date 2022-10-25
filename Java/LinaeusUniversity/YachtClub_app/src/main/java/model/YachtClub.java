package model;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import model.Boat.Type;

/**
 * YachtClub class.
 */
public class YachtClub {
  private List<Member> members;
  private Persistence data = new Data();

  public YachtClub() {
    members = data.getMembers();
  }

  /**
   * delete a boat.
   */
  public void deleteBoat(Boat b, Member member) {
    member.removeBoat(b);
  }

  /**
   * change a boat's information.
   */
  public void changeBoat(Boat b, LocalDate date, Type type, double length) {
    b.setRegistrationDate(date);
    b.setType(type);
    b.setLength(length);
  }

  /**
   * create a member with her/his info.
   */
  public void createMember(String firstname, String lastname, String personalNo) {
    Member m = new Member(firstname, lastname, personalNo, generateUniqueId());
    members.add(m);
  }

  /**
   * this method generates an alphanumeric id of length 6.
   */
  public String generateUniqueId() {
    String possibleValues = "0123456789" + "abcdefghijklmnopqrstuvwxyz" 
        + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String id = "";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 6; i++) {
      Random r = new Random();
      int index = r.nextInt(62);
      sb.append(possibleValues.charAt(index));
    }
    id = sb.toString();

    return id;
  }

  /**
   * add a member.
   */
  public void addMember(Member m) {
    members.add(m);
  }

  /**
   * delete a member.
   */
  public void deleteMember(Member member) {
    members.remove(member);
  }

  /**
   * change a member's information.
   */
  public void changeMemberInfo(Member member, String firstname, String lastname, 
      String personalNo, String memberid) {
    members.remove(member);
    member = new Member(firstname, lastname, personalNo, memberid);
    members.add(member);
  }

  /**
   * retrieve a member.
   */
  public Member getMember(String memberId) {
    for (Member member : members) {
      if (member.getMemberId().equals(memberId)) {
        return member;
      }
    }
    return null;
  }

  /**
   * register a boat for a member.
   */
  public void registerBoat(Boat b, Member member) {
    member.addBoat(b);
  }

  /**
   * get a copy of the member list.
   */
  public List<Member> getMembers() {
    List<Member> copy = this.members;
    return copy;
  }

}
