package hotelproject;

public class Date {
  private int year; // XXXX
  private int month; // 1-12
  private int dayOfMonth; // 1-31

  public Date(int y, int m, int d) {
    year = y;
    month = m;
    dayOfMonth = d;
  }

  public int getYear() {
    return this.year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getMonth() {
    return this.month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getDayOfMonth() {
    return this.dayOfMonth;
  }

  public void setDayOfMonth(int dayOfMonth) {
    this.dayOfMonth = dayOfMonth;
  }

  public String toString() {
    String s = this.year + "/" + this.month + "/" + this.dayOfMonth;
    return s;
  }

  public Boolean biggerThan(Date date) {
    if (this.year <= date.year) {
      if (this.month <= date.month) {
        if (this.dayOfMonth <= date.dayOfMonth) {
          return false;
        }
      }
    }
    return true;
  }
}
