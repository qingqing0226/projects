package assignment1;

import java.util.Scanner;

public class Diamonds {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String stars;
        String spaces;
        String line;
        int noOfStars;
        int noOfSpaces;
        int number;

        do{
            System.out.print("Give a positive number: ");
            while(!scan.hasNextInt()){
                String input = scan.next();
                System.out.println(input + " is an invalid input");
            }
            number = scan.nextInt();
            System.out.println();
            // print the diamond
            // upper half 
            for(int i = 1; i < number+1; i++){
                noOfStars =  i * 2 - 1;
                noOfSpaces = number * 2 - 1 - noOfStars;
                stars = "*".repeat(noOfStars);
                if(noOfSpaces == 0){
                    spaces = "";
                } else{
                    spaces = " ".repeat(noOfSpaces/2); //half spaces
                }
                line = spaces + stars + spaces;
                System.out.println(line);
            }
            // lower half
            int noOfStarsInMiddle = number*2-1;
            for(int j = 1; j < number; j++){
                noOfSpaces = j*2;
                noOfStars = noOfStarsInMiddle - noOfSpaces;
                stars = "*".repeat(noOfStars);
                spaces = " ".repeat(noOfSpaces/2); // half spaces
                line = spaces + stars + spaces;
                System.out.println(line);
            }
        } while(number>0);
        scan.close();
    }
}
