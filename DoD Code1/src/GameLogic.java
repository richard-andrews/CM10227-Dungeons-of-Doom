/*import necessary libraries*/
import java.awt.Point;
import java.util.Random;

/**
 * Contains the main logic part of the game, as it processes.
 */
public class GameLogic {
    /*create objects*/
	private Map map1;
	private  HumanPlayer player1;
	private Bot bot1;
	private Random random;

	 /*instantiate global variables*/
    private boolean playerQuit;
    private boolean playerInRangeLastTurn;


    /**
     * The default constructor for the GameLogic class, this initialises both the global variables and objects with
     * values
     */
	public GameLogic() {
	    /*initialises objects using the respective constructors*/
        map1 = new Map();
		player1 = new HumanPlayer();
		bot1 = new Bot();
		random = new Random();

		/* initialise global variables with values*/
		playerQuit = false;
		playerInRangeLastTurn = false;
	}


    /**
     * A method that finds the character (tile) at a specific position in the map by indexing array lists. If exception
     * occurs as index is outside bounds of the array, a wall tile is returned.
     *
     * @param xPos integer - the integer value of the 'column' that needs to be indexed
     * @param yPos integer - the integer value of the 'row' that needs to be indexed
     * @return tile char - the char value at the specified position on the map
     */
    public String getTileValue(int xPos, int yPos){
        String tile;

        /*try to get the character at the position specified by the co ordinate parameters
        if that fails, the co ordinates are out of bounds so it it represented as a wall # */
        try{
            tile = String.valueOf(map1.mapList.get(yPos).get(xPos));
        }
        catch(Exception e) {
            tile = "#";
        }

        return tile;
    }


    /**
	 * A method that checks if the game should still be running. Checks if the bot has captured the player by
     * comparing x and y co-ordinates of both. Checks if the player has enter 'quit' while on top of an Exit tile.
	 *
     * @return if the game is running.
     */
    public boolean gameRunning() {
        /*assume end conditions have not been met yet*/
        boolean endGame = false;

        /*check if the bot is sharing a position with the player ie the bot has caught the player
        check if the player has quit the game*/
        if ((player1.position.x == bot1.position.x)&&(player1.position.y == bot1.position.y)){
            System.out.println("Bot captured you, you lose");
            endGame =  true;
        }
        else if (playerQuit){
            endGame = true;
        }

        /*if the end conditions have been met, stop the game from running*/
        return !endGame;
    }


    /**
     * A method that handles the input of the 'hello' command by the user. Returns the amount of gold the user must
     * collect on this map to win the game.
     *
     * @return goldRequired int - An integer storing the amount of gold required to be collected by the player in order
     * for them to win the game. Concatenates prefix string with integer parsed to a string.
     */
    protected String hello() {
        /*define the default prefixed string*/
        String goldRequired = "Gold to win: ";

        /*parse the integer value of the gold required for the current map to a string
        concatenate this string to the end of the prefix string*/
        goldRequired += Integer.toString(map1.goldRequired);
        return goldRequired;
    }


    /**
     * A method that handles the input of the 'gold' command by the user. Returns the amount of gold the user has
     * collected up to that point in the game. Concatenates prefix string with integer parsed to a string.
     *
     * @return goldOwned int - An integer storing the amount of gold the user has collected so far.
     */
    protected String gold() {
        /*define the default prefixed string*/
        String goldOwned = "Gold owned: ";

        /*parse the integer value of the gold collected by the player so far to a string
        concatenate this string to the end of the prefix string*/
        goldOwned += Integer.toString(player1.goldCollected);
        return goldOwned;
    }


