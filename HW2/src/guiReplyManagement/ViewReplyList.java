package guiReplyManagement;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import entityClasses.User;
import entityClasses.Reply;

/**
 * Shows the replies for a selected post.
 * This view supports assignment stories where users read, edit, and delete replies.
 */
public class ViewReplyList {
    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    // Left side list of replies
    protected static ListView<Reply> list_Replies = new ListView<>();
    protected static TextArea area_ReplyBody = new TextArea();
    protected static Button button_Back = new Button("Back to Post");
    
    // Search box for filtering replies
    protected static TextField text_SearchReplies = new TextField();
    
    protected static Button button_EditReply = new Button("Edit Reply");
    protected static Button button_DeleteReply = new Button("Delete Reply");
    protected static int currentReplyId = -1; 

    private static Pane theRootPane;
    private static Scene theScene;
    private static ViewReplyList theView;
    private static int currentPostId;

    /**
     * Displays the reply list for a specific post.
     *
     * @param ps the JavaFX stage used for the screen
     * @param user the signed-in user
     * @param postId the post whose replies should be shown
     */
    public static void displayReplyList(Stage ps, User user, int postId) {
        currentPostId = postId;
        if (theView == null) theView = new ViewReplyList();
        
        refreshReplyList();
        ps.setScene(theScene);
        ps.setTitle("Viewing Replies");
        ps.show();
    }

    /**
     * Builds the reply list UI.
     */
    private ViewReplyList() {
        theRootPane = new Pane();
        theScene = new Scene(theRootPane, width, height);

        Label label_Title = new Label("Post Replies");
        label_Title.setFont(Font.font("Arial", 24));
        label_Title.setLayoutX(20); label_Title.setLayoutY(20);

        // Search bar for filtering replies
        text_SearchReplies.setPromptText("Search reply contents...");
        text_SearchReplies.setLayoutX(20); text_SearchReplies.setLayoutY(60);
        text_SearchReplies.setPrefWidth(280);
        text_SearchReplies.setOnKeyReleased((_) -> refreshReplyList());

        // Sidebar for replies
        list_Replies.setLayoutX(20); list_Replies.setLayoutY(100);
        list_Replies.setPrefSize(280, height - 180);
        
        // Big reading area for reply body
        area_ReplyBody.setLayoutX(320); area_ReplyBody.setLayoutY(70);
        area_ReplyBody.setPrefSize(width - 350, height - 200);
        area_ReplyBody.setEditable(false);
        area_ReplyBody.setWrapText(true);
        
        // Back button
        button_Back.setLayoutX(20); button_Back.setLayoutY(height - 60);
        button_Back.setOnAction((_) -> {
            guiPostManagement.ViewPostManagement.displayPostManagement(
                guiPostManagement.ViewPostManagement.theStage, 
                guiPostManagement.ViewPostManagement.theUser);
        });
        
        // Edit/Delete buttons
        setupButtonUI(button_EditReply, 320, height - 110);
        setupButtonUI(button_DeleteReply, 450, height - 110);
        
        button_EditReply.setVisible(false);
        button_DeleteReply.setVisible(false);

        button_EditReply.setOnAction((_) -> ControllerReplyManagement.performEditReply());
        button_DeleteReply.setOnAction((_) -> ControllerReplyManagement.performDeleteReply());
      
        // Update right panel when a reply is selected
        list_Replies.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
            if (selected != null) {
                area_ReplyBody.setText(selected.getBody());
                currentReplyId = selected.getId(); 

                boolean canModify = guiPostManagement.ViewPostManagement.theUser.getUserName().equals(selected.getAuthor()) 
                                    || guiPostManagement.ViewPostManagement.theUser.getAdminRole();
                
                button_EditReply.setVisible(canModify);
                button_DeleteReply.setVisible(canModify);
            }
        });
        
        theRootPane.getChildren().addAll(label_Title, text_SearchReplies, list_Replies, 
                area_ReplyBody, button_Back, button_EditReply, button_DeleteReply);
    }
    
    /**
     * Formats the timestamp for display in the list.
     */
    private static String getFormattedTimestamp(entityClasses.Reply r) {
        java.time.format.DateTimeFormatter formatter = 
            java.time.format.DateTimeFormatter.ofPattern("MMM dd, hh:mm a");
        return r.getTimestamp().format(formatter);
    }
    
    // Simple layout helper for buttons
    private void setupButtonUI(Button b, double x, double y) {
        b.setFont(Font.font("Arial", 14));
        b.setMinWidth(120);
        b.setLayoutX(x);
        b.setLayoutY(y);
    }

    /**
     * Refreshes the reply list and applies the search filter.
     */
    public static void refreshReplyList() {
        list_Replies.getItems().clear();
        
        String filter = text_SearchReplies.getText().toLowerCase();

        java.util.List<Reply> postReplies =
                ModelReplyManagement.getReplyStorage().getRepliesForPost(currentPostId);

        for (Reply r : postReplies) {
            if (filter.isEmpty() || r.getBody().toLowerCase().contains(filter)) {
                list_Replies.getItems().add(r);
            }
        }

        list_Replies.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Reply r, boolean empty) {
                super.updateItem(r, empty);
                if (empty || r == null) {
                    setText(null);
                } else {
                    setText("From: " + r.getAuthor() + "\n" + getFormattedTimestamp(r));
                }
            }
        });
    }
}
