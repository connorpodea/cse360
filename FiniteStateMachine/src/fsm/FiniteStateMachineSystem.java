package fsm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * <p> Title: Test Floating Point Recognizer Class. </p>
 * 
 * <p> Description: A demonstration main class of the Finite State Machine demonstration application </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2021 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 0.00	2017-08-16	Initial baseline 
 * @version 0.01	2021-12-07	Graceful exit if the input file name is wrong 
 * 
 */

public class FiniteStateMachineSystem {

	/**********
	 * This class roots the execution of the Finite State Machine System.  The application initializes the 
	 * state table from a user specified text file.
	 * 
	 * @param args	The program args are ignored/.
	 */
	public static void main(String[] args) {
		char currentChar = ' ';		// A local variable specifying the value of the current character
		StateTable fsmTable = null;

		System.out.println("Welcome to the Finite State Machine System\n");

		// Associate the system keyboard with a Scanner object
		Scanner keyboard = new Scanner(System.in);
        System.out.print("Please enter the name of the file containing the Finite State Machine description: ");
        
        String fsmFileName = keyboard.nextLine(); 

		try {
			Scanner fileReader = new Scanner(new File(fsmFileName));

			// This statement is used to initialized and load the floating point recognizer into the state table
			fsmTable = new StateTable(fileReader);
			System.out.println(fsmTable);
		}
		catch (FileNotFoundException e) {
			System.out.println("\nThe file <" + fsmFileName + "> can't be found.");
			keyboard.close();						// Display the reason for terminating the loop.
			return;
		}
		// See if there was an error in the provided input
		if (fsmTable.getIsError()) {
			System.out.println("\nThe file <" + fsmFileName + "> contained an error.");
			keyboard.close();						// Display the reason for terminating the loop.
			System.out.println("The error message was: " + fsmTable.getErrorMsg());
			return;
			
		}

		// Initialize the inputLine local variable.
		String inputLine = "";

		// Request input from the user and then read in what the user enters
		System.out.println("Enter a string to see if it is recognized by the FMS.  Enter an empty line to terminate.");

		// As long as there is a next line, read it in... Since the input is the keyboard, this is always true
		while (keyboard.hasNextLine()) {
			inputLine = keyboard.nextLine();		// Fetch the next line
			if (inputLine.length() == 0) {				// If the length of the trimmed line is zero, stop the loop
				System.out.println("\n*** Empty input line detected, the loop stops.");
				keyboard.close();						// Display the reason for terminating the loop.
				return;
			}
			
			inputLine = inputLine.trim();				//Remove leading and training white space

			// For each new line of input, we run the Finite State Machine on that input.
			int currentNdx = 0;							// Start at the beginning of the input String
			int currentStateNdx = 0;					// We start at State 0
			int nextStateNumber = 0;					// and we initialize the next state number

			// Display the title for the execution trace
			System.out.println("\nCurrent Final Input  Next\nState   State Char  State");

			// As long as there are characters remaining in the line to process, the loop tries to process them
			while (currentNdx < inputLine.length()) {

				// Fetch the current character and increment the index so it points to the next one
				currentChar = inputLine.charAt(currentNdx++);

				// Determine the next state number given this state and the current character
				nextStateNumber = fsmTable.getState(currentStateNdx).getNextStateNumber(currentChar);

				// Display the information about this step in the execution in the execution trace
				System.out.println(((currentStateNdx < 10) ? "  " : " ") + currentStateNdx + 
						((fsmTable.getState(currentStateNdx).isFinalState()) ? "       F     " : "             ") +
						currentChar + "   " + ((nextStateNumber < 10) && (nextStateNumber != -1) ? "  " : " ") + nextStateNumber);

				// As long as the next state number is not negative, we use it to become the new current state index
				// otherwise, we terminate the loop
				if (nextStateNumber >= 0) currentStateNdx = nextStateNumber;
				else break;
			}

			// At the end of the loop we see what caused the termination.  If it is a negative next state number, we're at a
			//  state for which no valid transition exists for the current character, so we output an appropriate error message
			if (nextStateNumber == -1) {
				System.out.println("\n*** Recognizer stopped after finding no valid transitions to another state.\n" + 
						"    The input was NOT recognized.  The point of the issue is indicated by the '?' character.\n" +
						"    The Input: " + inputLine + "\n" +
						"               " + inputLine.substring(0, currentNdx -1) + "?\n");

				// If the current state is a final state at the end of the loop, the FSM has successfully recognized the input,
				// as the loop ended by running out of data.
			} else if (fsmTable.getState(currentStateNdx).isFinalState()) {
				System.out.println(((currentStateNdx < 10) ? "  " : " ") + currentStateNdx + 
						((fsmTable.getState(currentStateNdx).isFinalState()) ? "       F   None" : "           None"));
				System.out.println("\n*** Success! *** End of input found at a final state, and the input <" + 
						inputLine + "> WAS recognized.\n");		

				// Running out of input data at a current state that is not final is an error.
			} else 
				System.out.println("\n*** Recognizer stopped *** End of input found at a non-final state.\n" + 
						"    The input was NOT recognized.  The point of the issue is indicated by the '?' character.\n" +	
						"    The Input: " + inputLine + "\n" +
						"               " + inputLine + "?\n");
		}
		// Once an empty input line is given to the application, the loop ends and so we close the keyboard and allow
		// the application to quit.
		keyboard.close();
	}
}