    /**
     * A method that handles the input of the 'move x' command by the user, where x is a compass direction. Checks
     * whether the move is legal (is not a movement into a wall), returns the result of this validity test and,
     * if the move is legal, updates the player's location on the map. Assumes move is illegal by default,
     * calls getTileValue method to retrieve the tile at the location the player wishes to move to.
     *
     * @param direction char - A single alphabetic character representing the user's desired direction of movement
     *                  e.g. 'n' is North, 'e' is East etc.
     * @return result String - The result of whether the move was legal or not and therefore whether the player's
     * location was able to be updated or not.
     */
    protected String move(char direction) {
        /*assume that the move is illegal and cannot be done*/
        String result = "FAIL";
        String tileToMoveTo = "";

        /*call a method to retrieve the character at the position that the player wants to move to for all 4 possible
        compass direction movements*/
        switch (direction) {
            case 'n':
                tileToMoveTo = getTileValue(player1.position.x, (player1.position.y - 1));
                break;
            case 'e':
                tileToMoveTo = getTileValue((player1.position.x + 1), player1.position.y);
                break;
            case 's':
                tileToMoveTo = getTileValue(player1.position.x, (player1.position.y + 1));
                break;
            case 'w':
                tileToMoveTo = getTileValue((player1.position.x - 1), player1.position.y);
                break;
        }

        /* checks if the move is legal or not, if the tile the player wants to move to is a wall or not
         if it is legal, change the position of the player on the map to reflect the movement and set the result as
         success to communicate to the player that the movement was legal and so successful*/
        if (!tileToMoveTo.equals("#")) {
            result = "SUCCESS";
            switch (direction) {
                case 'n':
                    player1.position.y--;
                    break;
                case 'e':
                    player1.position.x++;
                    break;
                case 's':
                    player1.position.y++;
                    break;
                case 'w':
                    player1.position.x--;
                    break;
            }
        }
        return result;
    }

    /**
     * A method that handles the input of the 'look' command by the player. Cycles through the tiles with a 2 tile
     * range on all sides of the player to produce 25 tiles including the player's location in the centre. The
     * tiles are added in rows to a string where each row is appended with a '\n' representing a new line. If the tile
     * being looked at is the bot's location, a 'B' will be added in place of the map tile.
     *
     *
     * @return mapAsString String - A string containing 25 characters as well as 5 new line symbols focusing around the
     * player at the middle.
     */
    protected String look() {
        String mapAsString = "";
        String tile = "";
        /*set the range of the look relative from the position of the player at the centre.*/
        int range = 2;

        /*loop through the 25 tiles around the player and add them to a string*/
        for (int yPosition = (-1*range); yPosition <=(range); yPosition++){
            for (int xPosition = (-1 * range); xPosition <= (range); xPosition++){
                tile = getTileValue(player1.position.x + xPosition, player1.position.y + yPosition);

                /*if at the centre, add a P to represent the player's position
                if the tile is occupied by the bot, add a B to represent the bot's position
                otherwise add the tile at that position*/
                if (yPosition == 0 && xPosition == 0){
                    mapAsString += "P";
                }
                else if ((player1.position.x + xPosition == bot1.position.x) && (player1.position.y + yPosition == bot1.position.y)) {
                    mapAsString += "B";
                }
                else{
                    mapAsString += tile;
                }
            }
            /*add a new line character*/
            mapAsString += "\n";
        }
        return mapAsString;
    }


    /**
     * A method that handles the input of the 'pickup' command by the user. Processes the player's pickup command,
     * checks if the player is currently at a tile with a gold, updating the map to replace the 'G' tile with an empty tile and increments the player's gold amount.
     *
     * @return result boolean - The result of whether or not the player was able to pick up a gold in their current
     * location.
     */
    protected String pickup() {
        /*assume the pickup cannot be done*/
        String result = "FAIL";

        /*if the player's current position has a gold on it, replace the gold with an empty tile, increase the gold
        collected by the player and state that the pickup was successful*/
        if (getTileValue(player1.position.x, player1.position.y).equals("G")){
            map1.mapList.get(player1.position.y).set(player1.position.x, '.');
            player1.goldCollected ++;
            result = "SUCCESS";
        }
        return result;
    }

    /**
     * A method that handles the input of the 'quit' command by the user. Checks if the command is legal by checking
     * if the play is at a location with an 'E' tile. If not denies the user command, otherwise, checks if the player
     * has collected the same amount fo gold as is required.
     *
     * @return result String - The result of the player's command, whether it be a win, a loss or an invalid move.
     */
    protected String quitGame() {
        String result;
        /*update global variable to show that player has entered quit command so game should stop running*/
        playerQuit = true;

        /*if the player is on a escape E tile and the player has collected the necessary amount of gold then the player
        has won, otherwise they have lost*/
        if ((getTileValue(player1.position.x, player1.position.y).equals("E"))&&(player1.goldCollected >= map1.goldRequired)) {
            result = "WIN";
        }
        else {
            result = "LOSE";
        }
        return result;
    }


