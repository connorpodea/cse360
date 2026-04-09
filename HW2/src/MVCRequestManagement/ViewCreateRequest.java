package MVCRequestManagement;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import entityClasses.User;

/**
 * View for creating a new staff request.
 * Staff fills in a title and description and hits Submit.
 * Separate from ViewCreatePost — this saves to requestDB, not postDB.
 */
public class ViewCreateRequest {

    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    // Accessed by the controller to read user input
    protected static TextField text_Title = new TextField();
    protected static TextArea text_Description = new TextArea();
    protected static Button button_Submit = new Button("Submit Request");
    protected static Button button_Cancel = new Button("Cancel");

    protected static Stage theStage;
    protected static User theUser;
    private static Pane theRootPane;
    private static Scene theScene;
    private static ViewCreateRequest theView;

    /**
     * Shows the create request screen.
     *
     * @param ps   the JavaFX stage
     * @param user the currently logged in user
     */
    public static void displayCreateRequest(Stage ps, User user) {
        theStage = ps;
        theUser = user;
        if (theView == null) theView = new ViewCreateRequest();

        theStage.setTitle("Submit New Request");
        theStage.setScene(theScene);
        theStage.show();
    }

    /**
     * Builds the create request form UI.
     * Only called once (singleton pattern).
     */
    private ViewCreateRequest() {
        theRootPane = new Pane();
        theScene = new Scene(theRootPane, width, height);

        Label label_PageTitle = new Label("Submit a New Request");
        label_PageTitle.setFont(Font.font("Arial", 24));
        label_PageTitle.setLayoutX(50);
        label_PageTitle.setLayoutY(30);

        Label label_TitleHint = new Label("Request Title:");
        label_TitleHint.setFont(Font.font("Arial", 14));
        label_TitleHint.setLayoutX(50);
        label_TitleHint.setLayoutY(90);

        text_Title.setPromptText("Brief title for your request...");
        text_Title.setLayoutX(50);
        text_Title.setLayoutY(115);
        text_Title.setPrefWidth(width - 100);

        Label label_DescHint = new Label("Description:");
        label_DescHint.setFont(Font.font("Arial", 14));
        label_DescHint.setLayoutX(50);
        label_DescHint.setLayoutY(160);

        text_Description.setPromptText("Describe what you need the admin to do...");
        text_Description.setLayoutX(50);
        text_Description.setLayoutY(185);
        text_Description.setPrefSize(width - 100, height - 340);
        text_Description.setWrapText(true);

        button_Submit.setFont(Font.font("Arial", 16));
        button_Submit.setLayoutX(50);
        button_Submit.setLayoutY(height - 120);
        button_Submit.setPrefWidth(180);
        button_Submit.setOnAction((_) -> {
            ControllerRequestManagement.performCreateRequest();
        });

        button_Cancel.setFont(Font.font("Arial", 16));
        button_Cancel.setLayoutX(250);
        button_Cancel.setLayoutY(height - 120);
        button_Cancel.setPrefWidth(150);
        button_Cancel.setOnAction((_) -> {
            ViewRequestManagement.displayRequestManagement(theStage, theUser);
        });

        theRootPane.getChildren().addAll(
            label_PageTitle, label_TitleHint, text_Title,
            label_DescHint, text_Description,
            button_Submit, button_Cancel
        );
    }
}