package MVCPostManagement;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import entityClasses.User;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * View class for managing and viewing discussion posts.
 * Handles the main discussion board UI.
 */
public class ViewPostManagement {
	// Screen Dimensions
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	// GUI Widgets - Navigation and Global Actions
	protected static Label label_PageTitle = new Label("Discussion Board");
	protected static Button button_ToCreatePage = new Button("Create New Post");
	protected static Button button_Back = new Button("Back to Home");
	protected static Button button_ViewReplies = new Button("View Replies");
	
	// Search and Filter Widgets
	protected static TextField text_SearchPosts = new TextField();
	protected static ComboBox<String> combo_FilterCategory = new ComboBox<>();
	
	// Left Sidebar Widgets
	protected static ListView<String> list_Posts = new ListView<>();
	protected static Label label_SidebarHeader = new Label("Recent Posts");
	
	// Right Side "Full Reading View"
	protected static Label label_FullTitle = new Label("Select a post to read...");
	protected static Label label_FullAuthor = new Label();
	protected static Label label_FullTimestamp = new Label();
	protected static TextArea area_FullBody = new TextArea();
	protected static Label label_ParticipationHeader = new Label("Participation Summary");
	protected static Label label_SummaryStudent = new Label("Select a post to review...");
	protected static Label label_SummaryMet = new Label("Requirement: ");
	protected static Label label_SummaryReplyCount = new Label("Replies to Others: ");
	protected static Label label_SummaryDistinct = new Label("Distinct Students Answered: ");
	protected static Label label_SummaryPosts = new Label("Posts: ");
	protected static Label label_SummaryReplies = new Label("Replies: ");
	protected static Button button_ReplyToThis = new Button("Reply to this Post");
	protected static Button button_Edit = new Button("Edit Post");
    protected static Button button_Delete = new Button("Delete Post");
    protected static Button button_Whisper = new Button("Whisper");
	
	// Keeps track of which post is currently selected
	protected static int currentPostId = -1;
	private static List<Integer> visiblePostIds = new ArrayList<>();

	/** The stage for this page. */
	public static Stage theStage;
	/** The current user. */
	public static User theUser;
	private static Pane theRootPane;
	private static Scene theScene;
	private static ViewPostManagement theView;

	/**
	 * Shows the post management screen.
	 * Refreshes the list so new posts show up when coming back.
	 * @param ps the stage for this page
	 * @param user the current user
	 */
	public static void displayPostManagement(Stage ps, User user) {
		theStage = ps;
		theUser = user;
		if (theView == null) theView = new ViewPostManagement();
		configureRoleSpecificLayout();
		
		// Update the sidebar so it stays in sync with storage
		refreshPostList(); 
		if (currentPostId != -1) {
			ControllerPostManagement.displaySelectedPost();
		} else {
			clearParticipationSummary();
		}
		
		theStage.setTitle("CSE 360 Discussion Board");
		theStage.setScene(theScene);
		theStage.show();
	}

	/**
	 * Builds the main discussion board UI.
	 */
	private ViewPostManagement() {
		theRootPane = new Pane();
		theScene = new Scene(theRootPane, width, height);

		// top global area
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 10);
		
		setupButtonUI(button_ToCreatePage, "Arial", 16, 200, Pos.CENTER, 20, 50);
		button_ToCreatePage.setOnAction((_) -> { 
			ViewCreatePost.displayCreatePost(theStage, theUser); 
		});

		setupButtonUI(button_Back, "Arial", 14, 150, Pos.CENTER, width - 170, 50);
		button_Back.setOnAction((_) -> { ControllerPostManagement.performBack(); });

		// left sidebar
		setupLabelUI(label_SidebarHeader, "Arial", 18, 250, Pos.BASELINE_LEFT, 20, 100);
		
		// Search bar for filtering posts
		text_SearchPosts.setPromptText("Search by title or body...");
		text_SearchPosts.setLayoutX(20); text_SearchPosts.setLayoutY(130);
		text_SearchPosts.setPrefWidth(280);
		text_SearchPosts.setOnKeyReleased((_) -> refreshPostList());

