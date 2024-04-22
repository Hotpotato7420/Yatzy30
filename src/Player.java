package Yatzy30;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


// MAIN FILEN, KÖR DEN HÄR!!!!!!



public class Player {

    private final List<Integer> savedDiceValues;
    private String name; // The name of the player
    private int score; // The score of the player
    private final List<Integer> diceValues; // List to store values of the rolled dices.

    private static String[] playerNames; // Array for the players names
    private static Player[] players;

    // Used for accessing the player and it's values in other parts of the code
    public Player(String name) {
        this.name = name;
        this.score = 30; // Starting points
        this.diceValues = new ArrayList<>();
        this.savedDiceValues = new ArrayList<>();
    }
    // Access the player name
    public String getName() {
        return name;
    }

    // Access the players score
    public int getScore(){
        return score;
    }

    // Ask for the players names
    public static String[] askForPlayerNames(Scanner scanner, int numPlayers){
        String[] playerNames = new String[numPlayers];

        for (int i = 0; i < numPlayers; i++){ // Loop for choosing a name for each player
            System.out.print("Enter name for player " + (i+1) + ": ");
            playerNames[i] = scanner.nextLine();
        }
        return playerNames;
    }
    public static void initializePlayerNames(String[] names){
        playerNames = names;
        players = new Player[names.length];
        for (int i = 0; i < names.length; i++) {
            players[i] = new Player(names[i]);
        }
    }
    // Returns the amount of players
    public static int getNumPlayers(){
        return playerNames != null ? playerNames.length : 0; // Returns the number of players as long as it's not null, then it returns 0.
    }
    // Used for accessing the players names
    public static String[] getPlayerNames(){
        return playerNames;
    }
    public static Player[] getPlayers(){
        return players;
    }


    // Rolls the dice for the player
    public void rollDice(Dice dice, int dicesLeft){
        diceValues.clear(); // Clear the previous values
        for (int i = 0; i < dicesLeft; i++) {
            dice.roll(); // Roll the actual dice
            diceValues.add(dice.getValue()); // Add the dice to the list
        }
    }

    public void saveDice (List<Integer> dicesToSave){
        for (int value : dicesToSave){
            if (diceValues.contains(value)){
                savedDiceValues.add(value);
                diceValues.remove(Integer.valueOf(value));
            }
            else {
                System.out.println("Invalid dice value: " + value);
            }
        }
        updateTotalSavedDiceValue();

        System.out.println("Your points: " + getTotalSavedDiceValue());
        }
    public void updateTotalSavedDiceValue() {
        int totalSavedDiceValue = 0; // Reset the total value
        for (Integer value : savedDiceValues) {
            totalSavedDiceValue += value; // Calculate the total saved dice value
        }
    }

    // Calculate the total value of all saved dices
    public int getTotalSavedDiceValue(){
        int total = 0;

        for (int i = 0; i < savedDiceValues.size(); i++){
            int value = savedDiceValues.get(i);
            total += value; // Add the total value of the saved dices
        }
        return total;
    }


