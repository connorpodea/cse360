package guiReplyManagement;

import database.Database;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * Controller for handling reply-related actions from the UI.
 * Connects the reply views with the model and database.
 */
public class ControllerReplyManagement {
	protected static Database database = applicationMain.FoundationsMain.database;
	
	/**
	 * Handles creating a new reply from the reply form.
	 */
	protected static void performCreateReply() {
		String body = ViewReplyManagement.text_Body.getText();
		String author = ViewReplyManagement.theUser.getUserName();
		int parentPostId = ViewReplyManagement.currentPostId;

		// Basic checks so we don't save empty or invalid replies
		if (body.isEmpty()) {
			showAlert("Validation Error", "Reply body cannot be empty.");
			return;
		}
		
		if (parentPostId <= 0) {
			showAlert("Validation Error", "Cannot reply to a post that does not exist.");
			return;
		}

		int id = ModelReplyManagement.getNextReplyId();
		entityClasses.Reply newReply = ModelReplyManagement.createReply(id, parentPostId, body, author);
		
		try {
	        database.saveReply(newReply); 
	        showAlert("Success", "Reply added!");

	        // Reload replies so memory matches what is in the database
	        ModelReplyManagement.getReplyStorage().clearAll();
	        for (entityClasses.Reply r : database.loadAllReplies()) {
	            ModelReplyManagement.getReplyStorage().addReply(r);
	        }

	    } catch (java.sql.SQLException e) {
	        showAlert("Error", "Failed to save reply.");
	    }
		
		// Clear input after submitting
		ViewReplyManagement.text_Body.clear();
		
		// Go back to the post view after submitting
		guiPostManagement.ViewPostManagement.displayPostManagement(
	            ViewReplyManagement.theStage, 
	            ViewReplyManagement.theUser
	    );
	}
	
	/**
	 * Deletes the currently selected reply.
	 */
	protected static void performDeleteReply() {
        try {
            Alert confirm = new Alert(AlertType.CONFIRMATION);
            confirm.setTitle("Delete Reply");
            confirm.setHeaderText("Are you sure?");
            confirm.setContentText("Deleting this reply will remove it.");

            java.util.Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Remove reply from memory and database
                ModelReplyManagement.getReplyStorage().deleteReply(ViewReplyList.currentReplyId);
                database.deleteReply(ViewReplyList.currentReplyId);
                
                // Update the UI so the deleted reply disappears
                ViewReplyList.refreshReplyList();
                ViewReplyList.area_ReplyBody.clear();
                showAlert("Success", "Reply deleted.");
            }
        } catch (Exception e) {
            showAlert("Error", "Could not delete reply.");
        }
    }

    /**
     * Allows editing the currently selected reply.
     */
    protected static void performEditReply() {
        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog(
            ViewReplyList.area_ReplyBody.getText()
        );
        dialog.setTitle("Edit Reply");
        dialog.setHeaderText("Update your reply:");

        dialog.showAndWait().ifPresent(newBody -> {
            if (!newBody.isEmpty()) {
                try {
                    // Save changes to both memory and database
                    ModelReplyManagement.getReplyStorage().updateReply(ViewReplyList.currentReplyId, newBody);
                    database.updateReply(ViewReplyList.currentReplyId, newBody);
                    
                    // Update what the user sees right away
                    ViewReplyList.area_ReplyBody.setText(newBody);
                    showAlert("Success", "Reply updated.");
                } catch (Exception e) {
                    showAlert("Error", "Update failed.");
                }
            }
        });
    }

    /**
     * Goes back to the role 2 home screen.
     */
	protected static void performBack() {
		guiRole2.ViewRole2Home.displayRole2Home(ViewReplyManagement.theStage, ViewReplyManagement.theUser);
	}

	// Simple helper for pop-up messages
	private static void showAlert(String title, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
