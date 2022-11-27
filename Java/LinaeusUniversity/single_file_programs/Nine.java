package assignment1;
import java.util.Scanner;
import java.util.Random;
public class Nine {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        Random rnd = new Random();
        int my_num1 = 0;
        int my_num2 = 0;
        int com_num1 = 0;
        int com_num2 = 0;

        System.out.println("Playing a game");
        System.out.println("=================\n\n");
        System.out.print("Ready to play? (Y/N) ");
        String choice = reader.next().toUpperCase();
        if(choice.equals("Y")){
            my_num1 = rnd.nextInt(6) + 1;
            System.out.println("You rolled " + my_num1);
            System.out.print("Would you like to roll again? (Y/N) ");
            choice = reader.next().toUpperCase();
            if(choice.equals("Y")){
                my_num2 = rnd.nextInt(6) + 1;
                System.out.println("You rolled " + my_num2 + " and in total you have " + (my_num1+my_num2));
                com_num1 = rnd.nextInt(6) + 1;
                System.out.println("The computer rolled " + com_num1);
                com_num2 = rnd.nextInt(6) + 1;
                System.out.println("The computer rolls again and gets " + com_num2 + " in total " + (com_num1+com_num2));
            } else if(choice.equals("N")){
                com_num1 = rnd.nextInt(6) + 1;
                System.out.println("The computer rolled " + com_num1);
                if(com_num1 <= 4){
                    com_num2 = rnd.nextInt(6) + 1;
                    System.out.println("The computer rolls again and gets " + com_num2 + " in total " + (com_num1+com_num2));
                    my_num2 = rnd.nextInt(6) + 1;
                    System.out.println("In the second round you rolled " + my_num2 + " and in total you have " + (my_num1+my_num2));
                } else{
                    my_num2 = rnd.nextInt(6) + 1;
                    System.out.println("You rolled " + my_num2 + " and in total you have " + (my_num1+my_num2));
                    com_num2 = rnd.nextInt(6) + 1;
                    System.out.println("The computer rolls again and gets " + com_num2 + " in total " + (com_num1+com_num2));
                }
            } else{
                System.out.println("Invalid input");
            }
        } else{
            System.out.println("Game ended");
        }
        int my_sum = my_num1 + my_num2;
        int com_sum = com_num1 + com_num2;
        if(my_sum >= 10 && com_sum < 10){
            System.out.println("You are flat.");
            System.out.println("The computer won!");
        }
        if(com_sum >= 10 && my_sum < 10){
            System.out.println("The computer is flat.");
            System.out.println("You won!");
        }
        if(my_sum >= 10 && com_sum >= 10){
            System.out.println("You and the computer are flat.");
            System.out.println("Nobody won.");
        }
        if(my_sum < 10 && com_sum <10){
            if(9-my_sum < 9-com_sum){
                System.out.println("You won!");
            } else if(9-my_sum > 9-com_sum){
                System.out.println("The computer won!");
            } else if(my_sum != 0 && com_sum != 0) {
                System.out.println("A tie!");
            }
        }
        reader.close();
    }
}
