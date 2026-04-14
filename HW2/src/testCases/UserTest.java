package testCases;

import entityClasses.User;

/**
 * Coverage tests for the User class.
 */
public class UserTest {

    public static void main(String[] args) {
        testConstructorAndGetters();
        testSettersWork();
        testRoleCounts();
        testDefaultConstructor();
        System.out.println("All User tests passed.");
    }

    private static void testConstructorAndGetters() {
        User u = new User(
                "sam",
                "pw123",
                "sam@example.com",
                "Sam",
                "Q",
                "Student",
                "Sammy",
                true,
                false,
                true
        );

        assertEquals("userName", "sam", u.getUserName());
        assertEquals("password", "pw123", u.getPassword());
        assertEquals("email", "sam@example.com", u.getEmailAddress());
        assertEquals("firstName", "Sam", u.getFirstName());
        assertEquals("middleName", "Q", u.getMiddleName());
        assertEquals("lastName", "Student", u.getLastName());
        assertEquals("preferredFirstName", "Sammy", u.getPreferredFirstName());
        assertTrue("adminRole", u.getAdminRole());
        assertFalse("staffRole", u.getNewRole1());
        assertTrue("studentRole", u.getNewRole2());
    }

    private static void testSettersWork() {
        User u = new User("u1", "p1", "e1@example.com", "A", "B", "C", "D", false, false, false);

        u.setUserName("u2");
        u.setPassword("p2");
        u.setFirstName("F");
        u.setMiddleName("M");
        u.setLastName("L");
        u.setPreferredFirstName("P");
        u.setEmailAddress("e2@example.com");
        u.setAdminRole(true);
        u.setRole1User(true);
        u.setRole2User(true);

        assertEquals("setUserName", "u2", u.getUserName());
        assertEquals("setPassword", "p2", u.getPassword());
        assertEquals("setFirstName", "F", u.getFirstName());
        assertEquals("setMiddleName", "M", u.getMiddleName());
        assertEquals("setLastName", "L", u.getLastName());
        assertEquals("setPreferredFirstName", "P", u.getPreferredFirstName());
        assertEquals("setEmailAddress", "e2@example.com", u.getEmailAddress());
        assertTrue("setAdminRole", u.getAdminRole());
        assertTrue("setRole1User", u.getNewRole1());
        assertTrue("setRole2User", u.getNewRole2());
    }

    private static void testRoleCounts() {
        User none = new User("a", "b", "c", "d", "e", "f", "g", false, false, false);
        User one = new User("a", "b", "c", "d", "e", "f", "g", true, false, false);
        User two = new User("a", "b", "c", "d", "e", "f", "g", true, true, false);
        User three = new User("a", "b", "c", "d", "e", "f", "g", true, true, true);

        assertEquals("numRoles none", 0, none.getNumRoles());
        assertEquals("numRoles one", 1, one.getNumRoles());
        assertEquals("numRoles two", 2, two.getNumRoles());
        assertEquals("numRoles three", 3, three.getNumRoles());
    }

    private static void testDefaultConstructor() {
        User u = new User();
        assertEquals("default userName", null, u.getUserName());
        assertEquals("default password", null, u.getPassword());
        assertEquals("default email", null, u.getEmailAddress());
        assertEquals("default numRoles", 0, u.getNumRoles());
    }

    private static void assertEquals(String label, int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("FAIL: " + label + " expected " + expected + " but was " + actual);
        }
        System.out.println("PASS: " + label);
    }

    private static void assertEquals(String label, String expected, String actual) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError("FAIL: " + label + " expected [" + expected + "] but was [" + actual + "]");
        }
        System.out.println("PASS: " + label);
    }

    private static void assertTrue(String label, boolean condition) {
        if (!condition) {
            throw new AssertionError("FAIL: expected true for " + label);
        }
        System.out.println("PASS: " + label);
    }

    private static void assertFalse(String label, boolean condition) {
        if (condition) {
            throw new AssertionError("FAIL: expected false for " + label);
        }
        System.out.println("PASS: " + label);
    }
}