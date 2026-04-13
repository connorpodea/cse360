package entityClasses;

/*******
 * <p> Title: User Class </p>
 * * <p> Description: This User class represents a user entity in the system.  It contains the user's
 * details such as userName, password, and roles being played. </p>
 * * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * * @author Lynn Robert Carter
 * * */ 

public class User {
	
	/*
	 * These are the private attributes for this entity object
	 */
    private String userName;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String preferredFirstName;
    private String emailAddress;
    private boolean adminRole;
    private boolean staffRole;
    private boolean studentRole;
    
    
    /*****
     * <p> Method: User() </p>
     * * <p> Description: This default constructor is not used in this system. </p>
     */
    public User() {
    	
    }

    
    /*****
     * <p> Method: User(String userName, String password, String email, String fn, String mn, 
     * String ln, String pfn, boolean r1, boolean r2, boolean r3) </p>
     * * <p> Description: This constructor is used to establish user entity objects. </p>
     * * @param userName specifies the account userName for this user
     * * @param password specifies the account password for this user
     * * @param email specifies the email address for this user
     * * @param fn specifies the first name for this user
     * * @param mn specifies the middle name for this user
     * * @param ln specifies the last name for this user
     * * @param pfn specifies the preferred first name for this user
     * * @param r1 specifies the the Admin attribute (TRUE or FALSE) for this user
     * * @param r2 specifies the the Staff attribute (TRUE or FALSE) for this user
     * * @param r3 specifies the the Student attribute (TRUE or FALSE) for this user
     * */
    // Constructor to initialize a new User object with userName, password, and role.
    public User(String userName, String password, String email, String fn, String mn, String ln, String pfn, boolean r1, boolean r2, boolean r3) {
        this.userName = userName;
        this.password = password;
        this.emailAddress = email;
        this.firstName = fn;
        this.middleName = mn;
        this.lastName = ln;
        this.preferredFirstName = pfn;
        this.adminRole = r1;
        this.staffRole = r2;
        this.studentRole = r3;
    }

    
    /*****
     * <p> Method: void setAdminRole(boolean role) </p>
     * * <p> Description: This setter defines the Admin role attribute. </p>
     * * @param role is a boolean that specifies if this user in playing the Admin role.
     * */
    // Sets the role of the Admin user.
    public void setAdminRole(boolean role) {
    	this.adminRole=role;
    }

    
    /*****
     * <p> Method: void setRole1User(boolean role) </p>
     * * <p> Description: This setter defines the Staff role attribute. </p>
     * * @param role is a boolean that specifies if this user in playing the Staff role.
     * */
    // Sets the role1 user.
    public void setRole1User(boolean role) {
    	this.staffRole=role;
    }

    
    /*****
     * <p> Method: void setRole2User(boolean role) </p>
     * * <p> Description: This setter defines the Student role attribute. </p>
     * * @param role is a boolean that specifies if this user in playing the Student role.
     * */
    // Sets the role2 user.
    public void setRole2User(boolean role) {
    	this.studentRole=role;
    }

    
    /*****
     * <p> Method: String getUserName() </p>
     * * <p> Description: This getter returns the UserName. </p>
     * * @return a String of the UserName
     * */
    // Gets the current value of the UserName.
    public String getUserName() { return userName; }

    
    /*****
     * <p> Method: String getPassword() </p>
     * * <p> Description: This getter returns the Password. </p>
     * * @return a String of the password
	 *
     */
    // Gets the current value of the Password.
    public String getPassword() { return password; }

    
    /*****
     * <p> Method: String getFirstName() </p>
     * * <p> Description: This getter returns the FirstName. </p>
     * * @return a String of the FirstName
	 *
     */
    // Gets the current value of the FirstName.
    public String getFirstName() { return firstName; }

    
    /*****
     * <p> Method: String getMiddleName() </p>
     * * <p> Description: This getter returns the MiddleName. </p>
     * * @return a String of the MiddleName
	 *
     */
    // Gets the current value of the Student role attribute.
    public String getMiddleName() { return middleName; }

    
    /*****
     * <p> Method: String getLasteName() </p>
     * * <p> Description: This getter returns the LastName. </p>
     * * @return a String of the LastName
	 *
     */
    // Gets the current value of the Student role attribute.
    public String getLastName() { return lastName; }

    
    /*****
     * <p> Method: String getPreferredFirstName() </p>
     * * <p> Description: This getter returns the PreferredFirstName. </p>
     * * @return a String of the PreferredFirstName
	 *
     */
    // Gets the current value of the Student role attribute.
    public String getPreferredFirstName() { return preferredFirstName; }

    
    /*****
     * <p> Method: String getEmailAddress() </p>
     * * <p> Description: This getter returns the EmailAddress. </p>
     * * @return a String of the EmailAddress
	 *
     */
    // Gets the current value of the Student role attribute.
    public String getEmailAddress() { return emailAddress; }

