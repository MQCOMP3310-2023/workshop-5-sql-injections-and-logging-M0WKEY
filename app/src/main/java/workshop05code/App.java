package workshop05code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
//Included for the logging exercise
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 *
 * @author sqlitetutorial.net
 */
public class App {
    // Start code for logging exercise
    static {
        // must set before the Logger
        // loads logging.properties from the classpath
        try {// resources\logging.properties
            LogManager.getLogManager().readConfiguration(new FileInputStream("resources/logging.properties"));
        } catch (SecurityException | IOException e1) {
            e1.printStackTrace();
        }
    }

    private static final Logger logger = Logger.getLogger(App.class.getName());
    // End code for logging exercise
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SQLiteConnectionManager wordleDatabaseConnection = new SQLiteConnectionManager("words.db");

        wordleDatabaseConnection.createNewDatabase("words.db");
        if (wordleDatabaseConnection.checkIfConnectionDefined()) {
            System.out.println("Wordle created and connected.");
        } else {
            System.out.println("Not able to connect. Sorry!");
            return;
        }
        if (wordleDatabaseConnection.createWordleTables()) {
            System.out.println("Wordle structures in place.");
        } else {
            System.out.println("Not able to launch. Sorry!");
            return;
        }

        // let's add some words to valid 4 letter words from the data.txt file

        try (BufferedReader br = new BufferedReader(new FileReader("resources/data.txt"))) {
            String line;
            int i = 1;
            while ((line = br.readLine()) != null) {
                // this prints out everyword in the list 
                // System.out.println(line);
                wordleDatabaseConnection.addValidWord(i, line);
                i++;
            }

        } catch (IOException e) {
            System.out.println("Not able to load . Sorry!");
            System.out.println(e.getMessage());
            return;
        }

        // let's get them to enter a word

        // Create a new scanner object to read user input from the console
    try (Scanner scanner = new Scanner(System.in)) {
    String guess = "";

    // Start a loop that runs until the user enters 'q' to quit
    while (!guess.equals("q")) {
        // Prompt the user to enter a 4-letter string or 'q' to quit
        System.out.print("Enter a 4 letter word for a guess or q to quit: ");
        guess = scanner.nextLine();

        // Check if the input string is a valid 4-letter lowercase string
        // https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/match
        if (!guess.matches("^[a-z]{4}$")) {

            logger.log(Level.SEVERE, "Invalid guess: " + guess); // log invalid guess at a severe level
            // If not, print an error message and continue with the loop
            System.out.println("Sorry, that input is not acceptable. Please enter a 4-letter string that consists only of lowercase letters a-z.\n");
            continue;
        }
        // If the input string is valid, print the guess and check if it is a valid word
        System.out.println("You've guessed '" + guess+"'.");

        if (wordleDatabaseConnection.isValidWord(guess)) { 
            // If it is a valid word, print a success message
            System.out.println("Success! It is in the the list.\n");
        } else {
            // If it is not a valid word, print a failure message
            System.out.println("Sorry. This word is NOT in the the list.\n");
        }
    }
        } catch (NoSuchElementException | IllegalStateException e) {
            // Catch any exceptions that may occur while reading input from the scanner
            logger.log(Level.WARNING, "Exception occurred while reading input from scanner.", e);
}
        }
    }
}