		// Category filter dropdown
		combo_FilterCategory.getItems().addAll("All", "Homework", "General Question", "Project", "Announcement", "Other");
		combo_FilterCategory.setValue("All");
		combo_FilterCategory.setLayoutX(20); combo_FilterCategory.setLayoutY(165);
		combo_FilterCategory.setPrefWidth(280);
		combo_FilterCategory.setOnAction((_) -> refreshPostList());

		list_Posts.setLayoutX(20); 
		list_Posts.setLayoutY(200);
		list_Posts.setPrefSize(280, height - 270);
		
		// When a post is selected, update the main reading area
		list_Posts.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
		    if (selected != null) {
		    	int selectedIndex = list_Posts.getSelectionModel().getSelectedIndex();
		        if (selectedIndex >= 0 && selectedIndex < visiblePostIds.size()) {
		        	currentPostId = visiblePostIds.get(selectedIndex);
		        	ControllerPostManagement.displaySelectedPost();
		        }
		    } else {
		    	currentPostId = -1;
		    	clearParticipationSummary();
		    }
		});

		// right side (reading pane)
		setupLabelUI(label_FullTitle, "Arial", 22, width - 350, Pos.BASELINE_LEFT, 320, 130);
		setupLabelUI(label_FullAuthor, "Arial", 14, 400, Pos.BASELINE_LEFT, 320, 165);
		setupLabelUI(label_FullTimestamp, "Arial", 12, 400, Pos.BASELINE_LEFT, 320, 185);
		setupLabelUI(label_ParticipationHeader, "Arial", 18, 180, Pos.BASELINE_LEFT, 590, 130);
		setupLabelUI(label_SummaryStudent, "Arial", 13, 180, Pos.BASELINE_LEFT, 590, 165);
		setupLabelUI(label_SummaryMet, "Arial", 13, 180, Pos.BASELINE_LEFT, 590, 190);
		setupLabelUI(label_SummaryReplyCount, "Arial", 13, 180, Pos.BASELINE_LEFT, 590, 215);
		setupLabelUI(label_SummaryDistinct, "Arial", 13, 180, Pos.BASELINE_LEFT, 590, 240);
		setupLabelUI(label_SummaryPosts, "Arial", 13, 180, Pos.BASELINE_LEFT, 590, 265);
		setupLabelUI(label_SummaryReplies, "Arial", 13, 180, Pos.BASELINE_LEFT, 590, 290);
		
		area_FullBody.setLayoutX(320); 
		area_FullBody.setLayoutY(210);
		area_FullBody.setPrefSize(width - 350, height - 420);
		area_FullBody.setEditable(false); 
		area_FullBody.setWrapText(true);

		setupButtonUI(button_ReplyToThis, "Arial", 16, 200, Pos.CENTER, 320, height - 150);
		button_ReplyToThis.setOnAction((_) -> { ControllerPostManagement.performReply(); });
		
		setupButtonUI(button_ViewReplies, "Arial", 16, 200, Pos.CENTER, 550, height - 150);
		button_ViewReplies.setOnAction((_) -> { 
		    MVCReplyManagement.ViewReplyList.displayReplyList(theStage, theUser, currentPostId); 
		});

		setupButtonUI(button_Edit, "Arial", 14, 100, Pos.CENTER, 320, height - 100);
	    setupButtonUI(button_Delete, "Arial", 14, 100, Pos.CENTER, 440, height - 100);
	    setupButtonUI(button_Whisper, "Arial", 14, 100, Pos.CENTER, 560, height - 100);

	    // Only show edit/delete when the user is allowed to modify the post
	    button_Edit.setVisible(false);
	    button_Edit.setOnAction((_) -> { ControllerPostManagement.performEdit(); });
	    button_Delete.setVisible(false);
	    button_Delete.setOnAction((_) -> { ControllerPostManagement.performDelete(); });
	    
	    
	    button_Whisper.setVisible(false); // Hidden by default, Controller will show it to Staff
	    button_Whisper.setOnAction((_) -> { ControllerPostManagement.performWhisper(); });
	    clearParticipationSummary();
	    
	    theRootPane.getChildren().addAll(
	    	    label_PageTitle, button_ToCreatePage, button_Back, 
	    	    label_SidebarHeader, text_SearchPosts, combo_FilterCategory, list_Posts, 
	    	    label_FullTitle, label_FullAuthor, label_FullTimestamp, label_ParticipationHeader,
	    	    label_SummaryStudent, label_SummaryMet, label_SummaryReplyCount, label_SummaryDistinct,
	    	    label_SummaryPosts, label_SummaryReplies, area_FullBody, 
	    	    button_ReplyToThis, button_ViewReplies, button_Edit, button_Delete, 
	    	    button_Whisper
	    	);
	}

	/**
	 * Refreshes the sidebar list based on search and filter values.
	 */
	public static void refreshPostList() {
	    list_Posts.getItems().clear();
	    visiblePostIds.clear();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
	    
	    String searchKey = text_SearchPosts.getText().toLowerCase(); 
	    String filterCat = combo_FilterCategory.getValue(); 
	    
	    for (entityClasses.Post p : ModelPostManagement.getPostStorage().getAllPosts()) {
	    	boolean matchesSearch = p.getTitle().toLowerCase().contains(searchKey) || 
	    							p.getBody().toLowerCase().contains(searchKey);
	    	boolean matchesCat = filterCat.equals("All") || p.getCategory().equals(filterCat);
	    	
	    	if (matchesSearch && matchesCat) {
	    		String titleText = p.isDeleted() ? "[Deleted] " + p.getTitle() : p.getTitle();
		        String displayString = String.format("[%s] %s\nBy: %s | %s", 
		        					   p.getCategory(),
		                               titleText, 
		                               p.getAuthor(), 
		                               p.getTimestamp().format(formatter)); 
		        list_Posts.getItems().add(displayString);
		        visiblePostIds.add(p.getId());
	    	}
	    }
	}

	// Small helper to avoid repeating label setup code everywhere
	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
		l.setFont(Font.font(ff, f)); l.setMinWidth(w); l.setAlignment(p); l.setLayoutX(x); l.setLayoutY(y);		
	}
	
	// Small helper to avoid repeating button setup code everywhere
	private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f)); b.setMinWidth(w); b.setAlignment(p); b.setLayoutX(x); b.setLayoutY(y);		
	}

	/**
	 * Returns true when the shared discussion board is being used by staff or admin.
	 * @return true if staff-only review widgets should be shown
	 */
	protected static boolean isStaffDiscussionView() {
		return theUser != null && theUser.getNewRole1()
				&& applicationMain.FoundationsMain.activeHomePage == 2;
	}

	/**
	 * Clears the participation summary until a post is selected.
	 */
	protected static void clearParticipationSummary() {
		label_SummaryStudent.setText("Select a post to review...");
		label_SummaryMet.setText("Requirement: ");
		label_SummaryReplyCount.setText("Replies to Others: ");
		label_SummaryDistinct.setText("Distinct Students Answered: ");
		label_SummaryPosts.setText("Posts: ");
		label_SummaryReplies.setText("Replies: ");
	}

	/**
	 * Shows staff-only widgets without changing the student discussion layout.
	 */
	private static void configureRoleSpecificLayout() {
		boolean showStaffSummary = isStaffDiscussionView();

		label_ParticipationHeader.setVisible(showStaffSummary);
		label_SummaryStudent.setVisible(showStaffSummary);
		label_SummaryMet.setVisible(showStaffSummary);
		label_SummaryReplyCount.setVisible(showStaffSummary);
		label_SummaryDistinct.setVisible(showStaffSummary);
		label_SummaryPosts.setVisible(showStaffSummary);
		label_SummaryReplies.setVisible(showStaffSummary);
		button_Whisper.setVisible(false);

		label_FullTitle.setMinWidth(showStaffSummary ? 250 : width - 350);
		area_FullBody.setPrefWidth(showStaffSummary ? 250 : width - 350);
		if (!showStaffSummary) {
			clearParticipationSummary();
		}
	}
}
