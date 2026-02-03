package fsm;

/**
 * <p> Title: TestState. </p>
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

import java.util.Scanner;

public class TestState {

	/**********
	 * This class roots the execution of the test of the State class.  The application tests the
	 * class by invoking the class and checking the result to see if the results are proper.  This
	 * particular set of tests is incomplete as it only tests the constructor method.
	 * 
	 * 
	 */
	
	/*********************************************************************************************/
	
	/**********
	 * The check method compares an expected String to an Actual String and returns true if the 
	 * Strings match and false otherwise.  In addition, the Strings are displayed to the console
	 * and a message is display stating whether or not there is a difference.  If there is a
	 * difference, the character at the point of the difference in the actual String is replaced
	 * with a "?" and both are displayed making it clear what character is the start of the
	 * difference
	 * 
	 * @param expected	The String object of the expected value
	 * @param Actual	The String object of the actual value
	 */
	private static boolean check(String expected, String actual) {
		// Display the input parameters
		System.out.println("***Expected String");
		System.out.println(expected);
		System.out.println("***Actual String");
		System.out.println(actual);
		
		// Check to see if there is a difference
		int lesserLength = expected.length();
		if (lesserLength > actual.length()) lesserLength = actual.length();
		int ndx = 0;
		while (ndx < lesserLength && expected.charAt(ndx) == actual.charAt(ndx))
			ndx++;
		
		// Explain why the loop terminated and if there is a difference make it clear to the user
		if (ndx < lesserLength || lesserLength < expected.length() || lesserLength < actual.length()) {
			System.out.println("*** There is a difference!\n" + expected.substring(0, ndx) + "? <-----");
			return false;
		}
		System.out.println("*** There is no difference!\n");
		return true;
	}
	
	/*********************************************************************************************/
	
	/**********
	 * This main method roots the execution of this test.  The method ignores the program
	 * parameters.  After initializing several local variables, it performs a sequence of
	 * tests, displaying information accordingly, and tallying the number of successes and
	 * failures.
	 * 
	 * @param args	Ignored by this application.
	 */
	public static void main(String[] args) {
		// Display the header message to the console and initialize local variables
		System.out.println("Test State Class\n");
		String testInput = "";
		int numPassed = 0;
		int numFailed = 0;	
		
		// Perform a test
		testInput = "\n";											// Set up the input for the test
		State test = new State(new Scanner(testInput));				// Perform the test
		System.out.println("1. Input:");							// Display the input that was given
		System.out.println(testInput);								// to the method under test

		// Check the actual output against the expected.  If they match, the test has been passed and display the proper
		// message and tally the result
		if (check("*** Error *** A state declaration was found starting with a blank line.\n" +
				"State: 0\n" +
				"      Number of Destinations: 0\n" +
				"      Error message: *** Error *** A state declaration was found starting with a blank line.\n\n", 
				test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		// If they do not match, display that there was a failure and tally that result
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		// This same pattern is done over and over again.  The messages in the code make it clear what is being tested.
		
		
		testInput = "State\n";
		test = new State(new Scanner(testInput));
		System.out.println("2. Input:");
		System.out.println(testInput);
		if (check("*** Error *** A state declaration must start with the word \"State\". " +
			"It must be followed by an \"F\" or an <int>.  It was followed by an end of line.\n" +
			"State: 0\n" +
			"      Number of Destinations: 0\n" +
			"      Error message: *** Error *** A state declaration must start with the word \"State\". " + 
			"It must be followed by an \"F\" or an <int>.  It was followed by an end of line.\n\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		
		
		testInput = "State F\n";
		test = new State(new Scanner(testInput));
		System.out.println("3. Input:");
		System.out.println(testInput);
		if (check("*** Error *** A state declaration must start with the word \"State\", " +
				"followed by an \"F\", followed by an <int>, but no <int> was found.\n" +
				"State: F 0\n" +
				"      Number of Destinations: 0\n" +
				"      Error message: *** Error *** A state declaration must start with the " + 
				"word \"State\", followed by an \"F\", followed by an <int>, but no <int> was found.\n\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		
		
		testInput = "State 0\n";
		test = new State(new Scanner(testInput));
		System.out.println("4. Input:");
		System.out.println(testInput);
		if (check("*** Error *** The first line of the State declaration number be followed by a line specificing" + 
				" the number of destinations. This line was missing.\n" +
				"State: 0\n" +
			    "      Number of Destinations: 0\n" +
			    "      Error message: *** Error *** The first line of the State declaration number be followed by a line " + 
			    "specificing the number of destinations. This line was missing.\n\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		
		
		testInput = "State -2\n";
		test = new State(new Scanner(testInput));
		System.out.println("5. Input:");
		System.out.println(testInput);
		if (check("*** Error *** A state number must not be negative. It was -2.\n" +
				"State: -2\n" +
			    "      Number of Destinations: 0\n" +
			    "      Error message: *** Error *** A state number must not be negative. It was -2.\n\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		
		
		testInput = "State 0\n ";
		test = new State(new Scanner(testInput));
		System.out.println("6. Input:");
		System.out.println(testInput);
		if (check("*** Error *** The first line of the State declaration number be followed by a line specificing" + 
				" the number of destinations. This line did not contain an <int>.\n" +
				"State: 0\n" +
			    "      Number of Destinations: 0\n" +
			    "      Error message: *** Error *** The first line of the State declaration number be followed by a" + 
			    " line specificing the number of destinations. This line did not contain an <int>.\n\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		
		
		testInput = "State 0\n -1";
		test = new State(new Scanner(testInput));
		System.out.println("7. Input:");
		System.out.println(testInput);
		if (check("*** Error *** The number of destinations must not be negative. It was: -1\n" +
				"State: 0\n" +
			    "      Number of Destinations: -1\n" +
			    "      Error message: *** Error *** The number of destinations must not be negative. It was: -1\n\n"
			    , test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		
		
		testInput = "State 0\n 0";
		test = new State(new Scanner(testInput));
		System.out.println("8. Input:");
		System.out.println(testInput);
		if (check("*** Error *** If the State has no destinations, it must be a Final state." + 
				"  This state was not final.\n" +
				"State: 0\n" +
			    "      Number of Destinations: 0\n" +
			    "      Error message: *** Error *** If the State has no destinations, it must be a Final state.  " + 
			    "This state was not final.\n\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		
		
		testInput = "State F 0\n 0\n ";
		test = new State(new Scanner(testInput));
		System.out.println("9. Input:");
		System.out.println(testInput);
		if (check("State: F 0\n      Number of Destinations: 0\n" +
				"      Error message: \n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		
		
		testInput = "State F 0\n 1\n 1 1 0-9\n ";
		test = new State(new Scanner(testInput));
		System.out.println("10. Input:");
		System.out.println(testInput);
		if (check("State: F 0\n" +
				"      Number of Destinations: 1\n" +
				"      Destination Number: 1 Destination State: 1 Number of codes: 1\n" +
				"         Code: 3; Range: 0-9\n" +
				"      Error message: \n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		
		
		testInput = "State F 0\n 1\n 1 3 0-9 a-f A-F\n ";
		test = new State(new Scanner(testInput));
		System.out.println("11. Input:");
		System.out.println(testInput);
		if (check("State: F 0\n" +
				"      Number of Destinations: 1\n" +
				"      Destination Number: 1 Destination State: 1 Number of codes: 3\n" +
				"         Code: 3; Range: 0-9\n" +
				"         Code: 3; Range: a-f\n" +
				"         Code: 3; Range: A-F\n" +
				"      Error message: \n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		
		
		
		testInput = "State 0\n 2\n 1 1 0-9\n 3 1 .\n ";
		test = new State(new Scanner(testInput));
		System.out.println("12. Input:");
		System.out.println(testInput);
		if (check("State: 0\n" +
				"      Number of Destinations: 2\n" +
				"      Destination Number: 1 Destination State: 1 Number of codes: 1\n" +
				"         Code: 3; Range: 0-9\n" +
				"      Destination Number: 2 Destination State: 3 Number of codes: 1\n" +
				"         Code: 1; Single Char: .\n" +
				"      Error message: \n", test.toString())) {

			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();


		
		testInput = "State F 1\n 2\n 2 1 . \n 5 2 e E\n ";
		test = new State(new Scanner(testInput));
		System.out.println("13. Input:");
		System.out.println(testInput);
		if (check("State: F 1\n" +
				"      Number of Destinations: 2\n" +
				"      Destination Number: 1 Destination State: 2 Number of codes: 1\n" +
				"         Code: 1; Single Char: .\n" +
				"      Destination Number: 2 Destination State: 5 Number of codes: 2\n" +
				"         Code: 1; Single Char: e\n" +
				"         Code: 1; Single Char: E\n" +
				"      Error message: \n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();

		
		
		
		testInput = "State 1\n 2\n 2 1 . \n 5 2 e E\n***Error*** Error message for this state.\n";
		test = new State(new Scanner(testInput));
		System.out.println("14. Input:");
		System.out.println(testInput);
		if (check("State: 1\n" +
				"      Number of Destinations: 2\n" +
				"      Destination Number: 1 Destination State: 2 Number of codes: 1\n" +
				"         Code: 1; Single Char: .\n" +
				"      Destination Number: 2 Destination State: 5 Number of codes: 2\n" +
				"         Code: 1; Single Char: e\n" +
				"         Code: 1; Single Char: E\n" +
				"      Error message: ***Error*** Error message for this state.\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();


		
		System.out.println("Number of tests passed: " + numPassed);
		System.out.println("Number of tests failed: " + numFailed);
	}

}
