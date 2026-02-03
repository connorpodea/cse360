package fsm;

import java.util.Scanner;

/**
 * <p> Title: State Class. </p>
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

public class State {
	
	/**********
	 * The State class is used to represent the concept of a state in a Finite State Machine (FSM).
	 * In this design, a state consists of stateNumber in the range [0, n], an indication as to 
	 * whether or not this is a final state (of which there can be many in any FSM, and a possibly 
	 * empty set of transitions to one of the other states based on whether or not the next 
	 * input character matches a specified character code.  If there is input character, the
	 * machine is in a given state, and none of the character codes for the various transition
	 * match the input character, the FSM will come to a halt.  This design includes the use of a
	 * string to help explain what happen when a failure to find a match takes place.
	 * 
	 */

	/***********
	 * A state transition in a textual form is as follows:
	 * 
	 * State [F] <int> \n					// Every state is declared.  Some of them may be Final states.
	 * <int> \n								// A state can have 0 to many out-bound transitions.  
	 * {<int> <int>{<character codes>} \n}	// Each transition has a destination followed by 1 or more character codes
	 * [<string>] \n						// Not valid if this state is a final state (e.g. F)
	 * 										// A state ends with a blank line or an error message
	 * 
	 */

	//	The following are the class attributes that define the FSM state
	private int stateNumber;			    // The state number in the range 0 to n
	private boolean finalState;			    // True if this state is a final state
	private int numberOfDestinations;	    // This is the number of destinations for this state
	private int[] destinationStateNumber;	    // Destination state number
	private int[] numberOfCodes;				// The number of character codes for this destination
	private CharCode[][] charCode;			    // An array of the codes for this transition
	private boolean isError;				// Captures the fact that this object contains an input error
	private String errorMsg;			    // Error message if this is not a final state and the next 
											// char does not match one of the charCodes for this state.

