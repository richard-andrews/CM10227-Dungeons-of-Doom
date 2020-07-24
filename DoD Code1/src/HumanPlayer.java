/* Import the necessary libraries */
import java.util.Scanner;
import java.awt.Point;

/**
 * Runs the game with a human player and contains code needed to read inputs.
 */
public class HumanPlayer {
    /* instantiate global variables*/
    public Point position;
    public int goldCollected;

    /**
     * The default constructor for HumanPlayer. Initialise global variables with values.
     */
    public HumanPlayer() {
        position = new Point(0, 0);
        goldCollected = 0;
    }

    /**
     * Reads player's input from the console. Converts all letter characters to lower case if they are not already
     * so that it is in the correct format.
     *
     * @return command String - A string containing the input the player entered.
     */
    protected String getInputFromConsole() {
        Scanner input = new Scanner(System.in);
        String command = input.nextLine();
        command = command.toLowerCase();

        return command;
    }
}