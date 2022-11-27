package assignment1;

import java.util.Random;
import java.util.Scanner;

public class GameSRP {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Random rnd = new Random();
        int player_score = 0;
        int computer_score = 0;
        int draw = 0;
        int choice = -1;
        while(choice != 0){
            System.out.print("Scissor (1), rock (2), paper (3) or 0 to quit: ");
            choice = scan.nextInt();
            int computer_number = rnd.nextInt(3) + 1;
            if(choice == 1){
                if(computer_number == 1){
                    System.out.println("It's a draw!");
                    draw++;
                } else if(computer_number == 2){
                    System.out.println("You lost, computer had rock!");
                    computer_score++;
                } else{
                    System.out.println("You won, computer had paper!");
                    player_score++;
                }
            } else if(choice == 2){
                if(computer_number == 1){
                    System.out.println("You won, computer had scissors!");
                    player_score++;
                } else if(computer_number == 2){
                    System.out.println("It's a draw!");
                    draw++;
                } else{
                    System.out.println("You lost, computer had paper!");
                    computer_score++;
                }
            } else if(choice == 3){
                if(computer_number == 1){
                    System.out.println("You lost, computer had scissors!");
                    computer_score++;
                } else if(computer_number == 2){
                    System.out.println("You won, computer had rock!");
                    player_score++;
                } else{
                    System.out.println("It's a draw!");
                    draw++;
                }
            }
        }
        scan.close();
        System.out.println("Score: " + player_score + " (you) " + computer_score + " (computer) " + draw + " (draw).");
    }
}