    /**
     * A method that handles the user's inputted command and calls the appropriate function for the command. Assumes
     * the command is invalid, if command is valid, sets the output to the returned result of the method called.
     *
     * @param command String - The processed user input to be sorted to call the appropriate function.
     * @return output String - The result of the method called according to the user's input.
     */
    private String getNextAction(String command) {
        /*assume the command is invalid*/
        String output = "Invalid.";

        /*filter through the possible valid commands, if the inputted command matches then call the appropriate
        function and return the result as a string to be printed*/
        switch (command) {
            case "hello":
                output = hello();
                break;
            case "gold":
                output = gold();
                break;
            /*switch will 'fall' through valid commands until it reaches a 'break' this will make it leave the switch,
            therefore all the valid move commands can use the same action as there is no break statement*/
            case "move n":
            case "move e":
            case "move s":
            case "move w":
                output = move(command.charAt(5));
                break;
            case "look":
                output = look();
                break;
            case "pickup":
                output = pickup();
                break;
            case "quit":
                output = quitGame();
                break;
        }
        return output;
    }

    /**
     * A method that handles the placing of both the human player and the user on the map at the beginning of the game.
     * Assumes that a valid position has not been found yet. Runs a loop that , while the position found is not valid,
     * will generate a pair of random numbers within the range of the dimensions of the chosen map. This potential
     * position is then compared against the map to ensure it is not either a wall or a gold tile or is already the
     * player's position as otherwise this might mean that the bot is placed in the same place as the player and the
     * game is already lost. The potential position does not need to be compared against the bot's position as the
     * player is placed on the map already.
     *
     * @return potentialPosition Point - returns a point variable that holds the valid x and y co ordinates of the
     * position to be assigned to either the bot or the human player.
     */
    public Point placePlayer() {
        /*create a new point object*/
        Point potentialPosition =  new Point();
        /*assume the position found is invalid*/
        boolean validPositionFound = false;

      /*  while the position is invalid, generate a new x within the width of the current map and generate a new y
        within the height of the map*/
        while (!validPositionFound) {
            potentialPosition.x= random.nextInt(map1.mapList.get(1).size());
            potentialPosition.y = random.nextInt(map1.mapList.size());

            /*checks the found position is not occupied by a wall, a gold or the player and if so, state a valid
            position has been found
            does not need to be checked against bot's position as player1's position is set first.*/
            if ((!getTileValue(potentialPosition.x, potentialPosition.y).equals("#")) &&
                    (!getTileValue(potentialPosition.x, potentialPosition.y).equals("G")) &&
                    (potentialPosition != player1.position)){
                validPositionFound = true;
            }
        }
        return potentialPosition;
    }

    /**
     * A method that handles the main game running loop. Calls methods to get the user's choice of map and then read
     * the map into the map arraylist, place the player and the bot on the map before running the main game loop while
     * the game running conditions are true. This loop handles both the player's and the bot's moves in sequence.
      */
    public void runGame() {
        /*get the players map choice and read the map from the text file to the arraylist memory*/
        String mapChoice = map1.getMapChoice();
        map1.readMap(mapChoice);

        /*call the methods to place the bot and the human player*/
        player1.position = placePlayer();
        bot1.position = placePlayer();

        /*first turn is odd number as 1 is odd*/
        boolean oddTurn = false;

        /*run the game loop while the end game conditions are false
        call methods to run the bot's and the player's turns
        toggle the value of the oddTurn variable after each turn (true to false, 1 to 2 - false to true, 2 to 3)*/
        while(gameRunning()){
            playerTurn();
            botTurn(oddTurn);
            oddTurn = !oddTurn;
        }
    }

