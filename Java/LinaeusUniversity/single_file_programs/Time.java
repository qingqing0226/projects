package assignment1;
import java.util.Scanner;
public class Time {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.print("Give a number of seconds: ");
        int input = reader.nextInt();
        int hour = input/3600;
        int minute = input%3600/60;
        int second = input - hour*3600 - minute*60;
        System.out.println("This corresponds to: " + hour + " hours, " + minute + " minutes  and " + second + " seconds.");
        reader.close();
    }

}
