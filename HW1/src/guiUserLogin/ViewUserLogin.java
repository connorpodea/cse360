package guiUserLogin;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * <p> Title: ViewUserLogin Class. </p>
 *
 * <p> Description: This class provides the JavaFX-based user interface for the 
 * system login page, allowing existing users to sign in or new users to 
 * initiate account setup via an invitation code. </p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @author Lynn Robert Carter
 * @version 1.00 2025-04-20 Initial version
 */
public class ViewUserLogin {

	/*-********************************************************************************************
	Attributes
	 *********************************************************************************************/

	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	private static Label label_ApplicationTitle = new Label("Foundation Application Startup Page");

	private static Label label_OperationalStartTitle = new Label("Log In or Invited User Account Setup ");
	private static Label label_LogInInsrtuctions = new Label("Enter your user name and password and "+	
			"then click on the LogIn button");
	
	/** Alert displayed when an invalid username or password combination is entered. */
	protected static Alert alertUsernamePasswordError = new Alert(AlertType.INFORMATION);

	/** TextField for the user to enter their username. */
	protected static TextField text_Username = new TextField();
	
	/** PasswordField for the user to enter their password. */
	protected static PasswordField text_Password = new PasswordField();
	
	private static Button button_Login = new Button("Log In");	

	private static Label label_AccountSetupInsrtuctions = new Label("No account? "+	
			"Enter your invitation code and click on the Account Setup button");
	private static TextField text_Invitation = new TextField();
	private static Button button_SetupAccount = new Button("Setup Account");

	private static Button button_Quit = new Button("Quit");

	private static Stage theStage;	
	private static Pane theRootPane;
	
	/** The primary Scene for the User Login interface. */
	public static Scene theUserLoginScene = null;	

	private static ViewUserLogin theView = null;

	/*-********************************************************************************************
	Constructor and Display Methods
	 *********************************************************************************************/

	/**
	 * <p> Method: displayUserLogin </p>
	 * * <p> Description: Static entry point to display the login page. Initializes 
	 * the view if necessary and resets all input fields. </p>
	 * @param ps The JavaFX Stage onto which the login scene will be set.
	 */
	public static void displayUserLogin(Stage ps) {
		
		// Establish the references to the GUI. There is no current user yet.
		theStage = ps;
		
		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewUserLogin();
		
		// Populate the dynamic aspects of the GUI with the data from the user and the current
		// state of the system.		
		text_Username.setText("");		// Reset the username and password from the last use
		text_Password.setText("");
		text_Invitation.setText("");	// Same for the invitation code

		// Set the title for the window, display the page, and wait for the Admin to do something
		theStage.setTitle("CSE 360 Foundation Code: User Login Page");		
		theStage.setScene(theUserLoginScene);
		theStage.show();
	}

	/**
	 * Private constructor to initialize the GUI layout, labels, and widgets.
	 * This handles both the existing user login and the invitation-based setup.
	 */
	private ViewUserLogin() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theUserLoginScene = new Scene(theRootPane, width, height);
		
		// Populate the window with the title and other common widgets and set their static state
		setupLabelUI(label_ApplicationTitle, "Arial", 32, width, Pos.CENTER, 0, 10);
		setupLabelUI(label_OperationalStartTitle, "Arial", 24, width, Pos.CENTER, 0, 60);

		// Existing user log in portion of the page
		setupLabelUI(label_LogInInsrtuctions, "Arial", 18, width, Pos.BASELINE_LEFT, 20, 120);

		setupTextUI(text_Username, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 160, true);
		text_Username.setPromptText("Enter Username");

		setupTextUI(text_Password, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 210, true);
		text_Password.setPromptText("Enter Password");

		setupButtonUI(button_Login, "Dialog", 18, 200, Pos.CENTER, 475, 180);
		button_Login.setOnAction((_) -> {ControllerUserLogin.doLogin(theStage); });

		alertUsernamePasswordError.setTitle("Invalid username/password!");
		alertUsernamePasswordError.setHeaderText(null);

		// The invitation to setup an account portion of the page
		setupLabelUI(label_AccountSetupInsrtuctions, "Arial", 18, width, Pos.BASELINE_LEFT, 20, 300);

		setupTextUI(text_Invitation, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 340, true);
		text_Invitation.setPromptText("Enter Invitation Code");

		setupButtonUI(button_SetupAccount, "Dialog", 18, 200, Pos.CENTER, 475, 340);
		button_SetupAccount.setOnAction((_) -> {
			System.out.println("**** Calling doSetupAccount");
			ControllerUserLogin.doSetupAccount(theStage, text_Invitation.getText());
		});

		setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 520);
		button_Quit.setOnAction((_) -> {ControllerUserLogin.performQuit(); });

		theRootPane.getChildren().addAll(
				label_ApplicationTitle, 
				label_OperationalStartTitle,
				label_LogInInsrtuctions, label_AccountSetupInsrtuctions, text_Username,
				button_Login, text_Password, text_Invitation, button_SetupAccount,
				button_Quit);
	}


	/*-********************************************************************************************
	Helper methods
	 *********************************************************************************************/

	/**
	 * Initializes standard fields for a label.
	 * @param l The Label object
	 * @param ff Font family
	 * @param f Font size
	 * @param w Width
	 * @param p Alignment position
	 * @param x X location
	 * @param y Y location
	 */
	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}

	/**
	 * Initializes standard fields for a button.
	 * @param b The Button object
	 * @param ff Font family
	 * @param f Font size
	 * @param w Width
	 * @param p Alignment position
	 * @param x X location
	 * @param y Y location
	 */
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}

	/**
	 * Initializes standard fields for a text field.
	 * @param t The TextField object
	 * @param ff Font family
	 * @param f Font size
	 * @param w Width
	 * @param p Alignment position
	 * @param x X location
	 * @param y Y location
	 * @param e Editable flag
	 */
	private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}		
}