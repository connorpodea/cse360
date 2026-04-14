package MVCPostManagement;

import entityClasses.Post;
import database.Database;
import java.sql.SQLException;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

/*******
 * <p> Title: ControllerPostManagement Class </p>
 * * <p> Description: Handles user actions for creating, viewing, editing,
 * deleting, and replying to posts. This class acts as the bridge between 
 * the View and the Model in the MVC pattern. </p>
 */
public class ControllerPostManagement {
	
	/*****
	 * <p> Method: ControllerPostManagement() </p>
	 * * <p> Description: Default constructor for the controller. </p>
	 */
	public ControllerPostManagement() {}

	/** Message displayed when a post has been logically deleted */
	private static final String DELETED_POST_MESSAGE = "This post was deleted. You can still see replies";

	/** Reference to the system singleton database */
	protected static Database database = applicationMain.FoundationsMain.database;
	
	/*****
	 * <p> Method: performCreatePost </p>
	 * * <p> Description: Creates a new post using values retrieved from the UI. 
	 * It validates length requirements and ensures all fields are filled before 
	 * persisting to the database. </p>
	 */
	public static void performCreatePost() {
		int id = ModelPostManagement.getNextPostId();
		String title = ViewCreatePost.text_Title.getText(); 
		String body = ViewCreatePost.text_Body.getText();   
		String category = ViewCreatePost.combo_Category.getValue();
		String author = ViewPostManagement.theUser.getUserName();

		// Prevent empty posts from being created
		if (title.isEmpty() || body.isEmpty() || category == null || category.isEmpty()) {
			showAlert("Validation Error", "All fields including Category must be filled.");
			return;
		}
		
		// Basic length checks to match requirements
		if (title.length() > 200) {
			showAlert("Validation Error", "Title cannot exceed 200 characters");
			return;
		}
		
		if (body.length() > 5000) {
			showAlert("Validation Error", "Body cannot exceed 5000 characters");
			return;
		}

		entityClasses.Post newPost = ModelPostManagement.createPost(id, title, body, author, category);
		
		try {
			// Save to database so it persists
			database.savePost(newPost); 
			showAlert("Success", "Post created successfully!");
		} catch (java.sql.SQLException e) {
			// If DB fails, user still gets feedback
			showAlert("Database Error", "The post was created in memory but failed to save to disk.");
			e.printStackTrace();
		}
		
		// Clear fields so user can enter a new post
		ViewCreatePost.text_Title.clear();
		ViewCreatePost.text_Body.clear();
		
		ViewPostManagement.displayPostManagement(ViewPostManagement.theStage, ViewPostManagement.theUser);
	}
	
	/*****
	 * <p> Method: displaySelectedPost </p>
	 * * <p> Description: Updates the UI to show the full content of the post 
	 * currently selected in the list. It also determines visibility of Edit, 
	 * Delete, and Whisper buttons based on user roles and post status. </p>
	 */
	protected static void displaySelectedPost() {
		boolean isStaffOrAdmin = ViewPostManagement.theUser.getAdminRole() || 
                ViewPostManagement.theUser.getNewRole1(); // staff role
		
		// Get post by ID instead of relying on strings
	    entityClasses.Post p = ModelPostManagement.getPostStorage()
	            .getPostById(ViewPostManagement.currentPostId);
	    
	    if (p != null) {
	        ViewPostManagement.label_FullTitle.setText(p.isDeleted() ? DELETED_POST_MESSAGE : "Title: " + p.getTitle());
	        ViewPostManagement.label_FullAuthor.setText("Author: " + p.getAuthor());
	        ViewPostManagement.label_FullTimestamp.setText("Posted: " + p.getFormattedTimestamp());
	        ViewPostManagement.area_FullBody.setText(p.isDeleted() ? DELETED_POST_MESSAGE : p.getBody());

	        // Update reply count so user can see activity
	        int count = MVCReplyManagement.ModelReplyManagement.getReplyStorage()
	                        .getRepliesForPost(p.getId()).size();
	        ViewPostManagement.button_ViewReplies.setText("View Replies (" + count + ")");

	        // Only allow editing if user owns the post, is admin, or is staff
	        boolean canModify = ViewPostManagement.theUser.getUserName().equals(p.getAuthor()) 
	                        || ViewPostManagement.theUser.getAdminRole()
	                        || ViewPostManagement.theUser.getNewRole1();
	        ViewPostManagement.button_Edit.setVisible(canModify && !p.isDeleted());
	        ViewPostManagement.button_Delete.setVisible(canModify && !p.isDeleted());
	        
	        if (ViewPostManagement.button_Whisper != null) {
	            ViewPostManagement.button_Whisper.setVisible(isStaffOrAdmin);
	        }
	    }
	}
    
