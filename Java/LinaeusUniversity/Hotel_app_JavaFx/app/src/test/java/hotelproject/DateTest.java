/*
 * Date test.
 * These test cases are by default run by using junit 5
 * To use junit 4, you need to replace some import statements 
 * with equivalent junit 4 statements.
 */
package hotelproject;

import org.junit.Test; // junit 5
// same as import org.junit.Test;  // junit 4

//import static org.junit.jupiter.api.Assertions.assertEquals;  // junit 5

import static org.junit.Assert.assertEquals;

public class DateTest {
  
  @Test
  // methods: getYear()
  // it tests: whether the year is being retrieved correctly
  public void dateYearGet() {
    Date d = new Date(2021, 5, 6);
    assertEquals("Year not retrieved correctly.", d.getYear(), 2021);
  }

  @Test
  // methods: setYear()
  // it tests: whether the year is being set correctly
  public void dateYearSet() {
    Date d = new Date(2021, 5, 6);
    d.setYear(2022);
    assertEquals("Year not set correctly.", d.getYear(), 2022);
  }

  @Test
  // methods: getMonth()
  // it tests: whether the Month is being retrieved correctly
  public void dateMonthGet() {
    Date d = new Date(2021, 5, 6);
    assertEquals("Month not retrieved correctly.", d.getMonth(), 5);
  }

  @Test
  // methods: setMonth()
  // it tests: whether the Month is being set correctly
  public void dateMonthSet() {
    Date d = new Date(2021, 5, 6);
    d.setMonth(7);
    assertEquals("Month not set correctly.", d.getMonth(), 7);
  }

  @Test
  // methods: getDayOfMonth()
  // it tests: whether the Day is being retrieved correctly
  public void dateDayOfMonthGet() {
    Date d = new Date(2021, 5, 6);
    assertEquals("day not retrieved correctly.", d.getDayOfMonth(), 6);
  }

  @Test
  // methods: setDayOfMonth()
  // it tests: whether the Day is being set correctly
  public void dateDayOfMonthSet() {
    Date d = new Date(2021, 5, 6);
    d.setDayOfMonth(7);
    assertEquals("day not set correctly.", d.getDayOfMonth(), 7);
  }

  @Test
  // methods: toString()
  // it tests: whether the date can format to string correctly
  public void dateToString() {
    Date d = new Date(2021, 5, 6);
    assertEquals("date can not format to a string.", d.toString(), "2021/5/6");
  }

  @Test
  // methods: biggerThan()
  // it tests: whether to date can be compare correctly
  public void dateBiggerThan() {
    Date d1 = new Date(2021, 5, 6);
    Date d2 = new Date(2021, 7, 6);
    assertEquals("date can not be compare correctly.", d1.biggerThan(d2), false);
    assertEquals("date can not be compare correctly.", d2.biggerThan(d1), true);
  }
}
