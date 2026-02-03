package fsm;

/**
 * <p> Title: CharCode Class. </p>
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

public class CharCode {
	
/**********
 * This class defines the character codes used in the transitions from one state to the
 * next. The class always returns an object, even when there is an error in the input.  
 * The returned object has a code that defines what is there, and that code is used
 * to express the cases where the input is not a valid code or the end of input has been 
 * found.
 * 
 * The code attribute specifies is the kind of character code use. Zero signals an error.
 * One signals a single character. Two indicates a two character representation of a 
 * special character. and three indicates a range of printable characters.
 * 
 * Code	Definition
 * 	 0	Error
 *   1	A printable char that is not a "\" or a "#"
 *   2 	A pair of printable characters starting with a "\" and followed by:
 *   		a. "\" stands for a single "\" character
 *   		b. "n" stands for a new line character (e.g. a carriage return)
 *   		c. "l" stands for a line feed character
 *   		d. "b" stands for a blank character
 *   		e. "t" stands for a tab character
 *   3	A sequence of three printable characters where the second is a "-" (minus sign)
 *   	as in "a-z", "A-Z", and "0-9"
 *   
 *   The error message gives human readable explanation of what was wrong with the input
 *   The other attributes encode the character being specified or the range of characters
 *   
 */
	
	//	The following are the class attributes that define the character code or the error
	private int code;			// This code defines what was found
	private char singleChar;	// If the code is 1, this attribute is used
	private char specialChar;	// If the code is 2, this attribute is used
	private char firstChar;		// If the code is 3, these two attributes are used
	private char finalChar;
	private boolean isError;	// Signals that there is an error in the creation of this object
	private String errorMsg;	// If the code is 0, this attribute is used
	
/*************************************************************************************************/
	
	/**********
	 * This constructor is used to create a new CharCode type of object based on the input given
	 * to the constructor in the String parameter.
	 * 
	 * @param str	The input string that must be parsed into a specific CharCode typed object.
	 */
	public CharCode(String str) {
		isError = false;					// Assume there is no error.
		errorMsg = "No Error";
		switch (str.length()) {				// The size of the string determines how it is parsed
		
		// A single character input - a printable character, but this constraint is not checked
		case 1: 
			code = 1;
			singleChar = str.charAt(0);
			specialChar = ' ';
			firstChar =  ' ';
			finalChar =  ' ';
			break;
			
		// A special character input - starting with a backslash character
		case 2: 
			code = 2;
			switch (str.charAt(1)) {
			case '\\':
				specialChar = '\\';			// Use the backslash character
				break;
			case '"':
				specialChar = '"';			// Use the double quote character
				break;
			case 'n':
				specialChar = '\r';			// Use the new line (CR) character
				break;
			case 'l':
				specialChar = '\n';			// Use the line feed (LF) character
				break;
			case 'b':
				specialChar = ' ';			// Use the blank character
				break;
			case 't':
				specialChar = '\t';			// Use the tab character
				break;
			default: 
				code = 0;					// Any other second character is an error.
				errorMsg = "***Error*** Character following the leading \"\\\" (" + str.charAt(1) + ") is not valid.";
				return;
			}
			singleChar = ' ';
			firstChar =  ' ';
			finalChar =  ' ';
			break;
			
		// A range of characters - bounded below and above by a printable character	
		case 3: 
			code = 3;
			
			// The middle of the three must be a minus sign.
			if (str.charAt(1) != '-') {
				isError = true;
				errorMsg = "***Error*** The second character should be a \"-\".";
				return;				
			}
			firstChar = str.charAt(0);
			finalChar = str.charAt(2);
			singleChar = ' ';
			specialChar = ' ';
			
			// The left character must be less than the right one
			if (str.charAt(0) >= str.charAt(2)) {
				isError = true;
				errorMsg = "***Error*** The first character (" + str.charAt(0) + ") must be less than the third (" + str.charAt(2) + ").";
				return;								
			}
			break;
			
		// If any other size string is passed in, it is rejected with an error message.
		default: 
			isError = true;
			errorMsg = "***Error*** The length of the string is not valid.  The input was <" + str + ">";
			return;
		}
	}
	
	/*************************************************************************************************/
	
	/**********
	 * This method checks to see of a specific character given as the input parameter agrees with 
	 * the code specified during the construction of this object.
	 * 
	 * @param currentChar	The character to be checked against the constructed object
	 * @return				The methods returns true if the input character matches the specified code
	 * 						else the method returns false.
	 */
	public boolean checkForMatch(char currentChar) {
		switch (code) {
		
		// A code of zero signals an input error, so this should never happen
		case 0:
			isError = true;
			System.out.println("*** Error *** Invalid code in the fsm.");
			return false;
			
		// Check for a single character
		case 1:
			if(currentChar == singleChar)
				return true;
			else
				return false;
		
		// Check for a specific special character	
		case 2:
			if (currentChar == specialChar)
				return true;
			else
				return false;
		
		// Check to see if it is part of a specified range of characters
		case 3:
			if (currentChar >= firstChar && currentChar <= finalChar)
				return true;
			else
				return false;
			
		// Like code 0, this should never happen
		default:
			isError = true;
			System.out.println("*** Error *** Unknown code in the fsm.");
			return false;
		}
	}
	
	/*************************************************************************************************/

	/* These are the getters and setters */
	
	/**********
	 * The getter to fetch the code attribute
	 * @return
	 */
	public int getCode() {
		return code;
	}
	
	/**********
	 * The getter to fetch the specialChar attribute
	 * @return
	 */
	public char getSpecialChar() {
		return singleChar;
	}

	/**********
	 * The getter to fetch the isError attribute
	 * @return
	 */
	public boolean getIsError() {
		return isError;
	}
	
	/**********
	 * The getter to fetch the errorMsg attribute
	 * @return
	 */
	public String getErrorMsg() {
		return errorMsg;
	}
	
	/*************************************************************************************************/

	/* This is the toString method for converting the class to a textual string */
	
	public String toString() {
		String result="";
		if (isError) 
			result = errorMsg + "\n";
		switch (code) {

		// The error code
		case 0:
			result += "Code: " + code + "; " + errorMsg + "\n";
			break;

			// A single character
		case 1:
			result += "Code: " + code + "; Single Char: " + singleChar + "\n";
			break;

			// A special character
		case 2:
			result += "Code: " + code + "; Special Char: ";
			switch (specialChar) {
			case '\\':
				result += "backslash\n";
				break;
			case '"':
				result += "quote\n";
				break;
			case 'n':
				result += "new line\n";
				break;
			case 'l':
				result += "line feed\n";
				break;
			case 'b':
				result += "blank\n";
				break;
			case 't':
				result += "tab\n";
				break;
			default: 
				result += "***Error*** Character following a leading \"\\\" (" + specialChar + ") is not valid.\n";
				break;
			}
			break;

			// A range of characters
		case 3:
			result += "Code: " + code + "; Range: " + firstChar + "-" + finalChar + "\n";
			break;

			// Some other unexpected character
		default:
			result += "***Error*** The code is not valid.";
			break;
		}
		return result;
	}
}