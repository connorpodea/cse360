/** The FoundationsF26 module. */
module FoundationsF26{
	requires javafx.controls;
	requires java.sql;
	
	opens applicationMain to javafx.graphics, javafx.fxml;
}
