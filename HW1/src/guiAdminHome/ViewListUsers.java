package guiAdminHome;

// Java Standard Imports
import java.util.List;

// JavaFX Layout and Geometry Imports
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

// JavaFX Widget Imports
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

// Application Specific Imports
import database.Database;

/**
 * <p> Title: ViewListUsers Class </p>
 * <p> Description: This class provides a graphical interface to display a paginated 
 * list of all registered users in the database. </p>
 */
public class ViewListUsers {
	
	/*-*******************************************************************************************
	Attributes
	*/
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	/** Label for the page title. */
	protected static Label label_PageTitle = new Label("All Registered Users");
	
	/** Label for the list column headers. */
	protected static Label label_Headers = new Label();
	private static Line line_Separator1 = new Line(20, 95, width-20, 95);

	private static VBox vbox_UserContainer = new VBox();
	private static Button button_PagePrev = new Button("<");
	private static Button button_PageNext = new Button(">");
	private static Label label_PageInfo = new Label();

	/** Button to navigate back to the Admin Home page. */
	protected static Button button_Back = new Button("Back to Admin Home");

	private static int currentPage = 1;
	private static int usersPerPage = 10;
	private static int totalPages = 1;

	private static ViewListUsers theView;
	private static Pane theRootPane;
	private static Scene theScene;
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/*-*******************************************************************************************
	Methods
	*/

	/**
	 * <p> Method: displayListUsers </p>
	 * <p> Description: Entry point to display the user list view. Initializes the view if 
	 * it hasn't been created and refreshes the user data. </p>
	 * @param ps The JavaFX Stage onto which the scene is set.
	 */
	public static void displayListUsers(Stage ps) {
		if (theView == null) theView = new ViewListUsers();
		
		// Refresh the data every time we enter the screen
		populateUserList();
		
		ps.setTitle("CSE 360 Foundation Code: User List");
		ps.setScene(theScene);
		ps.show();
	}

	/**
	 * Private constructor to initialize the GUI components for the user list.
	 */
	private ViewListUsers() {
		theRootPane = new Pane();
		theScene = new Scene(theRootPane, width, height);

		// Title setup
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 10);

		// Header labels setup (Using Monospaced font so columns line up)
		label_Headers.setText(String.format("%-5s %-15s | %-25s | %-5s", "#", "Username", "Email", "Roles"));
		setupLabelUI(label_Headers, "Monospaced", 14, width-40, Pos.BASELINE_LEFT, 30, 70);

		// VBox container setup
		vbox_UserContainer.setSpacing(10);
		vbox_UserContainer.setPadding(new Insets(10));
		vbox_UserContainer.setLayoutX(20);
		vbox_UserContainer.setLayoutY(100);
		vbox_UserContainer.setPrefWidth(width - 40);

		// Page navigation setup
		button_PagePrev.setLayoutX(width - 130);
		button_PagePrev.setLayoutY(10);
		button_PageNext.setLayoutX(width - 50);
		button_PageNext.setLayoutY(10);
		label_PageInfo.setLayoutX(width - 95);
		label_PageInfo.setLayoutY(10);
		label_PageInfo.setFont(Font.font("Arial", 14));

		button_PagePrev.setOnAction(e -> {
			if (currentPage > 1) {
				currentPage--;
				populateUserList();
			}
		});

		button_PageNext.setOnAction(e -> {
			if (currentPage < totalPages) {
				currentPage++;
				populateUserList();
			}
		});

		// Back Button setup
		setupButtonUI(button_Back, "Dialog", 18, 250, Pos.CENTER, (width/2)-125, height - 80);
		button_Back.setOnAction((_) -> {
			ViewAdminHome.displayAdminHome(ViewAdminHome.theStage, ViewAdminHome.theUser);
		});

		theRootPane.getChildren().addAll(label_PageTitle, label_Headers, line_Separator1, 
			vbox_UserContainer, button_PagePrev, button_PageNext, label_PageInfo, button_Back);
	}

	/**
	 * Populates the VBox container with user data based on the current page index.
	 */
	private static void populateUserList() {
	    vbox_UserContainer.getChildren().clear();
	    List<String> usernames = theDatabase.getUserList();
	    
	    // calculate total pages
	    int totalUsers = usernames.size();
	    totalPages = (int) Math.ceil((double) totalUsers / usersPerPage);
	    if (totalPages == 0) totalPages = 1;

	    // bounds for this page
	    int start = (currentPage - 1) * usersPerPage;
	    int end = Math.min(start + usersPerPage, totalUsers);

	    for (int i = start; i < end; i++) {
	        String u = usernames.get(i);
	        if (u == null || u.equals("<Select a User>")) continue;

	        String email = theDatabase.getEmailAddress(u);
	        int rolesCount = 1; // keep vibe style for now

	        // number the row on the left
	        String rowText = String.format("%-3d %-15s | %-25s | %-5d", i + 1, u, email, rolesCount);
	        Label userRow = new Label(rowText);
	        userRow.setFont(Font.font("Monospaced", 14));
	        vbox_UserContainer.getChildren().add(userRow);
	    }

	    // update page info
	    label_PageInfo.setText(currentPage + " of " + totalPages);
	}

	/**
	 * Helper method to setup Label UI properties.
	 */
	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}

	/**
	 * Helper method to setup Button UI properties.
	 */
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}
}