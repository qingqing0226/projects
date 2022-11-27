package assignment1;

import java.util.Scanner;

public class DayOfWeek {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int year = 0;
        int month = 0;
        int dayOfMonth = 0;
        int dayOfWeek;
        String[] daysOfWeek = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        System.out.println("Day of Week (enter 0 for year to quit)");
        do{
            System.out.print("Enter year: ");
            year = scan.nextInt();
            if(year > 0){
                System.out.print("Enter month (1-12): ");
                month = scan.nextInt();
                if(month == 1){
                    month = 13;
                    year--;
                }
                if(month == 2){
                    month = 14;
                    year--;
                }
                System.out.print("Enter day of the month (1-31): ");
                dayOfMonth = scan.nextInt();
                // formula
                int yearOfCentury = year%100;
                int j = year/100;
                dayOfWeek = (dayOfMonth + 26*(month+1)/10 + yearOfCentury + yearOfCentury/4 + j/4 + 5*j)%7;
                System.out.println("Day of week is " + daysOfWeek[dayOfWeek] + "\n");
            }
        } while(year > 0);
        scan.close();
    }
}
