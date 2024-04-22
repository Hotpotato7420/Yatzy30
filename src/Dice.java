package Yatzy30;

import java.util.Random;


// Den här filen är för att bygga upp tärningen! Kör Player filen

public class Dice {
    private int value; // Initializes the value
    private Random random; // Initialized the random function that I use for the dicerolls.


    // Used for later using the dice in my code.
    public Dice(){
        random = new Random(); // Makes a new random for each time I roll the dice.
    }


    // Rolls the dice and sets the value
    public void roll(){
        value = random.nextInt(6) + 1; // sets the value to between (0-5) + 1
    }


    // Gets the value of the dice
    public int getValue(){
        return value;
    }
}
