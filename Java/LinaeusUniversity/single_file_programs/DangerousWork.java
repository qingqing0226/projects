package assignment1;

import java.util.Scanner;

public class DangerousWork {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        double sum = 0.01;
        int days = 0;
        System.out.print("How much would you like to earn? ");
        int money = scan.nextInt();
        while(sum < money){
            sum *= 2;
            days++;
        }
        System.out.println("You will have your money in " + days + " days.");
        scan.close();
    }
}
