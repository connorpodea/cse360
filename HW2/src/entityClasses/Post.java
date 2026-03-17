package entityClasses;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a discussion post.
 * Stores basic post data like title, body, author, and time created.
 */
public class Post {

    /** Unique id for the post */
    private int id;

    /** Post title */
    private String title;

    /** Main content of the post */
    private String body;

    /** Author of the post */
    private String author;

    /** Time the post was created */
    private LocalDateTime timestamp;

    /** Category the post belongs to */
    private String category;

    /** Whether the post has been logically deleted */
    private boolean deleted;

    /** Max allowed length for post titles */
    public static final int MAX_TITLE_LENGTH = 200;

    /** Max allowed length for post bodies */
    public static final int MAX_BODY_LENGTH = 5000;

    /**
     * Creates a new Post with the current timestamp.
     * This function satisfies the CREATE functionality by creating the users post.
     * it gets all the users details and succesfully creates a post.
     * @param id       the id of the post
     * @param title    the post title
     * @param body     the post content
     * @param author   the user who created the post
     * @param category the category of the post
     */
    public Post(int id, String title, String body, String author, String category) {
        validateCreate(title, body, author);
        this.id = id;
        this.title = title;
        this.body = body;
        this.author = author;
        this.timestamp = LocalDateTime.now();
        this.category = category;
        this.deleted = false;
    }

    /**
     * Validates values when creating a post.
     *
     * @param title  post title
     * @param body   post body
     * @param author post author
     * @throws IllegalArgumentException if any value is invalid
     */
    private void validateCreate(String title, String body, String author) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Post title cannot be empty.");
        }
        if (body == null || body.trim().isEmpty()) {
            throw new IllegalArgumentException("Post body cannot be empty.");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Post author cannot be empty.");
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("Post title exceeds 200 characters.");
        }
        if (body.length() > MAX_BODY_LENGTH) {
            throw new IllegalArgumentException("Post body exceeds 5000 characters.");
        }
    }

    /**
     * Updates the title and body of the post.
     * the satisfies the update functionality for the CRUD.
     * this will take the users new body of text and or title and set it to the current.
     * replacing the old body and title with the new. 
     * @param newTitle updated title
     * @param newBody  updated body
     */
    public void update(String newTitle, String newBody) {
        if (deleted) {
            return;
        }
        validateUpdate(newTitle, newBody);
        this.title = newTitle;
        this.body = newBody;
    }

    /**
     * Validates values when updating a post.
     *
     * @param newTitle updated title
     * @param newBody  updated body
     * @throws IllegalArgumentException if values are invalid
     */
    private void validateUpdate(String newTitle, String newBody) {
        if (newTitle == null || newTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Updated post title cannot be empty.");
        }
        if (newBody == null || newBody.trim().isEmpty()) {
            throw new IllegalArgumentException("Updated post body cannot be empty.");
        }
        if (newTitle.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("Updated post title exceeds 200 characters.");
        }
        if (newBody.length() > MAX_BODY_LENGTH) {
            throw new IllegalArgumentException("Updated post body exceeds 5000 characters.");
        }
    }

    /**
     * Returns the timestamp in a readable format.
     *
     * @return formatted timestamp string
     */
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a");
        return this.timestamp.format(formatter);
    }
    
    public void setTitle(String input) {
    	this.title = input;
    }

    /**
     * Returns the post id.
     * Satifies the Read for CRUD
     * @return the post id
     */
    public int getId() {
        return id;
    }

    /**
     * Satifies the Read for CRUD
     * Returns the post id.
     * @return the post id
     */
    public int getPostId() {
        return id;
    }

    /**
     * Satifies the Read for CRUD
     * Returns the post title.
     * @return the post title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Satifies the Read for CRUD
     * Returns the post body.
     * @return the post body
     */
    public String getBody() {
        return body;
    }

    /**
     * Satifies the Read for CRUD
     * Returns the post author.
     * @return the post author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Satifies the Read for CRUD
     * Returns the author user name.
     * @return the author user name
     */
    public String getAuthorUserName() {
        return author;
    }

    /**\
     * Satifies the Read for CRUD
     * Returns when the post was created.
     * @return when the post was created
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Satifies the Read for CRUD
     * Returns the post category.
     * @return the post category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns true if the post is deleted.
     * Satisfies the Deleted functionality for the CRUD.
     * will just check to see if a post is deleted, this function is mostly for the test cases of our program. 
     * @return true if deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Marks the post as deleted.
     * This just returns a boolean value of true if the users post is deleted.
     * this way when the post is clicked on, the program will check to see if it is deleted and if so then 
     * replace the body of test and title with the word "DELETED".
     */
    public void markDeleted() {
        deleted = true;
    }

    /**
     * Sets the body of the post.
     * Used when loading data from the database.
     *
     * @param body post body
     */
    public void setBody(String body) {
        if (deleted) {
            return;
        }
        this.body = body;
    }

    /**
     * Constructor used when loading a post from the database.
     *
     * @param id        post id
     * @param title     post title
     * @param body      post body
     * @param author    post author
     * @param category  post category
     * @param timestamp time the post was created
     */
    public Post(int id, String title, String body, String author, String category, LocalDateTime timestamp) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.author = author;
        this.category = category;
        this.timestamp = timestamp;
        this.deleted = false;
    }
}