    /**
     * A method that handles the human players turn. This calls a function that gets the input from the user and passes
     * it to the getNextAction method which sorts it and calls the appropriate method(s) if it is a valid command,
     * otherwise it states it is invalid. The result of either the command method or the invalid command is then
     * printed to the console.
     */
    private void playerTurn(){
        String commandResult;
        String command;

        /*call function to read the user's input*/
        command = player1.getInputFromConsole();

        /*call function to handle the user's input*/
        commandResult = getNextAction(command);

        /*print the result of the command*/
        System.out.println(commandResult);
    }

    /**
     * A method that handles the non human player or bot's turn. The action taken by the bot is dependant on the bot's
     * previous action. If the bot has previously seen the player upon performing a look command the bot will 'chase'
     * player calling the botChaseMove method in the bot object, passing the player's position as well. If the bot has
     * not seen the player yet but it is an odd turn (determined by a counter from the start) the bot will perform
     * a look calling a method in the object. Otherwise the bot will move in a random direction.
     *
      * @param oddTurn boolean - A boolean variable that determines the action of the bot through whether there has
     *                been and odd or even number of turns in the game so far.
     */
    private void botTurn(boolean oddTurn){
        char botDirection;

        /*if bot spotted the player on the last turn, call method to chase the player (move in the direction of the
         player)*/
        if(playerInRangeLastTurn){
            botDirection = bot1.botChaseMove(player1.position);
            botMove(botDirection);
        }
      /*  if the bot has not spotted the player but is an odd numbered turn, call method for the bot to perform the
         look function*/
        else if (oddTurn){
            playerInRangeLastTurn = bot1.botLook(player1.position);
        }
        /*if the bot has not spotted the player and it is an even numbered turn, call the method for the bot to perform
        move in a random direction*/
        else{
            botDirection = bot1.botRandomMove();
            botMove(botDirection);
        }
    }

    /**
     * A method that checks if the desired movement of the bot is legal or not (if the tile to move to is a wall or
     * not) and handles the movement of the bot regardless of whether the movement is random or the bot is
     * chasing the player. Calls the getTileValue method to check if the tile that the bot wants to move to is a wall
     * or not. If it is not a wall the position of the bot on the map will be updated to reflect the bot moving.
     * Otherwise, the value of the variable that keeps track of whether the bot is chasing the player is set to false
     * so the bot will have 'lost' the player and will begin random movement again.
     *
     * @param direction char - a single character representing the compass direction in which the bot wishes to move.
     */
    private void botMove(char direction){
        String tileToMoveTo="";

        /* filter through the possible compass directions and call method to get the tile of that the bot wants to move
        to */
        switch (direction) {
            case 'n':
                tileToMoveTo = getTileValue(bot1.position.x, (bot1.position.y - 1));
                break;
            case 'e':
                tileToMoveTo = getTileValue((bot1.position.x + 1), bot1.position.y);
                break;
            case 's':
                tileToMoveTo = getTileValue(bot1.position.x, (bot1.position.y + 1));
                break;
            case 'w':
                tileToMoveTo = getTileValue((bot1.position.x - 1), bot1.position.y);
                break;
        }

        /* if the tile that the bot wants tot move to is not a wall then update the bot's position to reflect the bot
         moving on the map */
        if (!tileToMoveTo.equals("#")) {
            switch (direction) {
                case 'n':
                    bot1.position.y--;
                    break;
                case 'e':
                    bot1.position.x++;
                    break;
                case 's':
                    bot1.position.y++;
                    break;
                case 'w':
                    bot1.position.x--;
                    break;
            }
        }
        else{
            /*if the tile that the bot wants to move to is a wall # then update the player tracking variable to
             reflect that the bot has lost the player and needs to go back to looking for them and moving randomly*/
            playerInRangeLastTurn = false;
        }
    }

    /**
     * The main method that runs by default when the program starts. Creates a new creates a new instance of the
     * GameLogic class. Once this has been created and the constructor has been run the runGame method is called
     * from here the rest of the program is run and the main game running loop begins.
     *
     * @param args String[] - A default string array parameter that must be passed for the program to run.
     */
	public static void main(String[] args) {
	    /*instantiate game logic object and start game running*/
		GameLogic gl = new GameLogic();
		gl.runGame();
    }
}