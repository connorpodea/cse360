package fsm;

/**
 * <p> Title: TestStateTableClass. </p>
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

public class TestStateTable {

	/**********
	 * This class roots the execution of the test of the StateTable class.  The application tests
	 * the class by invoking the class and checking the result to see if the results are proper.
	 * This particular set of tests is incomplete as it only tests the constructor method.
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
	 * @param actual	The String object of the actual value
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
		System.out.println("Test StateTable Class\n");
		int numPassed = 0;
		int numFailed = 0;	
		String testInput="";
		
		// Perform a test
		testInput = "";												// Set up the input for the test
		StateTable test = new StateTable(new Scanner(testInput));	// Perform the test
		System.out.println("1. Input:");							// Display the input that was given
		System.out.println(testInput);								// to the method under test
		
		// Check the actual output against the expected.  If they match, the test has been passed and display the proper
		if (check("Number of States: 0\n" +							// message and tally the result
				"*** Error *** The input is empty.  It must start with a line containins a positive integer.\n", test.toString())) {
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
		
		
		testInput = "\n";
		test = new StateTable(new Scanner(testInput));
		System.out.println("2. Input:");
		System.out.println(testInput);
		if (check("Number of States: 0\n" +
				"*** Error *** The state table must start with an integer that specifies the number of states.\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		
		
		testInput = "1\n";
		test = new StateTable(new Scanner(testInput));
		System.out.println("3. Input:");
		System.out.println(testInput);
		if (check("Number of States: 1\n" + 
				"*** Error *** Missing State.\n" +
				"*** Error *** Expecting a state declarartion and no input was found.\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		
		
		testInput = "1\nState F -2\n 1\n 1 1 0-9\n ";
		test = new StateTable(new Scanner(testInput));
		System.out.println("4. Input:");
		System.out.println(testInput);
		if (check("Number of States: 1\n" + 
				"*** Error *** Missing State.\n" +
				"*** Error *** A state number must not be negative. It was -2.\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		
		
		testInput = "1\nState F -2\n1\n1 1 0-9\n ";
		test = new StateTable(new Scanner(testInput));
		System.out.println("5. Input:");
		if (check("Number of States: 1\n" + 
				"*** Error *** Missing State.\n" +
				"*** Error *** A state number must not be negative. It was -2.\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		
		
		testInput = "1\nState F 0\n 1\n 0 1 0-9\n ";
		test = new StateTable(new Scanner(testInput));
		System.out.println("6. Input:");
		System.out.println(testInput);
		if (check(  "Number of States: 1\n" + 
					"State: F 0\n" +
					"      Number of Destinations: 1\n" + 
					"      Destination Number: 1 Destination State: 0 Number of codes: 1\n" + 
					"         Code: 3; Range: 0-9\n" +
					"      Error message: \n" +
			        "No Errors were found!\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		
		
		testInput = "1\nState F 0\n1\n1 1 0-9\n ";
		test = new StateTable(new Scanner(testInput));
		System.out.println("7. Input:");
		System.out.println(testInput);
		if (check(  "Number of States: 1\n" + 
				"State: F 0\n" +
			    "      Number of Destinations: 1\n" +
			    "      Destination Number: 1 Destination State: 1 Number of codes: 1\n" +
			    "         Code: 3; Range: 0-9\n" +
			    "      Error message: \n" +
				"*** Error *** The destination: 1 is outside of the range of [0,0].\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();

		testInput = "2\nState 0\n2\n1 1 0-9\n3 1 .\n \nState F 1\n 2\n 2 1 . \n 5 2 e E\n ";
		test = new StateTable(new Scanner(testInput));
		System.out.println("8. Input:");
		System.out.println(testInput);
		if (check(  "Number of States: 2\n" + 
				"State: 0\n" +
			    "      Number of Destinations: 2\n" +
			    "      Destination Number: 1 Destination State: 1 Number of codes: 1\n" +
			    "         Code: 3; Range: 0-9\n" +
			    "      Destination Number: 2 Destination State: 3 Number of codes: 1\n" +
			    "         Code: 1; Single Char: .\n" +
			    "      Error message: \n" +
			    "State: F 1\n" +
			    "      Number of Destinations: 2\n" +
			    "      Destination Number: 1 Destination State: 2 Number of codes: 1\n" +
			    "         Code: 1; Single Char: .\n" +
			    "      Destination Number: 2 Destination State: 5 Number of codes: 2\n" +
			    "         Code: 1; Single Char: e\n" +
			    "         Code: 1; Single Char: E\n" +
			    "      Error message: \n" +
				"*** Error *** The destination: 5 is outside of the range of [0,1].\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else {
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		
		
		testInput = "8\n" + 
					"State 0\n2\n1 1 0-9\n3 1 .\n \n" + 
					"State F 1\n 3\n 1 1 0-9\n 2 1 . \n 5 2 e E\n \n" +
					"State F 2\n 2\n 2 1 0-9\n 5 2 e E\n \n" +
					"State 3\n 1\n 4 1 0-9\n \n" +
					"State F 4\n 2\n 4 1 0-9\n 5 2 e E\n \n" +
					"State 5\n 2\n 7 1 0-9\n 6 2 + -\n \n" +
					"State 6\n 1\n 7 1 0-9\n \n" +
					"State F 7\n 1\n 7 1 0-9\n \n";
		test = new StateTable(new Scanner(testInput));
		System.out.println("9. Input:");
		System.out.println(testInput);
		if (check(  "Number of States: 8\n" + 
				"State: 0\n" +
				"      Number of Destinations: 2\n" +
				"      Destination Number: 1 Destination State: 1 Number of codes: 1\n" +
		        "         Code: 3; Range: 0-9\n" +
				"      Destination Number: 2 Destination State: 3 Number of codes: 1\n" +
		        "         Code: 1; Single Char: .\n" +
				"      Error message: \n" +
				"State: F 1\n" +
				"      Number of Destinations: 3\n" +
				"      Destination Number: 1 Destination State: 1 Number of codes: 1\n" +
		        "         Code: 3; Range: 0-9\n" +
				"      Destination Number: 2 Destination State: 2 Number of codes: 1\n" +
		        "         Code: 1; Single Char: .\n" +
				"      Destination Number: 3 Destination State: 5 Number of codes: 2\n" +
		        "         Code: 1; Single Char: e\n" +
		        "         Code: 1; Single Char: E\n" +
				"      Error message: \n" +
				"State: F 2\n" +
				"      Number of Destinations: 2\n" +
				"      Destination Number: 1 Destination State: 2 Number of codes: 1\n" +
		        "         Code: 3; Range: 0-9\n" +
				"      Destination Number: 2 Destination State: 5 Number of codes: 2\n" +
		        "         Code: 1; Single Char: e\n" +
		        "         Code: 1; Single Char: E\n" +
				"      Error message: \n" +
				"State: 3\n" +
				"      Number of Destinations: 1\n" +
				"      Destination Number: 1 Destination State: 4 Number of codes: 1\n" +
		        "         Code: 3; Range: 0-9\n" +
				"      Error message: \n" +
				"State: F 4\n" +
				"      Number of Destinations: 2\n" +
				"      Destination Number: 1 Destination State: 4 Number of codes: 1\n" +
		        "         Code: 3; Range: 0-9\n" +
				"      Destination Number: 2 Destination State: 5 Number of codes: 2\n" +
		        "         Code: 1; Single Char: e\n" +
		        "         Code: 1; Single Char: E\n" +
				"      Error message: \n" +
				"State: 5\n" +
				"      Number of Destinations: 2\n" +
				"      Destination Number: 1 Destination State: 7 Number of codes: 1\n" +
		        "         Code: 3; Range: 0-9\n" +
				"      Destination Number: 2 Destination State: 6 Number of codes: 2\n" +
		        "         Code: 1; Single Char: +\n" +
		        "         Code: 1; Single Char: -\n" +
				"      Error message: \n" +
				"State: 6\n" +
				"      Number of Destinations: 1\n" +
				"      Destination Number: 1 Destination State: 7 Number of codes: 1\n" +
		        "         Code: 3; Range: 0-9\n" +
				"      Error message: \n" +
				"State: F 7\n" +
				"      Number of Destinations: 1\n" +
				"      Destination Number: 1 Destination State: 7 Number of codes: 1\n" +
		        "         Code: 3; Range: 0-9\n" +
				"      Error message: \n" +
		        "No Errors were found!\n", test.toString())) {
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
