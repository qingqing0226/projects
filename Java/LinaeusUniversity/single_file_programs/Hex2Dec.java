package assignment1;

import java.util.Scanner;

public class Hex2Dec {
    // main method hexToDecimal
    public static int hexToDecimal(String hex){
        hex = hex.toLowerCase();
        char[] characters = hex.toCharArray();
        int decimal = 0;
        for (int i = 0; i < characters.length; i++) {
            decimal += (charToNumber(characters[i]) * Math.pow(16, characters.length-1-i));
        }
        return decimal;
    }
    // help method charToNumber takes a character and returns a decimal number
    public static int charToNumber(char c){
        int number;
        if(Character.isDigit(c)){
            number = Character.getNumericValue(c);
        } else if(c == 'a'){
            number = 10;
        } else{
            number = Character.getNumericValue(c) - Character.getNumericValue('a') + 10;
        }
        return number;
    }
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter a hex number: ");
        String hex = scan.next();
        int decimalValue = hexToDecimal(hex);
        System.out.printf("The decimal value for %s is %d.", hex, decimalValue);
        scan.close();
    }
}
