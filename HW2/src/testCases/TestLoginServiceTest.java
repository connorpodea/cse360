package testCases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import loginRouting.LoginDestination;
import loginRouting.LoginResult;
import loginRouting.LoginService;

/**
 * Unit tests for the login routing business rules.
 *
 * <p>This test focuses on the requirement that a student login opens the
 * student board GUI and does not expose admin-only options.</p>
 */
public class TestLoginServiceTest {

    /**
     * Verifies that a student-only account routes to the student board and
     * hides admin options.
     */
    @Test
    public void loginAsStudentOpensStudentBoardAndHidesAdminOptions() {
        LoginService service = new LoginService();

        // This test uses a student-only account because the requirement
        // explicitly states that no admin options should be visible.
        LoginResult result = service.routeUser(false, true, false);

        assertEquals(LoginDestination.STUDENT_BOARD, result.getDestination());
        assertEquals("Student Board GUI", result.getDestinationDisplayName());
        assertFalse(result.isAdminOptionsVisible());
    }
}