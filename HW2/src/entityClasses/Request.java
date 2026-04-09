package entityClasses;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a staff request submitted to an admin.
 * Separate from Post — requests have a status (OPEN, CLOSED, REOPENED)
 * and can include resolution notes from the admin.
 * Can also link back to an original request when reopened.
 */
public class Request {

    /** Unique ID for this request */
    private int id;

    /** Short title describing what is being requested */
    private String title;

    /** Full description of the request */
    private String description;

    /** Username of the staff member who submitted the request */
    private String author;

    /** Current status: OPEN, CLOSED, or REOPENED */
    private String status;

    /** Notes written by the admin when closing the request */
    private String resolutionNotes;

    /** ID of the original request if this is a reopened request, -1 otherwise */
    private int originalRequestId;

    /** Time the request was submitted */
    private LocalDateTime timestamp;

    /**
     * Creates a new request with OPEN status.
     * Used when staff submits a brand new request.
     *
     * @param id          unique request id
     * @param title       short title
     * @param description full description
     * @param author      staff username
     */
    public Request(int id, String title, String description, String author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.status = "OPEN";
        this.resolutionNotes = "";
        this.originalRequestId = -1;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Full constructor used when loading a request from the database.
     *
     * @param id                unique request id
     * @param title             short title
     * @param description       full description
     * @param author            staff username
     * @param status            current status
     * @param resolutionNotes   admin notes on closure
     * @param originalRequestId id of original if reopened, -1 otherwise
     * @param timestamp         when the request was created
     */
    public Request(int id, String title, String description, String author,
                   String status, String resolutionNotes, int originalRequestId,
                   LocalDateTime timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.status = status;
        this.resolutionNotes = resolutionNotes == null ? "" : resolutionNotes;
        this.originalRequestId = originalRequestId;
        this.timestamp = timestamp;
    }

    /** @return the request id */
    public int getId() { return id; }

    /** @return the request title */
    public String getTitle() { return title; }

    /** @return the request description */
    public String getDescription() { return description; }

    /** @return the author username */
    public String getAuthor() { return author; }

    /** @return the current status */
    public String getStatus() { return status; }

    /** @return the admin resolution notes */
    public String getResolutionNotes() { return resolutionNotes; }

    /** @return the original request id, or -1 if not a reopen */
    public int getOriginalRequestId() { return originalRequestId; }

    /** @return the timestamp */
    public LocalDateTime getTimestamp() { return timestamp; }

    /**
     * Sets the status of this request.
     * @param status OPEN, CLOSED, or REOPENED
     */
    public void setStatus(String status) { this.status = status; }

    /**
     * Sets the resolution notes written by the admin.
     * @param notes the resolution notes
     */
    public void setResolutionNotes(String notes) { this.resolutionNotes = notes; }

    /**
     * Returns a human-readable timestamp.
     * @return formatted timestamp string
     */
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
        return timestamp.format(formatter);
    }
}