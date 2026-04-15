package MVCRequestManagement;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import entityClasses.User;

/**
 * Main view for the staff request system.
 * Shows a list of all requests on the left and full details on the right.
 * Admins see a Close button; staff see a Reopen button on closed requests.
 */
public class ViewRequestManagement {

    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    // --- Top area ---
    protected static Label label_PageTitle = new Label("Staff Requests");
    protected static Button button_CreateRequest = new Button("Create Request");
    protected static Button button_Back = new Button("Back to Home");

    // --- Left sidebar ---
    protected static Label label_SidebarHeader = new Label("All Requests");
    protected static TextField text_Search = new TextField();
    protected static ComboBox<String> combo_FilterStatus = new ComboBox<>();
    protected static ListView<String> list_Requests = new ListView<>();

    // --- Right detail panel ---
    protected static Label label_FullTitle = new Label("Select a request to view...");
    protected static Label label_FullAuthor = new Label();
    protected static Label label_FullTimestamp = new Label();
    protected static Label label_Status = new Label();
    protected static TextArea area_FullDescription = new TextArea();
    protected static Label label_ResolutionNotes = new Label();
    protected static Label label_OriginalLink = new Label();

    // --- Action buttons (shown based on role + status) ---
    protected static Button button_Close = new Button("Close Request");
    protected static Button button_Reopen = new Button("Reopen Request");

    // Tracks which request is currently selected
    protected static int currentRequestId = -1;

    public static Stage theStage;
    public static User theUser;
    private static Pane theRootPane;
    private static Scene theScene;
    private static ViewRequestManagement theView;

    /**
     * Shows the request management screen.
     * Reloads from the database each time so the list stays current.
     *
     * @param ps   the JavaFX stage
     * @param user the currently logged in user
     */
    public static void displayRequestManagement(Stage ps, User user) {
        theStage = ps;
        theUser = user;
        if (theView == null) theView = new ViewRequestManagement();

        // Always reload from DB so we don't show stale data
        ControllerRequestManagement.loadRequestsFromDatabase();
        refreshRequestList();

        theStage.setTitle("CSE 360 Staff Requests");
        theStage.setScene(theScene);
        theStage.show();
    }