    /*****
     * <p> Method: performReply </p>
     * * <p> Description: Transitions the UI to the reply management screen 
     * for the selected post. </p>
     */
	protected static void performReply() {
        if (ViewPostManagement.currentPostId != -1) {
            MVCReplyManagement.ViewReplyManagement.displayReplyManagement(
                ViewPostManagement.theStage, 
                ViewPostManagement.theUser, 
                ViewPostManagement.currentPostId
            );
        } else {
            showAlert("Selection Error", "Please select a post before replying.");
        }
    }
    
    /*****
     * <p> Method: performDelete </p>
     * * <p> Description: Marks the selected post as deleted in both memory 
     * and the database. This is a logical delete, preserving replies. </p>
     */
    protected static void performDelete() {
        int idToDelete = ViewPostManagement.currentPostId;
        
        try {
            Post post = ModelPostManagement.getPostStorage().getPostById(idToDelete);
            Alert confirm = new Alert(AlertType.CONFIRMATION);
            confirm.setTitle("Delete Post");
            confirm.setHeaderText("Are you sure?");
            confirm.setContentText("Deleting this post keeps all replies and marks the post as deleted.");

            java.util.Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                post.setBody(DELETED_POST_MESSAGE);
                post.setTitle("Post Deleted");
                post.markDeleted();
                database.markPostDeleted(idToDelete, DELETED_POST_MESSAGE);
                ViewPostManagement.refreshPostList();
                displaySelectedPost();
            }
        } catch (Exception e) {
            showAlert("Error", "Could not delete post.");
        }
    }
    
    /*****
     * <p> Method: performEdit </p>
     * * <p> Description: Opens a dialog to allow the user to modify the body 
     * of their post. Updates both the database and UI. </p>
     */
    protected static void performEdit() {
        Post post = ModelPostManagement.getPostStorage()
                .getPostById(ViewPostManagement.currentPostId);

        if (post.isDeleted()) {
            showAlert("Error", "Deleted posts cannot be updated.");
            return;
        }

        String currentBody = post.getBody();

        javafx.scene.control.TextInputDialog dialog = 
                new javafx.scene.control.TextInputDialog(currentBody);
        dialog.setTitle("Edit Post");
        dialog.setHeaderText("Modify your post content:");
        dialog.setContentText("Body:");

        dialog.showAndWait().ifPresent(newBody -> {
            if (newBody.trim().isEmpty()) {
                showAlert("Validation Error", "Post body cannot be empty.");
            } else {
                try {
                    // Update memory version
                    post.setBody(newBody);
                    
                    // Update database version
                    database.updatePost(ViewPostManagement.currentPostId, newBody);
                    
                    // Update UI text
                    ViewPostManagement.area_FullBody.setText(post.isDeleted() ? DELETED_POST_MESSAGE : newBody);
                    showAlert("Success", "Post updated successfully!");
                } catch (java.sql.SQLException e) {
                    showAlert("Error", "Failed to save the edit to the database.");
                    e.printStackTrace();
                }
            }
        });
    }

    /*****
     * <p> Method: performBack </p>
     * * <p> Description: Returns the user to the Role 2 (Student) home screen. </p>
     */
	protected static void performBack() {
		guiRole2.ViewRole2Home.displayRole2Home(
		        ViewPostManagement.theStage, 
		        ViewPostManagement.theUser);
	}

	/*****
	 * <p> Method: showAlert </p>
	 * * <p> Description: Helper method to display a JavaFX Information Alert. </p>
	 */
	private static void showAlert(String title, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	/*****
	 * <p> Method: performReplyToSelected </p>
	 * * <p> Description: Similar to performReply, but specifically parses the 
	 * selected string from the ListView to identify the Post ID. </p>
	 */
	protected static void performReplyToSelected() {
        String selected = ViewPostManagement.list_Posts
                .getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            showAlert("Selection Error", "Please select a post from the list to reply.");
            return;
        }

        try {
            int postId = Integer.parseInt(selected.split(":")[0]);
            MVCReplyManagement.ViewReplyManagement.displayReplyManagement(
                ViewPostManagement.theStage, 
                ViewPostManagement.theUser, 
                postId);
        } catch (Exception e) {
            showAlert("Error", "Could not parse Post ID.");
        }
    }
	
	/*****
	 * <p> Method: performWhisperFeedback </p>
	 * * <p> Description: Opens a custom dialog to allow staff to provide 
	 * private feedback to a student. Feedback is saved as a Request 
	 * entity for tracking. </p>
	 */
	protected static void performWhisperFeedback() {
	    Post p = ModelPostManagement.getPostStorage()
	            .getPostById(ViewPostManagement.currentPostId);
	    
	    if (p == null) return;

	    // 1. Create a Dialog UI components
	    javafx.scene.control.TextArea feedbackArea = new javafx.scene.control.TextArea();
	    javafx.scene.control.CheckBox anonCheck = new javafx.scene.control.CheckBox("Hide my name (Anonymous)");
	    VBox content = new VBox(10); 

	    content.getChildren().addAll(
	     new Label("Provide private feedback to " + p.getAuthor() + ":"),
	     feedbackArea, 
	    anonCheck
	     );

	    Alert dialog = new Alert(AlertType.CONFIRMATION);
	    dialog.setTitle("Staff Whisper System");
	    dialog.getDialogPane().setContent(content);

	    dialog.showAndWait().ifPresent(response -> {
	        if (response == ButtonType.OK && !feedbackArea.getText().trim().isEmpty()) {
	            try {
	                String staffName = anonCheck.isSelected() ? "Anonymous Staff" : ViewPostManagement.theUser.getUserName();
	                String messageBody = "[PRIVATE FEEDBACK regarding post #" + p.getId() + "]: " + feedbackArea.getText();
	                
	                // Store "Whisper" in the Request table for student visibility
	                database.saveRequest(
	                    "Feedback: " + p.getTitle(), 
	                    messageBody, 
	                    staffName
	                );
	                
	                showAlert("Sent", "Your feedback has been sent privately to " + p.getAuthor());
	            } catch (Exception e) {
	                showAlert("Error", "Failed to send whisper feedback.");
	            }
	        }
	    });
	}
	
	/*****
	 * <p> Method: performWhisper </p>
	 * * <p> Description: Advanced whisper function that saves feedback directly 
	 * to the post table and creates a request record for administrative transparency. </p>
	 */
	protected static void performWhisper() {
	    Post p = ModelPostManagement.getPostStorage().getPostById(ViewPostManagement.currentPostId);
	    if (p == null) return;

	    // 1. Setup UI
	    javafx.scene.control.TextArea feedbackArea = new javafx.scene.control.TextArea();
	    javafx.scene.control.CheckBox anonCheck = new javafx.scene.control.CheckBox("Hide my name (Anonymous)");
	    javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(10, 
	        new javafx.scene.control.Label("Provide private feedback to " + p.getAuthor() + ":"),
	        feedbackArea, 
	        anonCheck
	    );

	    Alert dialog = new Alert(AlertType.CONFIRMATION);
	    dialog.setTitle("Staff Whisper System");
	    dialog.getDialogPane().setContent(content);

	    // 2. Handle Action
	    dialog.showAndWait().ifPresent(response -> {
	        if (response == ButtonType.OK && !feedbackArea.getText().trim().isEmpty()) {
	            try {
	                String feedback = feedbackArea.getText().trim();
	                boolean isAnon = anonCheck.isSelected();
	                
	                // Save specifically to postDB columns
	                database.updatePostFeedback(p.getId(), feedback, isAnon);
	                
	                // Record action in Request table for Admin visibility (Transparency)
	                database.saveRequest(
	                    "Feedback Sent to " + p.getAuthor(),
	                    "Staff provided feedback on post #" + p.getId() + ": " + feedback,
	                    isAnon ? "Anonymous Staff" : ViewPostManagement.theUser.getUserName()
	                );

	                showAlert("Sent", "Feedback saved successfully.");
	            } catch (SQLException e) {
	                showAlert("Error", "Database failure while sending feedback.");
	            }
	        }
	    });
	}
}