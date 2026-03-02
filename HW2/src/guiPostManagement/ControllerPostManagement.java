package guiPostManagement;

import entityClasses.Post;
import database.Database;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Handles user actions for creating, viewing, editing,
 * deleting, and replying to posts.
 */
public class ControllerPostManagement {

	protected static Database database = applicationMain.FoundationsMain.database;
	/**
	 * Creates a new post using the values from the UI.
	 * Performs basic validation before saving.
	 */
	protected static void performCreatePost() {
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
	
	/**
	 * Displays the selected post details in the main view.
	 */
	protected static void displaySelectedPost() {
	    // Get post by ID instead of relying on strings
	    entityClasses.Post p = ModelPostManagement.getPostStorage()
	            .getPostById(ViewPostManagement.currentPostId);
	    
	    if (p != null) {
	        ViewPostManagement.label_FullTitle.setText("Title: " + p.getTitle());
	        ViewPostManagement.label_FullAuthor.setText("Author: " + p.getAuthor());
	        ViewPostManagement.label_FullTimestamp.setText("Posted: " + p.getFormattedTimestamp());
	        ViewPostManagement.area_FullBody.setText(p.isDeleted() ? "[DELETED]" : p.getBody());

	        // Update reply count so user can see activity
	        int count = guiReplyManagement.ModelReplyManagement.getReplyStorage()
	                        .getRepliesForPost(p.getId()).size();
	        ViewPostManagement.button_ViewReplies.setText("View Replies (" + count + ")");

	        // Only allow editing if user owns the post or is admin
	        boolean canModify = ViewPostManagement.theUser.getUserName().equals(p.getAuthor()) 
	                            || ViewPostManagement.theUser.getAdminRole();
	        ViewPostManagement.button_Edit.setVisible(canModify && !p.isDeleted());
	        ViewPostManagement.button_Delete.setVisible(canModify && !p.isDeleted());
	    }
	}
    
    /**
     * Opens the reply screen for the selected post.
     */
	protected static void performReply() {
        if (ViewPostManagement.currentPostId != -1) {
            guiReplyManagement.ViewReplyManagement.displayReplyManagement(
                ViewPostManagement.theStage, 
                ViewPostManagement.theUser, 
                ViewPostManagement.currentPostId
            );
        } else {
            showAlert("Selection Error", "Please select a post before replying.");
        }
    }
    
    /**
     * Deletes the currently selected post.
     */
    protected static void performDelete() {
        int idToDelete = ViewPostManagement.currentPostId;
        
        try {
            Post post = ModelPostManagement.getPostStorage().getPostById(idToDelete);

            if (post == null) {
                showAlert("Error", "Could not find the selected post.");
                return;
            }

            post.markDeleted();

            ViewPostManagement.refreshPostList();
            displaySelectedPost();
            
            showAlert("Deleted", "Post has been marked as deleted.");
        } catch (Exception e) {
            showAlert("Error", "Could not mark the post as deleted.");
        }
    }
    
    /**
     * Allows editing of the selected post body.
     */
    protected static void performEdit() {
        Post post = ModelPostManagement.getPostStorage()
                .getPostById(ViewPostManagement.currentPostId);

        if (post == null) {
            showAlert("Error", "Could not find the selected post.");
            return;
        }

        if (post.isDeleted()) {
            showAlert("Validation Error", "Deleted posts cannot be edited.");
            return;
        }

        String currentBody = post.getBody();

        javafx.scene.control.TextInputDialog dialog = 
                new javafx.scene.control.TextInputDialog(currentBody);
        dialog.setTitle("Edit Post");
        dialog.setHeaderText("Modify your post content:");
        dialog.setContentText("Body:");

        dialog.showAndWait().ifPresent(newBody -> {
            if (!newBody.isEmpty()) {
                try {
                    // Update memory version
                    post.setBody(newBody);
                    
                    // Update database version
                    database.updatePost(ViewPostManagement.currentPostId, newBody);
                    
                    // Update UI text
                    ViewPostManagement.area_FullBody.setText(post.isDeleted() ? "[DELETED]" : newBody);
                    showAlert("Success", "Post updated successfully!");
                } catch (java.sql.SQLException e) {
                    showAlert("Error", "Failed to save the edit to the database.");
                    e.printStackTrace();
                }
            }
        });
    }

	protected static void performBack() {
		guiRole2.ViewRole2Home.displayRole2Home(
		        ViewPostManagement.theStage, 
		        ViewPostManagement.theUser);
	}

	/**
	 * Shows a basic popup message.
	 */
	private static void showAlert(String title, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	/**
	 * Opens reply screen using the selected post from the list.
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
            guiReplyManagement.ViewReplyManagement.displayReplyManagement(
                ViewPostManagement.theStage, 
                ViewPostManagement.theUser, 
                postId);
        } catch (Exception e) {
            showAlert("Error", "Could not parse Post ID.");
        }
    }
}
