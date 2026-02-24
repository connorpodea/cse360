package guiNewAccount;

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
import database.Database;
import entityClasses.User;

/**
 * <p> Title: ViewNewAccount Class. </p>
 * * <p> Description: The ViewNewAccount Page enables a potential user with an invitation
 * code to establish an account after they have specified the code on the login page. </p>
 * * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * * @author Lynn Robert Carter
 * @version 1.00 2025-08-19 Initial version
 */
public class ViewNewAccount {
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	
	private static Label label_ApplicationTitle = new Label("Foundation Application Account Setup Page");
    
	/** Label to display the title for the account creation area. */
	protected static Label label_NewUserCreation = new Label(" User Account Creation.");
    
	/** Label providing instructions for username and password entry. */
	protected static Label label_NewUserLine = new Label("Please enter a username and a password.");
    
	/** TextField for the new username input. */
	protected static TextField text_Username = new TextField();
	
	/** PasswordField for the initial password entry. */
	protected static PasswordField text_Password1 = new PasswordField();
	
	/** PasswordField to confirm the password entry. */
	protected static PasswordField text_Password2 = new PasswordField();
	
	/** Button to trigger the account creation logic. */
	protected static Button button_UserSetup = new Button("Create Account");
    
	/** TextField for the invitation code (if displayed). */
	protected static TextField text_Invitation = new TextField();
	
	/** TextField for the user's email address. */
	protected static TextField text_Email = new TextField();
	
	/** TextField for the user's full name. */
	protected static TextField text_Name = new TextField();
	
    /** Alert displayed if the invitation code provided is invalid. */
	protected static Alert alertInvitationCodeIsInvalid = new Alert(AlertType.INFORMATION);

	/** Alert displayed if the two entered passwords do not match. */
	protected static Alert alertUsernamePasswordError = new Alert(AlertType.INFORMATION);
	
	/** Alert displayed if the entered username does not meet requirements. */
	protected static Alert alertUsernameIsInvalid = new Alert(AlertType.INFORMATION);

    /** Button to quit the application from this view. */
	protected static Button button_Quit = new Button("Quit");


	private static ViewNewAccount theView;
	private static Database theDatabase = applicationMain.FoundationsMain.database;		

	/** The Stage established by JavaFX for this view. */
	protected static Stage theStage;			
	
	private static Pane theRootPane;			
	
	/** The User currently logged into the application. */
	protected static User theUser;				
   
    /** The invitation code used to link to account details. */
	protected static String theInvitationCode;	
    
	/** The email address associated with the account. */
	protected static String emailAddress;		
    
	/** The role assigned to the user via the invitation code. */
	protected static String theRole;			
	
	/** The Scene for the New Account GUI, allowing access to its widgets. */
	public static Scene theNewAccountScene = null;	
	

	/**
	 * <p> Method: displayNewAccount </p>
	 * * <p> Description: Main entry point for displaying the New Account view. </p>
	 * @param ps The JavaFX Stage to be used for this GUI.
	 * @param ic The invitation code used to initialize the account setup.
	 */
	public static void displayNewAccount(Stage ps, String ic) {
		theStage = ps;
		theInvitationCode = ic;
		
		if (theView == null) theView = new ViewNewAccount();
		
		text_Username.setText("");
		text_Password1.setText("");
		text_Password2.setText("");
		text_Email.setText("");
		text_Name.setText("");
		
		theRole = theDatabase.getRoleGivenAnInvitationCode(theInvitationCode);
		
		if (theRole.length() == 0) {
			alertInvitationCodeIsInvalid.showAndWait();
			return;
		}
		
		emailAddress = theDatabase.getEmailAddressUsingCode(theInvitationCode);
		text_Email.setText(emailAddress);
		
    	theRootPane.getChildren().clear();
    	theRootPane.getChildren().addAll(label_NewUserCreation, label_NewUserLine, text_Username,
    			text_Password1, text_Password2, text_Email, text_Name, button_UserSetup, button_Quit);    	

		theStage.setTitle("CSE 360 Foundation Code: New User Account Setup");	
        theStage.setScene(theNewAccountScene);
		theStage.show();
	}
	
	/**
	 * Private constructor to initialize the GUI widgets and setup the layout.
	 */
	private ViewNewAccount() {
		theRootPane = new Pane();
		theNewAccountScene = new Scene(theRootPane, width, height);

		setupLabelUI(label_ApplicationTitle, "Arial", 28, width, Pos.CENTER, 0, 5);
    	setupLabelUI(label_NewUserCreation, "Arial", 32, width, Pos.CENTER, 0, 10);
    	setupLabelUI(label_NewUserLine, "Arial", 24, width, Pos.CENTER, 0, 70);
		
		setupTextUI(text_Username, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 160, true);
		text_Username.setPromptText("Enter the Username");
		
		setupTextUI(text_Password1, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 210, true);
		text_Password1.setPromptText("Enter the Password");
		
		setupTextUI(text_Password2, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 260, true);
		text_Password2.setPromptText("Enter the Password Again");
		
		setupTextUI(text_Email, "Arial", 16, 350, Pos.BASELINE_LEFT, 200, 310, true);
		text_Email.setPromptText("Email Address");

		setupTextUI(text_Name, "Arial", 16, 350, Pos.BASELINE_LEFT, 200, 360, true);
		text_Name.setPromptText("Full Name (First Middle Last)");

		alertInvitationCodeIsInvalid.setTitle("Invalid Invitation Code");
		alertInvitationCodeIsInvalid.setHeaderText("The invitation code is not valid.");
		alertInvitationCodeIsInvalid.setContentText("Correct the code and try again.");

		alertUsernamePasswordError.setTitle("Passwords Do Not Match");
		alertUsernamePasswordError.setHeaderText("The two passwords must be identical.");
		alertUsernamePasswordError.setContentText("Correct the passwords and try again.");
		
		alertUsernameIsInvalid.setTitle("UserName is Invalid");
		alertUsernameIsInvalid.setHeaderText("Must be <= 4 or >= 16...");
		alertUsernameIsInvalid.setContentText("Correct the username");

        setupButtonUI(button_UserSetup, "Dialog", 18, 200, Pos.CENTER, 475, 210);
        button_UserSetup.setOnAction((_) -> {
        	ControllerNewAccount.setUsername();
        	ControllerNewAccount.setPassword1();
			ControllerNewAccount.setPassword2();
			ControllerNewAccount.setEmail();
			ControllerNewAccount.setName();
			ControllerNewAccount.doCreateUser();
        });
		
        setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((_) -> {ControllerNewAccount.performQuit(); });
	}
	
	/**
	 * Setup Label UI helper.
	 * @param l Label
	 * @param ff Font
	 * @param f Size
	 * @param w Width
	 * @param p Pos
	 * @param x X
	 * @param y Y
	 */
	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	
	/**
	 * Setup Button UI helper.
	 * @param b Button
	 * @param ff Font
	 * @param f Size
	 * @param w Width
	 * @param p Pos
	 * @param x X
	 * @param y Y
	 */
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}

	/**
	 * Setup Text UI helper.
	 * @param t TextField
	 * @param ff Font
	 * @param f Size
	 * @param w Width
	 * @param p Pos
	 * @param x X
	 * @param y Y
	 * @param e Editable
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