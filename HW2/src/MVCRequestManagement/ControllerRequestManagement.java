package MVCRequestManagement;

import entityClasses.Request;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller for the staff request system.
 * Handles all button actions: creating, closing, and reopening requests.
 * Only admins can close requests. Staff can reopen closed requests.
 */
public class ControllerRequestManagement {

    /** Creates the controller object. */
    public ControllerRequestManagement() {}

    private static database.Database database = applicationMain.FoundationsMain.database;

    /**
     * Loads all requests from the database into memory and refreshes the list view.
     * Called every time the request page is shown so the list stays current.
     */
    public static void loadRequestsFromDatabase() {
        try {
            List<String[]> rows = database.loadAllRequests();
            List<entityClasses.Request> loaded = new java.util.ArrayList<>();
            for (String[] row : rows) {
                // row: [id, title, description, author, status, resolutionNotes, originalRequestId, timestamp]
                int originalId = -1;
                try { originalId = Integer.parseInt(row[6]); } catch (NumberFormatException e) { originalId = -1; }

                loaded.add(new entityClasses.Request(
                    Integer.parseInt(row[0]),
                    row[1],
                    row[2],
                    row[3],
                    row[4],
                    row[5],
                    originalId,
                    java.time.LocalDateTime.parse(row[7])
                ));
            }
            ModelRequestManagement.setRequestList(loaded);
        } catch (SQLException e) {
            showAlert("Database Error", "Could not load requests from the database.");
            e.printStackTrace();
        }
    }

    /**
     * Creates a new request using values from ViewCreateRequest.
     * Validates input before saving to database.
     */
    protected static void performCreateRequest() {
        String title = ViewCreateRequest.text_Title.getText().trim();
        String description = ViewCreateRequest.text_Description.getText().trim();

        // Basic validation
        if (title.isEmpty() || description.isEmpty()) {
            showAlert("Validation Error", "Title and description cannot be empty.");
            return;
        }
        if (title.length() > 200) {
            showAlert("Validation Error", "Title cannot exceed 200 characters.");
            return;
        }

        String author = ViewCreateRequest.theUser.getUserName();
        int id = ModelRequestManagement.getNextRequestId();

        Request newRequest = ModelRequestManagement.createRequest(id, title, description, author);

        try {
            database.saveRequest(title, description, author);
            showAlert("Success", "Request submitted successfully!");
        } catch (SQLException e) {
            showAlert("Database Error", "Request saved in memory but failed to save to disk.");
            e.printStackTrace();
        }

        // Clear fields and go back to the list
        ViewCreateRequest.text_Title.clear();
        ViewCreateRequest.text_Description.clear();
        ViewRequestManagement.displayRequestManagement(
            ViewCreateRequest.theStage, ViewCreateRequest.theUser);
    }

    /**
     * Displays the selected request's details in the right panel.
     * Shows or hides close/reopen buttons based on role and status.
     */
    protected static void displaySelectedRequest() {
        Request r = ModelRequestManagement.getRequestById(ViewRequestManagement.currentRequestId);
        if (r == null) return;

        ViewRequestManagement.label_FullTitle.setText(r.getTitle());
        ViewRequestManagement.label_FullAuthor.setText("Submitted by: " + r.getAuthor());
        ViewRequestManagement.label_FullTimestamp.setText("Submitted: " + r.getFormattedTimestamp());
        ViewRequestManagement.area_FullDescription.setText(r.getDescription());
        ViewRequestManagement.label_Status.setText("Status: " + r.getStatus());

        // Show resolution notes if the request has been closed
        if (r.getResolutionNotes() != null && !r.getResolutionNotes().isEmpty()) {
            ViewRequestManagement.label_ResolutionNotes.setText("Resolution: " + r.getResolutionNotes());
        } else {
            ViewRequestManagement.label_ResolutionNotes.setText("");
        }

        // Show link to original request if this is a reopened one
        if (r.getOriginalRequestId() != -1) {
            ViewRequestManagement.label_OriginalLink.setText(
                "Reopened from Request #" + r.getOriginalRequestId());
        } else {
            ViewRequestManagement.label_OriginalLink.setText("");
        }

        boolean isAdmin = ViewRequestManagement.theUser.getAdminRole();
        boolean isOpen = r.getStatus().equals("OPEN") || r.getStatus().equals("REOPENED");
        boolean isClosed = r.getStatus().equals("CLOSED");

        // Only admin can close, only staff can reopen
        ViewRequestManagement.button_Close.setVisible(isAdmin && isOpen);
        ViewRequestManagement.button_Reopen.setVisible(!isAdmin && isClosed);
    }

    /**
     * Closes the currently selected request.
     * Prompts the admin for resolution notes before closing.
     * Only admins can perform this action.
     */
    protected static void performClose() {
        int id = ViewRequestManagement.currentRequestId;
        if (id == -1) {
            showAlert("Selection Error", "Please select a request first.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Close Request");
        dialog.setHeaderText("Document what action was taken:");
        dialog.setContentText("Resolution notes:");

        dialog.showAndWait().ifPresent(notes -> {
            if (notes.trim().isEmpty()) {
                showAlert("Validation Error", "Resolution notes cannot be empty.");
                return;
            }
            try {
                database.closeRequest(id, notes);
                ModelRequestManagement.updateRequestStatus(id, "CLOSED");
                Request r = ModelRequestManagement.getRequestById(id);
                if (r != null) r.setResolutionNotes(notes);
                showAlert("Success", "Request has been closed.");
                ViewRequestManagement.refreshRequestList();
                displaySelectedRequest();
            } catch (SQLException e) {
                showAlert("Database Error", "Could not close the request.");
                e.printStackTrace();
            }
        });
    }

    /**
     * Reopens a closed request as a new request linked to the original.
     * Staff provides a description explaining why they are reopening it.
     */
    protected static void performReopen() {
        int originalId = ViewRequestManagement.currentRequestId;
        if (originalId == -1) {
            showAlert("Selection Error", "Please select a request first.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Reopen Request");
        dialog.setHeaderText("Explain why you are reopening this request:");
        dialog.setContentText("Description:");

        dialog.showAndWait().ifPresent(description -> {
            if (description.trim().isEmpty()) {
                showAlert("Validation Error", "Description cannot be empty.");
                return;
            }
            try {
                String author = ViewRequestManagement.theUser.getUserName();
                database.reopenRequest(originalId, description, author);
                // Reload so the new reopened request shows up in the list
                loadRequestsFromDatabase();
                showAlert("Success", "Request has been reopened.");
                ViewRequestManagement.refreshRequestList();
            } catch (SQLException e) {
                showAlert("Database Error", "Could not reopen the request.");
                e.printStackTrace();
            }
        });
    }

    /**
     * Navigates back to the staff home page.
     */
    protected static void performBack() {
        guiRole1.ViewRole1Home.displayRole1Home(
            ViewRequestManagement.theStage,
            ViewRequestManagement.theUser);
    }

    /**
     * Shows a simple popup alert.
     * @param title   the alert title
     * @param message the alert message
     */
    private static void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}