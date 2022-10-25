package hotelproject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//import javafx.scene.control.Button;

import javafx.scene.control.Button;

public class Room {
  /* types of bed */

  public enum Bed {
    SINGLE, DOUBLE, EXTRA_LARGE,
  }

  public enum RoomDetails {
    NUMBER, PRICE, ADDBEDS, REMOVEDEDS, SIZE, LOCATION, None
  }

  private int number;
  private double price;
  private String bedsString = "";
  private double size; // square metres
  private String location;
  private String comment = "";
  private Button SelectButton = new Button("Select");
  private List<Bed> beds; // there might be more than one beds in a room

  public Room(int number, double price, double size, String location, String comment) {
    this.number = number;
    this.price = price;
    this.size = size;
    this.location = location;
    this.comment = comment;
    beds = new ArrayList<>();
  }

  public Room(int number, double price, String bedStr, double size, String location, String comment, Button Select) {
    this.number = number;
    this.price = price;
    this.bedsString = bedStr;
    this.size = size;
    this.location = location;
    this.comment = comment;
    this.SelectButton = Select;
  }

  public void setNumber(int number) throws SQLException {
    dbHandeler.removeRoom(this);
    this.number = number;
    dbHandeler.addRoom(this);
  }

  public int getNumber() {
    return this.number;
  }

  public double getPrice() {
    return this.price;
  }

  public void setPrice(double price) throws SQLException {
    this.price = price;
    dbHandeler.editRoomDetail(this);
  }

  public List<Bed> getBeds() {
    return this.beds;
  }

  public void addBed(Bed b) throws SQLException {
    beds.add(b);
    dbHandeler.editRoomDetail(this);
  }

  public void bedsToString() {
    for (Bed b : beds) {
      if (b == Bed.SINGLE) {
        this.bedsString += "Single ";
      } else if (b == Bed.DOUBLE) {
        this.bedsString += "Double ";
      } else if (b == Bed.EXTRA_LARGE) {
        this.bedsString += "Extra_large ";
      }
    }
  }

  public String getBedsString() {
    return this.bedsString;
  }

  public void removeBed(Bed b) throws SQLException {
    beds.remove(b);
    dbHandeler.editRoomDetail(this);
  }

  public double getSize() {
    return this.size;
  }

  public void setSize(double size) throws SQLException {
    this.size = size;
    dbHandeler.editRoomDetail(this);
  }

  public String getLocation() {
    return this.location;
  }

  public void setLocation(String location) throws SQLException {
    this.location = location;
    dbHandeler.editRoomDetail(this);
  }

  public String getComment() {
    return this.comment;
  }

  public void setComment(String comment) throws SQLException {
    this.comment = comment;
    dbHandeler.editRoomDetail(this);
  }

  public void setButton() throws SQLException {
    this.SelectButton = new Button();
    SelectButton.setText(String.valueOf(this.number));
  }

  public Button getButton() {
    return this.SelectButton;
  }

  public void multipleBeds(String s) throws SQLException {
    String[] bedStrings = s.split(" ");
    for (String i : bedStrings) {
      if (i.equalsIgnoreCase("SINGLE")) {
        this.addBed(Bed.SINGLE);
      } else if (i.equalsIgnoreCase("DOUBLE")) {
        this.addBed(Bed.DOUBLE);
      } else if (i.equalsIgnoreCase("EXTRA_LARGE")) {
        this.addBed(Bed.EXTRA_LARGE);
      }
    }
  }
}
