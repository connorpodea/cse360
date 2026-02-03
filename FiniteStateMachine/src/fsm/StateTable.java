package fsm;

import java.util.Scanner;

/**
 * <p> Title: StateTable Class. </p>
 * 
 * <p> Description: A component of the Finite State Machine demonstration application </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2017 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 0.00	2017-08-16	Initial baseline 
 * 
 */

public class StateTable {
	
	/**********
	 * The StateTable class is used to represent the concept of a tables of state in a Finite 
	 * State Machine (FSM). In this design, a state table consists of an <int> that specifies
	 * the number of states in the table and an array of that many objects, each being a State.
	 * 
	 * The state table is used by a Finite State Machine "recognizer" that uses the table to
	 * evaluate whether or not an input string can be recognized that the finite state machine
	 * that is encoded into the table.
	 */


	/***********
	 * A state table in a textual form is as follows:
	 * 
	 * <int> \n								// The table starts with an integer on a line by itself, specifying the number of states
	 * {state} 								// Following that line will be a sequence of state definitions.
	 * 
	 */

	//	The following are the class attributes that define the FSM state table
	private int numberOfStates;				// The number of states in the table (The size of the following array.)
	private State[] stateTable;				// This array holds the state table
	private boolean isError;				// This specified that there was an input error in creating this object
	private String errorMsg;				// This is the error message for that input error
	

/*************************************************************************************************/
		
	//	The following are the class working variables
	private String inputLine;
	private Scanner line;


	/**********
	 * This constructor is used to create a Finite State Machine state table by parsing input from a Scanner
	 * class that could be from a file or a text string.  As described above the first line number be an <int>
	 * of the number of states in the Scanner and that must be followed by that many State objects.
	 * 
	 * As with the other classes, if the number of states is negative, this object represents an error.  If
	 * the number is not negative, it specifies how many states are to be loaded into the state table using
	 * the State class.
	 * 
	 * After reading in the state table, if there are no errors, a final sequence of tests are performed to
	 * make sure that the semantic rules have not been violated.
	 * 
	 * @param fileScanner	This is the Scanner that has access to the input text
	 * 
	 */
	public StateTable(Scanner fileScanner) {
		isError = false;													// Assume there is no error
		errorMsg = "No Errors were found!\n";
		if (fileScanner.hasNextLine()) {									// Verify that there is a line of data, it must be the
			inputLine = fileScanner.nextLine();								// number of states in the state table that is being 
			line = new Scanner(inputLine);									// constructed.  If there is no such line or the line
			if (line.hasNextInt()) {										// does not consist of a <int>, display an error
				numberOfStates = line.nextInt();							// Fetch the numberOfStates and verify that it is
				if (numberOfStates > 0) {									// positive.  If so, establish a state table with a
					stateTable = new State[numberOfStates];					// many elements as the number of states.  Then loop
					for (int ndx = 0; ndx < numberOfStates; ndx++) {		// through the input processing each state, one by one.
						State tempState = new State(fileScanner);			// Use the State constructor to parse out a state and
						int tempStateNumber = tempState.getStateNumber();	// verify that the parse worked (the state number is
						if (tempState.getIsError()) {						// not -1)  If it is minus 1, there was something wrong
							errorMsg = tempState.getErrorMessage();			// with the parsing of that state, so fetch the error
							isError = true;									// message and return the table indicating -1 number of
							return;											// states, which signals that there was an error.
						}
						if (tempStateNumber >= numberOfStates) {			// If the state number is too large, display and error
							errorMsg = "*** Error *** This state number, " + tempStateNumber + 	// message.
									", is out of range [0," + (numberOfStates-1) + "].";
							isError = true;		
							return;																	
						}													// If the new state number has not been used earlier
						if (tempStateNumber >= 0)
							if (stateTable[tempStateNumber] == null) {			// then save the state into the table.  If something is
								stateTable[tempStateNumber] = tempState;		// already there, display an error message.
							}
							else {
								errorMsg = "*** Error *** This state number: " + tempStateNumber + " duplicates a previsously declared state.";
								isError = true;		
								return;	
							}
						else {
						}
					}
				}
				else {							// If the number of states is not positive, display an error message
					errorMsg = "*** Error *** The number of states must be positive. It was " + numberOfStates + ".\n";
					isError = true;		
					return;										
				}
			}
			else {								// If there isn't an <int> in the first line, display an error message
				errorMsg = "*** Error *** The state table must start with an integer that specifies the number of states.\n";
				isError = true;		
				return;										
			}
			
			/*************************************************************************************/
			
			// The following are semantic error checks.
			
			// Verify all of the states in the table have been defined
			for (int ndx = 0; ndx < numberOfStates; ndx++) {
				if (stateTable[ndx] == null) {
					isError = true;		
					errorMsg = "*** Error *** The following states were not defined: " + ndx;
					for (int ndx2 = ndx +1; ndx2 < numberOfStates; ndx2++)
						if (stateTable[ndx2] == null) errorMsg += (", " + ndx2);
				}
			}
			if (isError) errorMsg += "\n";
			
			// Verify that all of the destinations are in range (e.g. [0, numberOfStates-1])
			for (int ndx = 0; ndx < numberOfStates; ndx++) {
				if (stateTable[ndx] != null) {
					int [] destinations = stateTable[ndx].getDestinationStateNumbers();
					int numberOfDestinations = stateTable[ndx].getNumberOfDestinations();
					for (int ndx2 = 0; ndx2 < numberOfDestinations; ndx2++) {
						if ((destinations[ndx2] < 0) || (destinations[ndx2] >= numberOfStates)) {
							errorMsg = "*** Error *** The destination: " + destinations[ndx2] + 
									" is outside of the range of [0," + (numberOfStates - 1) + "].\n";
							isError = true;		
						}
					}
				}
			}
			
			// Additional semantic checks would go here.
		}
		else {								// If there is an empty first line, display an error message
			errorMsg = "*** Error *** The input is empty.  It must start with a line containins a positive integer.\n";
			isError = true;		
			return;										
		}
	}
	
	/*************************************************************************************************/
	
	/* These are the getters and setters */

	/**********
	 * The getter to fetch the numberOfStates 
	 * @return	The numberOfStates integer value is returned
	 */
	public int getNumberOfStates() {
		return numberOfStates;
	}
	
	/**********
	 * The getter to fetch the isError 
	 * @return	The isError boolean value is returned
	 */
	public boolean getIsError() {
		return isError;
	}
	
	/**********
	 * The getter to fetch the errorMsg 
	 * @return	The errorMsg String value is returned
	 */
	public String getErrorMsg() {
		return errorMsg;
	}
	
	/**********
	 * The getter to fetch the state 
	 * @return	The state object value is returned
	 */
	public State getState(int ndx) {
		return stateTable[ndx];
	}
	
	/*************************************************************************************************/

	/* This is the toString method for converting the class to a textual string */
	
	public String toString() {
		String result = "Number of States: " + numberOfStates + "\n";
		for (int ndx = 0; ndx < numberOfStates; ndx++) {
			if (stateTable[ndx] == null)
				result += "*** Error *** State Table is missing a state for index: " + ndx +  ".\n";
			else
				result += stateTable[ndx].toString();
		}
		result += errorMsg;
		return result;
	}
}