    /*****
     * <p> Method: void setUserName(String s) </p>
     * * <p> Description: This setter defines the userName attribute. </p>
     * * @param s is a String that specifies the userName.
     */
    // Sets the username.
    public void setUserName(String s) { userName = s; }

    /*****
     * <p> Method: void setPassword(String s) </p>
     * * <p> Description: This setter defines the password attribute. </p>
     * * @param s is a String that specifies the password.
     */
    // Sets the password.
    public void setPassword(String s) { password = s; }

    /*****
     * <p> Method: void setFirstName(String s) </p>
     * * <p> Description: This setter defines the firstName attribute. </p>
     * * @param s is a String that specifies the firstName.
     */
    // Sets the first name.
    public void setFirstName(String s) { firstName = s; }

    /*****
     * <p> Method: void setMiddleName(String s) </p>
     * * <p> Description: This setter defines the middleName attribute. </p>
     * * @param s is a String that specifies the middleName.
     */
    // Sets the middle name.
    public void setMiddleName(String s) { middleName = s; }

    /*****
     * <p> Method: void setLastName(String s) </p>
     * * <p> Description: This setter defines the lastName attribute. </p>
     * * @param s is a String that specifies the lastName.
     */
    // Sets the last name.
    public void setLastName(String s) { lastName = s; }

    /*****
     * <p> Method: void setPreferredFirstName(String s) </p>
     * * <p> Description: This setter defines the preferredFirstName attribute. </p>
     * * @param s is a String that specifies the preferredFirstName.
     */
    // Sets the preferred first name.
    public void setPreferredFirstName(String s) { preferredFirstName = s; }

    /*****
     * <p> Method: void setEmailAddress(String s) </p>
     * * <p> Description: This setter defines the emailAddress attribute. </p>
     * * @param s is a String that specifies the emailAddress.
     */
    // Sets the email address.
    public void setEmailAddress(String s) { emailAddress = s; }

    
    /*****
     * <p> Method: boolean getAdminRole() </p>
     * * <p> Description: This getter returns the value of the Admin role attribute. </p>
     * * @return the current boolean value of the Admin role attribute.
	 *
     */
    // Gets the current value of the Admin role attribute.
    public boolean getAdminRole() { return adminRole; }

    
    /*****
     * <p> Method: boolean getNewRole1() </p>
     * * <p> Description: This getter returns the value of the Staff role attribute. </p>
     * * @return the current boolean value of the Staff role attribute.
	 *
     */
    // Gets the current value of the Staff role attribute.
	public boolean getNewRole1() { return staffRole; }

    
    /*****
     * <p> Method: boolean getNewRole2() </p>
     * * <p> Description: This getter returns the value of the Student role attribute. </p>
     * * @return the current boolean value of the Student role attribute.
	 *
     */
    // Gets the current value of the Student role attribute.
    public boolean getNewRole2() { return studentRole; }

        
    /*****
     * <p> Method: int getNumRoles() </p>
     * * <p> Description: This getter returns the number of roles this user plays. </p>
     * * @return a value 0 - 3 of the number of roles this user plays
	 *
     */
    // Gets the current value of the roles count.
    public int getNumRoles() {
    	int numRoles = 0;
    	if (adminRole) numRoles++;
    	if (staffRole) numRoles++;
    	if (studentRole) numRoles++;
    	return numRoles;
    }
}