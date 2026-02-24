/**
 * The FoundationsF26 module.
 * This module manages the applications dependencies on JavaFX and SQL.
 */
module FoundationsF26 {
	requires javafx.controls;
	requires java.sql;
	
	opens applicationMain to javafx.graphics, javafx.fxml;
}