    public void takeTurn(Scanner scanner, Dice dice){
        savedDiceValues.clear();
        int dicesLeft = 6;
        int totalRounds = 99;

        for (int round = 1; round <= totalRounds; round++){

            // Roll dice
            rollDice(dice, dicesLeft);

            // prints the rolled dices
            System.out.println("\n");
            System.out.println("Player " + name + " rolled: " + diceValues);


            System.out.println("Select which dice you would want to save with spaces inbetween each number (ex. 5 4 1 6)");
            String input = scanner.nextLine();
            String[] parts = input.split(" "); // Make sure that it can take input with spaces between
            List<Integer>dicesToSave = new ArrayList<>();
            for (int i = 0; i < parts.length; i++) { // Goes through your input
                if (!parts[i].isEmpty()){
                    dicesToSave.add(Integer.parseInt(parts[i])); // Save the dices that you input
                }

            }
            dicesLeft -= dicesToSave.size(); // Reduces the amount of dices by how many we saved with.
            System.out.println("Dices left: " + dicesLeft);

            // Save the dices
            saveDice(dicesToSave);

            if (dicesLeft == 0){
                System.out.println("No remaining dices.");
                break;
            }
        }
        updateTotalSavedDiceValue(); // Update the Total of the saved dices
        int totalSavedDices = getTotalSavedDiceValue();

        if (totalSavedDices > 30) { // Enter battle phase if our score is over 30

            int battleNumber = totalSavedDices - 30; // Create variable for the amount that you are gonna roll for
            System.out.println("Your battle number: " + battleNumber);

            dicesLeft = 6;
            int attackDices = 0; // Sets attackDices to 0 at the beginnning of each battle phase
            int totalDamage = 0; // Sets our damage to 0 at beginning of each battle phase
            List<Integer> dicesToSave = new ArrayList<>();

            while (dicesLeft > 0) {
                rollDice(dice, dicesLeft); // Roll the dices
                System.out.println("Player " + name + " rolled: " + diceValues); // Print out the roll
                boolean found = false; // Important boolean, used for breaking out of the loop if we dont find a battleNumber

                for (int value : diceValues){ // Enhanced for-loop
                    if (value == battleNumber){ // Checks if battleNumber is inside the roll.
                        attackDices++; // Adds 1 to the attackDice for each battleNumber found
                        totalDamage += battleNumber; // Adds to the totalDamage which is how much damage we deal.
                        found = true; // Sets found to true to not break the loop.
                    }
                }
                if (!found){ // Breaks the loop if no battle numbers found
                    System.out.println("No battle numbers found");
                    break;
                }
                dicesLeft -= attackDices; // Reduce the dicesLeft by the amount of attackDices found.
            }


            if (totalDamage > 0 ){ // Checks if we have any amount of damage to deal
                System.out.println("You can deal " + totalDamage + " amount of damage. Who do you want to attack? ");
                for (Player player : players){ // Loop through all players
                    System.out.println((player.getName()) + ": " + player.getScore() + " points"); // Print out the names and points of players
                }
                String input = scanner.next();
                for (Player player : players){ // Initialize the players
                    if (input.equalsIgnoreCase(player.getName())){ // Search for the players name
                        System.out.println("Their hp before: " + player.score);
                        player.score-= totalDamage;
                        System.out.println("Their hp after: " + player.score);
                    }
                }

            }
            System.out.println("You deal " + totalDamage + " amount of damage.");
            System.out.println("Ending battle phase");


        }
        else {
            int hpLoss = 30 - totalSavedDices;
            score -= hpLoss; // Reduce the players hp by the amount of score under 30
            System.out.println("Player " + name + " loses " + hpLoss + " hit points.");
            if (score <= 0){ // Eliminate a player if they are 0 or below in score
                System.out.println("Player " + name + " is now eliminated!");
            }
        }
    }



    public static void main (String[] args){
        Scanner scanner = new Scanner(System.in);

        // Ask for amount of players
        System.out.print("Enter number of players: ");
        int numPlayers = scanner.nextInt();
        scanner.nextLine();

        // Ask for the player names
        String[] playerNames = askForPlayerNames(scanner, numPlayers);

        Player.initializePlayerNames(playerNames);

        // Display the players
        System.out.println("\nPlayers:");
        for (int i = 0; i < numPlayers; i++) {
            System.out.println((i + 1) + ". " + players[i].getName());
        }

        // Create a dice
        Dice dice = new Dice();

        int currentPlayerIndex = 0;
        while (true) {
            Player currentPlayer = players[currentPlayerIndex];
            System.out.print("It's " + currentPlayer.getName() + "'s turn.");
            currentPlayer.takeTurn(scanner, dice); // Start turn for the current player

            // Check if the player is dead
            int alivePlayers = 0;
            for (Player player : players) { // Enhanced for-loop
                if (player.getScore() > 0) {
                    alivePlayers++; // Go to the next player
                }
            }
            if (alivePlayers == 1) { // Check if only 1 player is alive
                break; // Breaks if true^
            }
            try {
                Thread.sleep(5000); // Pause for 5 seconds after each round
            } catch (InterruptedException e){
                e.printStackTrace(); // Check for errors
            }
            System.out.println("------------------------------------------------------------------------------------------");
            System.out.println("  **    *   ******  *      *  *******      *****    ******  *    *   **    *   *****     ");
            System.out.println("  * *   *   *         *   *      *         *    *   *    *  *    *   * *   *   *     *   ");
            System.out.println("  *  *  *   ****        *        *         *****    *    *  *    *   *  *  *   *     *   ");
            System.out.println("  *   * *   *         *   *      *         *    *   *    *  *    *   *   * *   *     *   ");
            System.out.println("  *    **   ******   *     *     *         *     *  ******  ******   *    **   ******    ");
            System.out.println("------------------------------------------------------------------------------------------");
            currentPlayerIndex = (currentPlayerIndex + 1) % numPlayers; // Go to next player, loop back if last player
        }

        System.out.println("Game over! Player");
        scanner.close(); // Close the scanner for less memory leaks.
    }

}

