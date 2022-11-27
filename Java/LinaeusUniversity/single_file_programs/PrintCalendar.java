package assignment1;

import java.util.ArrayList;
import java.util.Scanner;

public class PrintCalendar {
    public static String months(int month){
        String[] monthArray = {"January", "February", "March", 
        "April", "May", "June", "July", 
        "August", "September", "October", 
        "November", "December"};
        return monthArray[month-1];
    }
    public static boolean isLeapYear(int year){
        return (year%100 != 0 && year%4 == 0) || year%400 == 0;
    }
    // takes boolean leap, returns an array of days of month
    public static int[] daysOfMonth(boolean leap){
        int daysOfFeb = leap? 29:28;
        int[] days = {0, 31, daysOfFeb, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        return days;
    }
    // takes an int year, returns the day of the week on January 1
    // Monday - 1, Tuesday - 2,......, Sunday - 7
    public static int dayOfWeekJan1(int year){
        int dayOfWeek;
        int leapYear = 0;
        int commonYear = 0;
        if(year == 1800){
            dayOfWeek = 3;  // Wednesday, January 1, 1800
        } else{
            for(int y = 1800; y < year; y++){
                if(isLeapYear(y)){
                    leapYear++;
                } else{
                    commonYear++;
                }
            }
            int noOfDays = leapYear*366 + commonYear*365;
            dayOfWeek = (noOfDays - 5)%7 + 1;
        }
        return dayOfWeek;
    }
    /* 
        int firstDayOfYear: the day of week of January 1st (1-7)
        int month: 1-12
        boolean leap: leap year
        returns the day of week of the first day of the month (1-7)
    */
    public static int firstDayOfMonth(int firstDayOfYear, int month, boolean leap){
        int firstDay;
        int[] daysOfM = daysOfMonth(leap);
        int noOfDays = 0;
        if(month == 1){
            firstDay = firstDayOfYear;
        } else{
            for(int m = 1; m < month; m++){
                noOfDays += daysOfM[m];
            }
            firstDay = (noOfDays - (7 - firstDayOfYear + 1))%7 + 1;
        }
        return firstDay;
    }
    /*
        int firstDayOfM: day of week of first day of the month (1-7)
        int month: 1-12
        boolean leap: leap year
        returns a string ArrayList containing day numbers(1-31)
    */
    public static ArrayList<String> makeCalendar(int firstDayOfM, int month, boolean leap){
        int dayNum = 1;
        int noOfDays = daysOfMonth(leap)[month]; // 28/29, 30 or 31 days
        ArrayList<String> table = new ArrayList<>();
        for(int d = 0; d < firstDayOfM-1; d++){
            table.add(" ");     // empty spaces before the first day
        }
        for(int d = 0; d < noOfDays; d++){
            table.add(Integer.toString(dayNum));
            dayNum++;
        }
        return table;
    }
    /*
        takes the string ArrayList from previous method
        prints the calendar
     */
    public static void printCalendar(ArrayList<String> table){
        int count = 0;
        System.out.println(" Mon Tue Wed Thu Fri Sat Sun");
        for(String s: table){
            System.out.printf("%4s", s);
            count++;
            if(count%7 == 0){
                System.out.println();
            }
        }
    }
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter a year after 1800: ");
        int year = scan.nextInt();
        System.out.print("Enter a month (1-12): ");
        int month = scan.nextInt();
        System.out.println(months(month) + " " + year );
        System.out.println("-".repeat(29));
        // January 1, 1800: Wednesday
        int janFirst = dayOfWeekJan1(year);  // day of week of January 1st
        boolean isLeap = isLeapYear(year);
        // day of week of the first day of the month:
        int firstDayOfMon = firstDayOfMonth(janFirst, month, isLeap);
        ArrayList<String> calendar = makeCalendar(firstDayOfMon, month, isLeap);
        printCalendar(calendar);
        scan.close();
    }
}
