package guiRole2;

import java.time.format.DateTimeFormatter;
import java.util.List;

import database.Database;
import entityClasses.User;
import entityClasses.WhisperMessage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * View for the student inbox page.
 * Shows private whispers sent by staff about the student's posts.
 */
public class ViewStudentInbox {

	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	protected static Label label_PageTitle = new Label("Student Inbox");
	protected static Label label_InboxHeader = new Label("Staff Whispers");
	protected static ListView<WhisperMessage> list_Whispers = new ListView<>();
	protected static Label label_PostTitle = new Label("Select a whisper to view...");
	protected static Label label_StaffAuthor = new Label();
	protected static Label label_Timestamp = new Label();
	protected static TextArea area_WhisperBody = new TextArea();
	protected static Button button_Back = new Button("Back to Home");

	protected static Stage theStage;
	protected static User theUser;
	private static Pane theRootPane;
	private static Scene theScene;
	private static ViewStudentInbox theView;
	private static Database database = applicationMain.FoundationsMain.database;

	/**
	 * Displays the student inbox.
	 * @param ps the stage for this page
	 * @param user the current user
	 */
	public static void displayStudentInbox(Stage ps, User user) {
		theStage = ps;
		theUser = user;
		if (theView == null) theView = new ViewStudentInbox();

		refreshInbox();
		theStage.setTitle("CSE 360 Student Inbox");
		theStage.setScene(theScene);
		theStage.show();
	}

	/**
	 * Builds the inbox page.
	 */
	private ViewStudentInbox() {
		theRootPane = new Pane();
		theScene = new Scene(theRootPane, width, height);

		setupLabel(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 10);
		setupLabel(label_InboxHeader, "Arial", 18, 250, Pos.BASELINE_LEFT, 20, 90);
		setupButton(button_Back, "Arial", 14, 150, Pos.CENTER, width - 170, 50);
		button_Back.setOnAction((_) -> { ControllerStudentInbox.performBack(); });

		list_Whispers.setLayoutX(20);
		list_Whispers.setLayoutY(120);
		list_Whispers.setPrefSize(280, height - 180);

		setupLabel(label_PostTitle, "Arial", 20, width - 350, Pos.BASELINE_LEFT, 320, 120);
		setupLabel(label_StaffAuthor, "Arial", 14, width - 350, Pos.BASELINE_LEFT, 320, 155);
		setupLabel(label_Timestamp, "Arial", 12, width - 350, Pos.BASELINE_LEFT, 320, 180);

		area_WhisperBody.setLayoutX(320);
		area_WhisperBody.setLayoutY(210);
		area_WhisperBody.setPrefSize(width - 350, height - 260);
		area_WhisperBody.setEditable(false);
		area_WhisperBody.setWrapText(true);

		list_Whispers.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
			if (selected != null) {
				label_PostTitle.setText("Post: " + selected.getPostTitle());
				label_StaffAuthor.setText("From: " + selected.getStaffAuthor());
				label_Timestamp.setText("Sent: " + selected.getTimestamp().format(
						DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")));
				area_WhisperBody.setText(selected.getMessage());
			}
		});

		list_Whispers.setCellFactory(param -> new ListCell<>() {
			@Override
			protected void updateItem(WhisperMessage whisper, boolean empty) {
				super.updateItem(whisper, empty);
				if (empty || whisper == null) {
					setText(null);
				} else {
					setText("Post: " + whisper.getPostTitle() + "\nFrom: " + whisper.getStaffAuthor());
				}
			}
		});

		theRootPane.getChildren().addAll(label_PageTitle, label_InboxHeader, button_Back,
				list_Whispers, label_PostTitle, label_StaffAuthor, label_Timestamp, area_WhisperBody);
	}

	/**
	 * Reloads the inbox list for the current student.
	 */
	protected static void refreshInbox() {
		list_Whispers.getItems().clear();
		label_PostTitle.setText("Select a whisper to view...");
		label_StaffAuthor.setText("");
		label_Timestamp.setText("");
		area_WhisperBody.clear();

		try {
			List<WhisperMessage> whispers = database.loadWhispersForStudent(theUser.getUserName());
			list_Whispers.getItems().addAll(whispers);
			if (whispers.isEmpty()) {
				label_PostTitle.setText("No whispers yet.");
			}
		} catch (Exception e) {
			label_PostTitle.setText("Unable to load whispers.");
		}
	}

	private static void setupLabel(Label l, String ff, double f, double w, Pos p, double x, double y) {
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);
	}

	private static void setupButton(Button b, String ff, double f, double w, Pos p, double x, double y) {
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);
	}
}
