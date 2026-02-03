package fsm;

/**
 * <p> Title: TestCharCode. </p>
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

public class TestCharCode {

	/**********
	 * This class roots the execution of the test of the CharCode class.  The application tests the
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
		System.out.println("Test CharCode Class\n");
		String testInput = "";
		int numPassed = 0;
		int numFailed = 0;
		
		// Perform a test
		testInput = "0";											// Set up the input for the test
		CharCode test = new CharCode(testInput);					// Perform the test
		System.out.println("1. Input:");							// Display the input that was given
		System.out.println(testInput);								// to the method under test

		// Check the actual output against the expected.  If they match, the test has been passed and display the proper
		// message and tally the result
		if (check("Code: 1; Single Char: 0\n", test.toString())) {
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
		

		testInput = "\\\\";
		test = new CharCode(testInput);
		System.out.println("2. Input:");
		System.out.println(testInput);
		if (check("Code: 2; Special Char: backslash\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else{
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		

		testInput = "\\\"";
		test = new CharCode(testInput);
		System.out.println("3. Input:");
		System.out.println(testInput);
		if (check("Code: 2; Special Char: quote\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else{
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		

		testInput = "\\n";
		test = new CharCode(testInput);
		System.out.println("4. Input:");
		System.out.println(testInput);
		if (check("Code: 2; Special Char: new line\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else{
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		

		testInput = "\\l";
		test = new CharCode(testInput);
		System.out.println("5. Input:");
		System.out.println(testInput);
		if (check("Code: 2; Special Char: line feed\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else{
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		

		testInput = "\\b";
		test = new CharCode(testInput);
		System.out.println("6. Input:");
		System.out.println(testInput);
		if (check("Code: 2; Special Char: blank\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else{
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		

		testInput = "\\t";
		test = new CharCode(testInput);
		System.out.println("7. Input:");
		System.out.println(testInput);
		if (check("Code: 2; Special Char: tab\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else{
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		

		testInput = "\\z";
		test = new CharCode(testInput);
		System.out.println("8. Input:");
		System.out.println(testInput);
		if (check("Code: 0; ***Error*** Character following the leading \"\\\" (z) is not valid.\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else{
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		

		testInput = "a-z";
		test = new CharCode(testInput);
		System.out.println("9. Input:");
		System.out.println(testInput);
		if (check("Code: 3; Range: a-z\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else{
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		

		testInput = "z-a";
		test = new CharCode(testInput);
		System.out.println("10. Input:");
		System.out.println(testInput);
		if (check("***Error*** The first character (z) must be less than the third (a).\nCode: 3; Range: z-a\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else{
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
	

		testInput = "a-a";
		test = new CharCode(testInput);
		System.out.println("11. Input:");
		System.out.println(testInput);
		if (check("***Error*** The first character (a) must be less than the third (a).\nCode: 3; Range: a-a\n", test.toString())) {
			numPassed++;
			System.out.println("\tPass");
		}
		else{
			numFailed++;
			System.out.println("\tFail");
		}
		System.out.println();
		
		System.out.println("Number of tests passed: " + numPassed);
		System.out.println("Number of tests failed: " + numFailed);
	}
}