/*************************************************************************************************/
		
	//	The following are the class working variables
	String inputLine;						// This String holds the input to parse the definitions
	String token = "";						// Input is a sequence of white-space delimited tokens
	Scanner line;							// We use the Scanner class to parse the input

	/**********
	 * This constructor is used to create a new State type object based on the input from a file
	 * scanner, be it from a real file or a String.  Regardless of the success of failure of this
	 * effort to parse the input, a State object will be created.  If there was an error in the
	 * creation of this State, an error message will be stored into the errorMsg attribute and a
	 * code of -1 will be placed into the stateNumber attribute.  Valid state numbers are in the
	 * range from [0, n], so any State with a stateNumber of -1 indicated a parsing or semantic 
	 * error in the state.
	 * 
	 * This object represents a rather complex data structure.  Checks are made at each step in the
	 * input parsing which results in a rather long and somewhat complex looking constructor.  In
	 * reality, the code is very straight-forward. If a required item is found, it is processed, 
	 * else an error message is captured, an error code is specified, and the constructor returns
	 * the error object.
	 * 
	 * @param fileScanner
	 */
	public State(Scanner fileScanner) {
		isError = false;										// Assume there is no error
		errorMsg = "No Error";
		if (fileScanner.hasNextLine()) {						// There must be a line of input
			inputLine = fileScanner.nextLine();
			line = new Scanner(inputLine);

			if (line.hasNext()) {								// There must be a token in that line
				token = line.next();
				if (token.equals("State")) {					// The first token must be "State"
					if (line.hasNext()) {						// After the "State" token should be either an F or an <int>
						if (line.hasNextInt()) {				// See if the next token is an <int>
							finalState = false;					// If it is an <int> then there is no F, so not a final state
							stateNumber = line.nextInt();		// Fetch the state number from the <int>
							if (stateNumber < 0 ){				// If the state number is negative, it is an error
								errorMsg = "*** Error *** A state number must not be negative. It was " + stateNumber + ".\n";
								isError = true;
								return;										
							}
						}
																// The first thing after the "State" is not an <int>
						else {
							token = line.next();
							if (token.equals("F")) {			// Check to see if it is a token of "F"
								finalState = true;				// If it is an "F", this is a final state
								if (line.hasNextInt()) {		// The F must be followed by an <int>
									stateNumber = line.nextInt();	// If it is, extract the state number from the <int>
									if (stateNumber < 0 ){		// Verify that this number is a valid state number (not < 0)
										errorMsg = "*** Error *** A state number must not be negative. It was " + stateNumber + ".\n";
										isError = true;
										return;										
									}
								}
								else {							// The token after the "State" was an "F", but it was
									isError = true;				// not followed by an <int>, therefore it is not valid
									errorMsg = "*** Error *** A state declaration must start with the word \"State\", " +
											"followed by an \"F\", followed by an <int>, but no <int> was found.\n";
									return;
								}
							}									// The token after the "State" was not an "F" or an 
							else {								// <int>, therefore it is not valid
								isError = true;
								errorMsg = "*** Error *** A state declaration must start with the word \"State\".\n" +
										"It must be followed by an \"F\" or an <int>.";
								return;
							}
						}
					}
																// The token after the "State" was not an "F" or an
					else {										// <int>, it was an end of line, therefore it is not valid
						isError = true;
						errorMsg = "*** Error *** A state declaration must start with the word \"State\". " +
								"It must be followed by an \"F\" or an <int>.  It was followed by an end of line.\n";
						return;
					}

				}
																// The first token on the line was NOT State
				else {
					isError = true;
					errorMsg = "*** Error *** A state declaration must start with the word \"State\".\n" +
							" This starts with \"" + token + "\".";
					return;
				}
			}
																// A line was read in with no token on it
			else {
				isError = true;
				errorMsg = "*** Error *** A state declaration was found starting with a blank line.\n";
				return;
			}
			if (fileScanner.hasNextLine()) {					// Following the "State" line comes a line with a 
				inputLine = fileScanner.nextLine();				// single <int> value that specifies how many
				line = new Scanner(inputLine);					// destination description lines follow
				if (line.hasNextInt()) {						// If there is a line with that <int>, it will
					numberOfDestinations = line.nextInt();		// will be check to see if it is reasonable,
					if (numberOfDestinations >= 0) {			// meaning it must be positive
						if (numberOfDestinations == 0) {		// or zero.  If it is zero, the state must be a
							if (finalState) {					// final state.
								errorMsg = "No Error";
							}
							else {								// If there are no destinations and yet this is a
								isError = true;					// final state, it is an error
								errorMsg = "*** Error *** If the State has no destinations, it must be a Final state.  This state was not final.\n";
								return;
							}
						}
																// If the are a positive number of destinations, 
						else {									// prepare to loop through processing each of them.
							destinationStateNumber = new int[numberOfDestinations];		// Save the number of destinations
							numberOfCodes = new int[numberOfDestinations];				// Create an array of number of codes
							charCode = new CharCode[numberOfDestinations][];			// Create a double dim array to hold them.
							for (int ndx = 0; ndx < numberOfDestinations; ndx++) {		// Loop through all of the destinations
								if (fileScanner.hasNextLine()) {						// Verify there is a new line
									inputLine = fileScanner.nextLine();
									line = new Scanner(inputLine);							
									if (line.hasNextInt()) {							// Verify that the new line starts with an <int>
										destinationStateNumber[ndx] = line.nextInt();	// The <int> is the destination state number
										if (destinationStateNumber[ndx] < 0){			// The state number must not be negative
											isError = true;								// If it is, return an error
											errorMsg = "*** Error *** A destination state number must not be negative." + 
													"It was " + destinationStateNumber[ndx] + "\n     Input line with the error: " + inputLine;
											return;													
										}												// After the destination state <int> should
										if (line.hasNextInt()) {						// be another <int> for the number of CharCodes
											numberOfCodes[ndx] = line.nextInt();		// for this destination.  The number of CharCodes
											if (numberOfCodes[ndx] > 0) {				// must be positive.
												charCode[ndx] = new CharCode[numberOfCodes[ndx]];					// Create the array of ChatCodes
												for (int codeNdx = 0; codeNdx < numberOfCodes[ndx]; codeNdx++) {	// Fill the array with CharCodes
													if (line.hasNext()) {											// Check to see if there is a token
														charCode[ndx][codeNdx] = new CharCode(line.next());			// Use it to create the CharCode
														if (charCode[ndx][codeNdx].getCode() == 0){					// If this CharCode has a problem
															isError = true;											// display an error
															errorMsg = "*** Error *** Not enough CharCodes present to complete the declaration.\n     Input line with the error: " + inputLine;
															return;													
														}
													}						// If there is no next token, there are not
													else {					// enough CharCodes for this destination
														isError = true;
														errorMsg = charCode[ndx][codeNdx].getErrorMsg();
														return;													
													}
												}
											}								// If the number of CharCodes is negative
											else {							// display an error message
												isError = true;
												errorMsg = "*** Error *** The number CharCodes is supposed to be positive.\n     Input line with the error: " + inputLine;
												return;													
											}
										}									// If there isn't an <int> for the number of
										else {								// CharCodes, we must display an error message
											isError = true;
											errorMsg = "*** Error *** An integer for the number CharCodes is required.\n     Input line with the error: " + inputLine;
											return;													
										}
									}										// If there isn't an <int> for the destination
									else {									// state number, we must display an error message
										isError = true;
										errorMsg = "*** Error *** An integer for the  destination state is required.\n     Input line with the error: " + inputLine;
										return;													
									}
								}
								else {										// If there isn't a line of text, it is not possible
									isError = true;							// to parse the destination state number
									errorMsg = "*** Error *** A line of text is required to specify the transition to a destination state.\n     Input line with the error: " + inputLine;
									return;													
								}
							}
						}
					}								// The number of destinations must not be
					else {							// negative, or we must display an error
						isError = true;
						errorMsg = "*** Error *** The number of destinations must not be negative. It was: " + numberOfDestinations + "\n     Input line with the error: " + inputLine;
						return;
					}
				}									// The input must be an <int> to specify
				else {								// the number of destinations, otherwise
					isError = true;					// we must display an error	
					errorMsg = "*** Error *** The first line of the State declaration number be followed by a line specificing" + 
							" the number of destinations. This line did not contain an <int>.\n     Input line with the error: " + inputLine;
					return;
				}
			}										// The input must have a line after the declaration
			else {									// of the State to specify the number of destinations
				isError = true;						// If no such line, we must display and error
				errorMsg = "*** Error *** The first line of the State declaration number be followed by a line specificing" + 
						" the number of destinations. This line was missing.\n";
				return;
			}										// After the destinations there must be either an
													// empty line or a error message
			if (fileScanner.hasNextLine()) {
				errorMsg = fileScanner.nextLine().trim();
				if (errorMsg.length() > 0 && finalState) {	// An error message is only valid if this is
					isError = true;							// is NOT a final state.
					errorMsg = "*** Error *** A final state may not have an error message. The line must be blank!\n     Input line with the error: " + inputLine;
					return;					
				}
			}										// After the destinations, there must be either an
			else {									// empty line of an error message
				isError = true;
				errorMsg = "*** Error *** The end of a state must be a blank line or an error message.\n";
				return;
			}
		}
													// The input did not contain a single line of text
		else {
			isError = true;
			errorMsg = "*** Error *** Expecting a state declarartion and no input was found.\n";
			return;
		}
	}
	
	/*************************************************************************************************/
	
	/* These are the getters and setters */

	/**********
	 * The getter to fetch the finalState 
	 * @return	The finalState boolean value is returned
	 */
	public boolean isFinalState() {
		return finalState;
	}
	
	/**********
	 * The getter to fetch the stateNumber 
	 * @return	The stateNumber integer value is returned
	 */
	public int getStateNumber() {
		return stateNumber;
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
	public String getErrorMessage() {
		return errorMsg;
	}
	
	/**********
	 * The getter to fetch the numberOfDestinations 
	 * @return	The numberOfDestinations integer value is returned
	 */
	public int getNumberOfDestinations() {
		return numberOfDestinations;
	}
	
	/**********
	 * The getter to fetch the destinationStateNumber 
	 * @return	The destinationStateNumber integer value is returned
	 */
	public int[] getDestinationStateNumbers() {
		return destinationStateNumber;
	}
	
	/*************************************************************************************************/
	
	/**********
	 * Given this state and a character, determine what the next state number should be.  This is done
	 * by checking each destination in turn and seeing if one of the CharCode objects matches the 
	 * character provided as a parameter.  If a match is found, the destination state number associated
	 * with this set of CharCode objects is return.  If after scanning all of the destinations and still
	 * no match is found, a minus one is returned signaling that the provided character does not match
	 * any of the transitions from this state to the various destinations states.
	 * 
	 * @param currentCharacter	This character in the next character in the input
	 * @return					The next state number or a minus one is returned
	 */
	public int getNextStateNumber(char currentCharacter) {
		// Prepare to check ALL of the potential destination states from this state
		for (int ndx1 = 0; ndx1 < numberOfDestinations; ndx1++) {
			
			// For each destination, check ALL of the potential CharCodes to see if there is a match
			for (int ndx2 = 0; ndx2 < numberOfCodes[ndx1]; ndx2++) {
				
				// If there is a match between the current character and the CharCode, return the destination
				if (charCode[ndx1][ndx2].checkForMatch(currentCharacter)) {		// state number
					return destinationStateNumber[ndx1];
				}
			}
		}
		return -1;	// If none of the CharCodes across all the potential destinations match, return -1
	}
	
	/*************************************************************************************************/

	/* This is the toString method for converting the class to a textual string */
	
	public String toString() {
		String result="";
		
		// If the stateNumber is negative, the state object is an error
		if (isError) {
			result = errorMsg;
		}
		
		// If the stateNumber is is NOT negative, it is a valid state, so display it
		result += "State: ";
		if (finalState) result += "F ";		// Display an F to show this is a Final State
		result +=  + stateNumber + "\n";	// Display this state's staeNumber

		// Iterate through all of the destinations, display each in turn - Start with the number
		// of destinations
		result += "      Number of Destinations: " + numberOfDestinations + "\n";
		for (int ndx = 0; ndx < numberOfDestinations; ndx++) {
			
			// Display the destination number (sequence number), the destination State Number, and the number of CharCodes
			result += "      Destination Number: " + (ndx+1) + " Destination State: " + destinationStateNumber[ndx] +
					" Number of codes: " + numberOfCodes[ndx] + "\n";
			
			// For that destination, display each of the CharCodes
			for (int codeNdx = 0; codeNdx < numberOfCodes[ndx]; codeNdx++) {
				result += "         "+ charCode[ndx][codeNdx];
			}
		}
		// After displaying all of the destinations, display the blank line or the error message
		result += "      Error message: " + errorMsg + "\n";
		
		// Return the constructed string to the caller
		return result;
	}
}