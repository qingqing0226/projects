package model;

import java.time.LocalDate;

/**
 * Boat class.
 */
public class Boat {
  /**
   * boat types enum.
   */
  public enum Type {
    SAILBOAT, MOTORSAILER, CANOE, OTHER
  }

  private LocalDate registrationDate;
  private Type type;
  private double length;

  /**
   * constructor.
   */
  public Boat(LocalDate registrationDate, Type type, double length) {
    this.registrationDate = registrationDate;
    this.type = type;
    this.length = length;
  }

  public LocalDate getRegistrationDate() {
    return this.registrationDate;
  }

  public void setRegistrationDate(LocalDate registrationDate) {
    this.registrationDate = registrationDate;
  }

  public Type getType() {
    return this.type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public double getLength() {
    return this.length;
  }

  public void setLength(double length) {
    this.length = length;
  }

}
