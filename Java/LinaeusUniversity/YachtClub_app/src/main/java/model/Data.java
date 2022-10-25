package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Boat.Type;

/**
 * Data class contains the hard-coded data of members and their boats.
 */
public class Data implements Persistence {

  @Override
  public List<Member> getMembers() {
    List<Member> members = new ArrayList<>();

    Member m = new Member("Elias", "Hanna", "12345678", "2e3h4s");
    LocalDate date = LocalDate.of(2021, 3, 28);
    Boat b = new Boat(date, Type.SAILBOAT, 25.5);
    m.addBoat(b);
    members.add(m);

    m = new Member("Qingqing", "Dai", "87654321", "1q2q3d");
    date = LocalDate.of(2021, 5, 15);
    b = new Boat(date, Type.MOTORSAILER, 30);
    m.addBoat(b);
    members.add(m);

    m = new Member("Tobias", "Ohlsson", "22222222", "9t8o7s");
    date = LocalDate.of(2020, 8, 8);
    b = new Boat(date, Type.CANOE, 17.8);
    m.addBoat(b);
    b = new Boat(date, Type.MOTORSAILER, 36);
    m.addBoat(b);
    members.add(m);

    return members;
  }

}
