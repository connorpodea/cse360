package testCases;

import entityClasses.User;

/**
 * <p> Title: UserTest Class </p>
 *
 * <p> Description: This class performs boundary value testing and
 * coverage testing on the User entity. It specifically targets potential
 * weaknesses such as CWE-20 and CWE-476. </p>
 *
 * <p> Statement Coverage: This test executes every getter and setter
 * at least once to achieve full statement coverage of the User class. </p>
 *
 * <p> Branch Coverage: The getNumRoles method is tested with multiple role
 * combinations to ensure all conditional branches are executed. </p>
 */
public class UserTest {

    /**
     * Main entry point to run automated tests for the User entity.
     */
    public static void main() {
        System.out.println("Starting User Entity BVT and Coverage Tests\n");

        User user = new User("cpodea", "Password1234!", "cpodea@asu.edu",
                             "Connor", "T", "Podea", "Connor",
                             true, false, true);

        // TEST 1: Constructor Verification
        System.out.println("TEST 1: Constructor Verification");
        boolean t1Pass = true;

        if (!"cpodea".equals(user.getUserName())) {
            System.out.println("  FAIL: userName mismatch.");
            t1Pass = false;
        }
        if (!"Password1234!".equals(user.getPassword())) {
            System.out.println("  FAIL: password mismatch.");
            t1Pass = false;
        }
        if (!"cpodea@asu.edu".equals(user.getEmailAddress())) {
            System.out.println("  FAIL: emailAddress mismatch.");
            t1Pass = false;
        }
        if (!"Connor".equals(user.getFirstName())) {
            System.out.println("  FAIL: firstName mismatch.");
            t1Pass = false;
        }
        if (!"T".equals(user.getMiddleName())) {
            System.out.println("  FAIL: middleName mismatch.");
            t1Pass = false;
        }
        if (!"Podea".equals(user.getLastName())) {
            System.out.println("  FAIL: lastName mismatch.");
            t1Pass = false;
        }
        if (!"Connor".equals(user.getPreferredFirstName())) {
            System.out.println("  FAIL: preferredFirstName mismatch.");
            t1Pass = false;
        }
        if (!user.getAdminRole()) {
            System.out.println("  FAIL: adminRole mismatch.");
            t1Pass = false;
        }
        if (user.getNewRole1()) {
            System.out.println("  FAIL: staffRole mismatch.");
            t1Pass = false;
        }
        if (!user.getNewRole2()) {
            System.out.println("  FAIL: studentRole mismatch.");
            t1Pass = false;
        }

        if (t1Pass) {
            System.out.println("  PASS: All constructor fields verified correctly.");
        }
        System.out.println();


        // TEST 2: Role Count Logic (Branch Coverage — mixed roles)        
        System.out.println("TEST 2: getNumRoles with 2 Active Roles (Expected: 2)");
        if (user.getNumRoles() == 2) {
            System.out.println("  PASS: Role count is 2.");
        } else {
            System.out.println("  FAIL: Expected 2 but got " + user.getNumRoles());
        }
        System.out.println();

        // TEST 3: Setter/Getter Cycle — emailAddress
        System.out.println("TEST 3: Setter/Getter Cycle for emailAddress");
        user.setEmailAddress("newemail@asu.edu");
        if ("newemail@asu.edu".equals(user.getEmailAddress())) {
            System.out.println("  PASS: Email setter/getter cycle functional.");
        } else {
            System.out.println("  FAIL: Email getter did not return updated value.");
        }
        System.out.println();

        // TEST 4: Setter/Getter Cycle — remaining fields (Statement Coverage)
        System.out.println("TEST 4: Full Setter/Getter Coverage for Remaining Fields");
        boolean t4Pass = true;

        user.setUserName("newuser");
        if (!"newuser".equals(user.getUserName())) {
            System.out.println("  FAIL: userName setter/getter mismatch.");
            t4Pass = false;
        }
        user.setPassword("NewPass99!");
        if (!"NewPass99!".equals(user.getPassword())) {
            System.out.println("  FAIL: password setter/getter mismatch.");
            t4Pass = false;
        }
        user.setFirstName("Jane");
        if (!"Jane".equals(user.getFirstName())) {
            System.out.println("  FAIL: firstName setter/getter mismatch.");
            t4Pass = false;
        }
        user.setMiddleName("M");
        if (!"M".equals(user.getMiddleName())) {
            System.out.println("  FAIL: middleName setter/getter mismatch.");
            t4Pass = false;
        }
        user.setLastName("Doe");
        if (!"Doe".equals(user.getLastName())) {
            System.out.println("  FAIL: lastName setter/getter mismatch.");
            t4Pass = false;
        }
        user.setPreferredFirstName("Jan");
        if (!"Jan".equals(user.getPreferredFirstName())) {
            System.out.println("  FAIL: preferredFirstName setter/getter mismatch.");
            t4Pass = false;
        }

        if (t4Pass) {
            System.out.println("  PASS: All remaining setter/getter cycles functional.");
        }
        System.out.println();

        // TEST 5: Boundary — Minimum Role Count (0 Roles)
        System.out.println("TEST 5: Boundary — Minimum Role Count (Expected: 0)");
        user.setAdminRole(false);
        user.setRole1User(false);
        user.setRole2User(false);
        if (user.getNumRoles() == 0) {
            System.out.println("  PASS: Minimum boundary met.");
        } else {
            System.out.println("  FAIL: Expected 0 but got " + user.getNumRoles());
        }
        System.out.println();

        // TEST 6: Boundary — Maximum Role Count (3 Roles)
        System.out.println("TEST 6: Boundary — Maximum Role Count (Expected: 3)");
        user.setAdminRole(true);
        user.setRole1User(true);
        user.setRole2User(true);
        if (user.getNumRoles() == 3) {
            System.out.println("  PASS: Maximum boundary met.");
        } else {
            System.out.println("  FAIL: Expected 3 but got " + user.getNumRoles());
        }
        System.out.println();

        // TEST 7: Empty String Boundary — setUserName (CWE-20)
        System.out.println("TEST 7: Boundary — Empty String Username (Input: \"\")");
        user.setUserName("");
        if (user.getUserName().equals("")) {
            System.out.println("  OBSERVED: System accepted a blank username. "
                             + "(CWE-20 Risk — identity field left empty.)");
        } else {
            System.out.println("  OBSERVED: System rejected or transformed blank username. "
                             + "Stored value: \"" + user.getUserName() + "\"");
        }
        System.out.println();

        // TEST 8: Null String Boundary — setUserName (CWE-476)
        System.out.println("TEST 8: Boundary — Null Username (Input: null)");
        try {
            user.setUserName(null);
            if (user.getUserName() == null) {
                System.out.println("  OBSERVED: System allowed userName to become null. "
                                 + "(CWE-476 Risk — null dereference possible downstream.)");
            } else {
                System.out.println("  OBSERVED: System blocked null assignment. "
                                 + "Stored value: \"" + user.getUserName() + "\"");
            }
        } catch (Exception e) {
            System.out.println("  OBSERVED: System threw " + e.getClass().getSimpleName()
                             + " on null input. (CWE-476 — unhandled exception risk.)");
        }

        System.out.println("\nUser Testing Complete");
    }
}