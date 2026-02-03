package fsm;

import java.util.Scanner;

/**
 * <p> Title: Test Floating Point Recognizer Class. </p>
 * 
 * <p> Description: A demonstration main class of the Finite State Machine demonstration application </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2017 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 0.00	2017-08-16	Initial baseline 
 * 
 */

public class TestFloatingPointRecognizer {

	/**********
	 * This class roots the execution of the demonstration.  The application initializes the state table from
	 * a fixed text string.  This 8 state FSM is a direct encoding of the figure used in the materials showing
	 * how a FSM can be used to recognize a floating point number in decimal or scientific notation format.
	 * 
	 * @param args	The program args are ignored/.
	 */
	public static void main(String[] args) {
		
		// The following local variable contains the definition of the floating point recognizer
		String fprRecognizer = "8\n" + 
				"State 0\n2\n1 1 0-9\n3 1 .\n \n" + 
				"State F 1\n 3\n 1 1 0-9\n 2 1 . \n 5 2 e E\n \n" +
				"State F 2\n 2\n 2 1 0-9\n 5 2 e E\n \n" +
				"State 3\n 1\n 4 1 0-9\n \n" +
				"State F 4\n 2\n 4 1 0-9\n 5 2 e E\n \n" +
				"State 5\n 2\n 7 1 0-9\n 6 2 + -\n \n" +
				"State 6\n 1\n 7 1 0-9\n \n" +
				"State F 7\n 1\n 7 1 0-9\n \n";
		
		char currentChar = ' ';		// A local variable specifying the value of the current character

		// This statement is used to initialized and load the floating point recognizer into the state table
		StateTable fpr = new StateTable(new Scanner(fprRecognizer));
		
		// Associate the system keyboard with a Scanner object
		Scanner keyboard = new Scanner(System.in);
		
		// Initialize the inputLine local variable.
		String inputLine = "";
		
		// Request input from the user and then read in what the user enters
		System.out.println("Enter a string to see if it is a floating point value.  Enter an empty line to terminate.");
		
		// As long as there is a next line, read it in... Since the input is the keyboard, this is always true
		while (keyboard.hasNextLine()) {
			inputLine = keyboard.nextLine().trim();		// Fetch the next line
			if (inputLine.length() == 0) {				// If the length of the trimmed line is zero, stop the loop
				System.out.println("\n*** Empty input line detected, the loop stops.");
				keyboard.close();						// Display the reason for terminating the loop.
				return;
			}
			
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
				nextStateNumber = fpr.getState(currentStateNdx).getNextStateNumber(currentChar);
				
				// Display the information about this step in the execution in the execution trace
				System.out.println(((currentStateNdx < 10) ? "  " : " ") + currentStateNdx + 
						((fpr.getState(currentStateNdx).isFinalState()) ? "       F     " : "             ") +
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
			} else if (fpr.getState(currentStateNdx).isFinalState()) {
				System.out.println(((currentStateNdx < 10) ? "  " : " ") + currentStateNdx + 
						((fpr.getState(currentStateNdx).isFinalState()) ? "       F   None" : "           None"));
				System.out.println("\n*** Success! *** End of input found at a final state and the input <" + 
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