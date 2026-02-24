package guiAdminHome;
import database.Database;
import java.util.List;
import entityClasses.User;

/*******
 * <p> Title: ModelAdminHome Class. </p>
 * 
 * <p> Description: The AdminHome Page Model.  This class is not used as there is no
 * data manipulated by this MVC beyond accepting role information and saving it in the
 * database.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-15 Initial version
 *  
 */

public class ModelAdminHome {
	protected static List<User> getAllUsers(Database db) {
		   return db.getAllUsersForDisplay();
	}
}

