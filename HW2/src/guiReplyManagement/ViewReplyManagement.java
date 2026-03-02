package guiReplyManagement;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import entityClasses.User;

/**
 * Shows the screen used to create a reply for a post.
 * This view supports assignment stories where users respond to posts.
 */
public class ViewReplyManagement {
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	// GUI Widgets for reply input
	protected static Label label_PageTitle = new Label("Add a Reply");
	protected static TextArea text_Body = new TextArea();
	protected static Button button_Submit = new Button("Submit Reply");
	protected static Button button_Back = new Button("Back");

	protected static Stage theStage;
	protected static User theUser;
	protected static int currentPostId; // tracks which post the reply belongs to
	private static Pane theRootPane;
	private static Scene theScene;
	private static ViewReplyManagement theView;

	/**
	 * Opens the reply creation screen for a specific post.
	 *
	 * @param ps the JavaFX stage used for the screen
	 * @param user the signed-in user
	 * @param postId the post that will receive the reply
	 */
	public static void displayReplyManagement(Stage ps, User user, int postId) {
		theStage = ps;
		theUser = user;
		currentPostId = postId;
		if (theView == null) theView = new ViewReplyManagement();
		
		theStage.setTitle("Reply Management");
		theStage.setScene(theScene);
		theStage.show();
	}

	/**
	 * Builds the UI for adding a reply.
	 */
	private ViewReplyManagement() {
		theRootPane = new Pane();
		theScene = new Scene(theRootPane, width, height);

		setupLabelUI(label_PageTitle, "Arial", 24, width, Pos.CENTER, 0, 20);

		Label label_Body = new Label("Your Reply:");
		setupLabelUI(label_Body, "Arial", 16, 100, Pos.BASELINE_LEFT, 50, 80);
		
		// Text area for the reply body
		text_Body.setLayoutX(50); 
		text_Body.setLayoutY(110);
		text_Body.setPrefRowCount(8);
		text_Body.setMinWidth(width - 100);

		// Submit reply button
		setupButtonUI(button_Submit, "Arial", 18, 150, Pos.CENTER, 50, 400);
		button_Submit.setOnAction((_) -> { ControllerReplyManagement.performCreateReply(); });

		// Back button to return to post view
		setupButtonUI(button_Back, "Arial", 18, 150, Pos.CENTER, 250, 400);
		button_Back.setOnAction((_) -> { 
			guiPostManagement.ViewPostManagement.displayPostManagement(
	            ViewReplyManagement.theStage, 
	            ViewReplyManagement.theUser
	        ); 
		});

		theRootPane.getChildren().addAll(label_PageTitle, label_Body, text_Body, button_Submit, button_Back);
	}

	// Small helper to keep label layout consistent
	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
		l.setFont(Font.font(ff, f)); 
		l.setMinWidth(w); 
		l.setAlignment(p); 
		l.setLayoutX(x); 
		l.setLayoutY(y);		
	}

	// Small helper for button layout
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f)); 
		b.setMinWidth(w); 
		b.setAlignment(p); 
		b.setLayoutX(x); 
		b.setLayoutY(y);		
	}
}
