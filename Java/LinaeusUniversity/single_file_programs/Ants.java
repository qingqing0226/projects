package assignment1;

import java.util.Random;

public class Ants {
    enum Direction{
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
    public static void main(String[] args) {
        Random rnd = new Random();
        System.out.println("Ants");
        System.out.println("=====\n");
        /*
        chessboard size: 8 x 8
        ant location: (x, y)
        top-left: (0, 0)
        bottom-right: (7, 7)
        initialize chessBoard with false
        the boolean will be used to test if a square has been visited
        */
        int x, y;
        boolean[][] chessBoard = new boolean[8][8];
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                chessBoard[row][col] = false;
            }
        }
        Direction[] directions = Direction.values();
        int noOfSteps; // the number of steps for each movement
        int stepsSum = 0; // the number of steps for all simulations
        int stepsForOneSimulation; // the number of steps for one simulation 

        // Start of simulations
        for(int i = 1; i < 11; i++){
            // random start location (x, y)
            x = rnd.nextInt(8);
            y = rnd.nextInt(8);
            chessBoard[y][x] = true;  // The first square that has been visited
            int squares = 0;  // the number of squares that have been visited
            stepsForOneSimulation = 0;
            boolean running = true;
            while(running){     
                // pick a random direction          
                int randomIndex = rnd.nextInt(4);
                Direction direction = directions[randomIndex];
                // a random number of steps
                noOfSteps = rnd.nextInt(7) + 1;
                // current position: (initialX, initialY)
                int initialX = x;
                int initialY = y;
                squares = 0;
                if(direction == Direction.UP){
                    y -= noOfSteps;
                    if(y < 0){
                        y = 0;
                        stepsForOneSimulation += initialY;
                        for(int j = 0; j < initialY; j++){
                            chessBoard[j][initialX] = true;
                        }
                    } else {
                        stepsForOneSimulation += noOfSteps;
                        for(int j = y; j < initialY; j++){
                            chessBoard[j][initialX]= true;
                        }
                    }    
                } else if(direction == Direction.DOWN){
                    y += noOfSteps;
                    if(y > 7){
                        y = 7;
                        stepsForOneSimulation += (7 - initialY);
                        for(int j = initialY; j < 8; j++){
                            chessBoard[j][initialX] = true;
                        }
                    } else{
                        stepsForOneSimulation += noOfSteps;
                        for(int j = initialY + 1; j < y+1; j++){
                            chessBoard[j][initialX] = true;
                        }
                    }
                } else if(direction == Direction.LEFT){
                    x -= noOfSteps;
                    if(x < 0){
                        x = 0;
                        stepsForOneSimulation += initialX;
                        for(int j = 0; j < initialX; j++){
                            chessBoard[initialY][j] = true;
                        }
                    } else{
                        stepsForOneSimulation += noOfSteps;
                        for(int j = x; j < initialX; j++){
                            chessBoard[initialY][j] = true;
                        }
                    }
                } else{
                    x += noOfSteps;
                    if(x > 7){
                        x = 7;
                        stepsForOneSimulation += (7 - initialX);
                        for(int j = initialX + 1; j < 8; j++){
                            chessBoard[initialY][j] = true;
                        }
                    } else{
                        stepsForOneSimulation += noOfSteps;
                        for(int j = initialX + 1; j < x + 1; j++){
                            chessBoard[initialY][j] = true;
                        }
                    }
                }
                // Check if all squares have been visited
                for(int row = 0; row < 8 ; row++){
                    for(int col = 0; col < 8; col++){
                        if(chessBoard[row][col]){
                            squares++;
                        }
                    }
                }
                /*  When all squares have been visited,
                    assign false to all elements in chessBoard
                    and stop while loop
                */
                if(squares == 64){
                    for(int row = 0; row < 8; row++){
                        for(int col = 0; col < 8; col++){
                            chessBoard[row][col] = false;
                        }
                    }
                    running = false;
                }
            }
            System.out.println("Number of steps in simulation " + i + ": " + stepsForOneSimulation );
            stepsSum += stepsForOneSimulation;
        }
        // End of simulations
        System.out.println("Average amount of steps: " + (stepsSum/10.0));
    }
}