    /**
     * Builds the full UI layout.
     * Only called once (singleton pattern).
     */
    private ViewRequestManagement() {
        theRootPane = new Pane();
        theScene = new Scene(theRootPane, width, height);

        // --- TOP ---
        setupLabel(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 10);

        setupButton(button_CreateRequest, "Arial", 14, 160, Pos.CENTER, 20, 50);
        button_CreateRequest.setOnAction((_) -> {
            ViewCreateRequest.displayCreateRequest(theStage, theUser);
        });

        setupButton(button_Back, "Arial", 14, 140, Pos.CENTER, width - 160, 50);
        button_Back.setOnAction((_) -> { ControllerRequestManagement.performBack(); });

        // --- LEFT SIDEBAR ---
        setupLabel(label_SidebarHeader, "Arial", 16, 260, Pos.BASELINE_LEFT, 20, 95);

        text_Search.setPromptText("Search requests...");
        text_Search.setLayoutX(20); text_Search.setLayoutY(125);
        text_Search.setPrefWidth(260);
        text_Search.setOnKeyReleased((_) -> refreshRequestList());

        combo_FilterStatus.getItems().addAll("All", "OPEN", "CLOSED", "REOPENED");
        combo_FilterStatus.setValue("All");
        combo_FilterStatus.setLayoutX(20); combo_FilterStatus.setLayoutY(160);
        combo_FilterStatus.setPrefWidth(260);
        combo_FilterStatus.setOnAction((_) -> refreshRequestList());

        list_Requests.setLayoutX(20);
        list_Requests.setLayoutY(200);
        list_Requests.setPrefSize(260, height - 270);

        // When a request is selected, update the right panel
        list_Requests.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, selected) -> {
                if (selected != null) {
                    // Parse the ID from the display string (format: "#ID | title")
                    try {
                        int id = Integer.parseInt(selected.split("\\|")[0].trim().replace("#", ""));
                        currentRequestId = id;
                        ControllerRequestManagement.displaySelectedRequest();
                    } catch (NumberFormatException e) {
                        // couldn't parse, ignore
                    }
                }
            });

        // --- RIGHT DETAIL PANEL ---
        setupLabel(label_FullTitle, "Arial", 20, width - 320, Pos.BASELINE_LEFT, 300, 100);
        setupLabel(label_FullAuthor, "Arial", 13, 400, Pos.BASELINE_LEFT, 300, 135);
        setupLabel(label_FullTimestamp, "Arial", 12, 400, Pos.BASELINE_LEFT, 300, 155);
        setupLabel(label_Status, "Arial", 13, 400, Pos.BASELINE_LEFT, 300, 175);

        area_FullDescription.setLayoutX(300); area_FullDescription.setLayoutY(200);
        area_FullDescription.setPrefSize(width - 330, height - 430);
        area_FullDescription.setEditable(false);
        area_FullDescription.setWrapText(true);

        setupLabel(label_ResolutionNotes, "Arial", 12, width - 330, Pos.BASELINE_LEFT, 300, height - 220);
        label_ResolutionNotes.setWrapText(true);

        setupLabel(label_OriginalLink, "Arial", 12, width - 330, Pos.BASELINE_LEFT, 300, height - 190);
        label_OriginalLink.setStyle("-fx-text-fill: blue; -fx-underline: true;");

        setupButton(button_Close, "Arial", 14, 180, Pos.CENTER, 300, height - 160);
        button_Close.setVisible(false);
        button_Close.setOnAction((_) -> { ControllerRequestManagement.performClose(); });

        setupButton(button_Reopen, "Arial", 14, 180, Pos.CENTER, 500, height - 160);
        button_Reopen.setVisible(false);
        button_Reopen.setOnAction((_) -> { ControllerRequestManagement.performReopen(); });

        theRootPane.getChildren().addAll(
            label_PageTitle, button_CreateRequest, button_Back,
            label_SidebarHeader, text_Search, combo_FilterStatus, list_Requests,
            label_FullTitle, label_FullAuthor, label_FullTimestamp, label_Status,
            area_FullDescription, label_ResolutionNotes, label_OriginalLink,
            button_Close, button_Reopen
        );
    }

    /**
     * Refreshes the sidebar list based on current search text and status filter.
     * Called on page load and whenever the search/filter changes.
     */
    public static void refreshRequestList() {
        list_Requests.getItems().clear();
        String search = text_Search.getText().toLowerCase();
        String filter = combo_FilterStatus.getValue();

        for (entityClasses.Request r : ModelRequestManagement.getAllRequests()) {
            boolean matchesSearch = r.getTitle().toLowerCase().contains(search)
                    || r.getDescription().toLowerCase().contains(search);
            boolean matchesFilter = filter.equals("All") || r.getStatus().equals(filter);

            if (matchesSearch && matchesFilter) {
                // Format: "#12 | OPEN | Request Title"
                String display = String.format("#%d | %s | %s", r.getId(), r.getStatus(), r.getTitle());
                list_Requests.getItems().add(display);
            }
        }
    }

    // Helper to set up labels
    private static void setupLabel(Label l, String ff, double f, double w, Pos p, double x, double y) {
        l.setFont(Font.font(ff, f)); l.setMinWidth(w); l.setAlignment(p);
        l.setLayoutX(x); l.setLayoutY(y);
    }

    // Helper to set up buttons
    private static void setupButton(Button b, String ff, double f, double w, Pos p, double x, double y) {
        b.setFont(Font.font(ff, f)); b.setMinWidth(w); b.setAlignment(p);
        b.setLayoutX(x); b.setLayoutY(y);
    }
}
