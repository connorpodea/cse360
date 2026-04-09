package MVCRequestManagement;

import entityClasses.Request;
import java.util.ArrayList;
import java.util.List;

/**
 * Model for the staff request system.
 * Holds all requests in memory and provides access methods.
 * Requests are separate from student posts — they live in requestDB, not postDB.
 */
public class ModelRequestManagement {

    // In-memory list of all requests loaded from the database
    private static List<Request> requestList = new ArrayList<>();

    /** Creates the model object. */
    public ModelRequestManagement() {}

    /**
     * Creates a new Request object and adds it to the in-memory list.
     * The caller (Controller) is responsible for saving it to the database.
     *
     * @param id          the unique request id
     * @param title       the request title
     * @param description the request description
     * @param author      the staff member submitting the request
     * @return the newly created Request
     */
    public static Request createRequest(int id, String title, String description, String author) {
        Request newRequest = new Request(id, title, description, author);
        requestList.add(newRequest);
        return newRequest;
    }

    /**
     * Returns the next available request ID.
     * Scans the list and returns max + 1 so IDs never collide.
     *
     * @return the next request id
     */
    public static int getNextRequestId() {
        int maxId = 0;
        for (Request r : requestList) {
            if (r.getId() > maxId) maxId = r.getId();
        }
        return maxId + 1;
    }

    /**
     * Returns all requests currently in memory.
     *
     * @return list of all requests
     */
    public static List<Request> getAllRequests() {
        return requestList;
    }

    /**
     * Finds a request by its ID.
     *
     * @param id the request id to search for
     * @return the matching Request, or null if not found
     */
    public static Request getRequestById(int id) {
        for (Request r : requestList) {
            if (r.getId() == id) return r;
        }
        return null;
    }

    /**
     * Replaces the in-memory list with a fresh list loaded from the database.
     * Called on page load to keep the view in sync with stored data.
     *
     * @param loaded the list of requests loaded from the database
     */
    public static void setRequestList(List<Request> loaded) {
        requestList = loaded;
    }

    /**
     * Updates the status of a request in the in-memory list.
     *
     * @param id     the request id
     * @param status the new status (OPEN, CLOSED, REOPENED)
     */
    public static void updateRequestStatus(int id, String status) {
        Request r = getRequestById(id);
        if (r != null) r.setStatus(status);
    }
}