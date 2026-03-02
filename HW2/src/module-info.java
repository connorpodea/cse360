/**
 * Defines the modules used by the Foundations application.
 * This module supports the JavaFX screens and database features used in the assignment.
 */
module FoundationsF26 {
	requires javafx.controls;
	requires java.sql;
	
	opens applicationMain to javafx.graphics, javafx.fxml;
}
