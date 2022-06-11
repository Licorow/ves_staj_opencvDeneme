module OPENCV_ {
	requires javafx.controls;
	requires javafx.fxml;
	requires opencv;
	requires java.desktop;
	requires javafx.swing;

	
	opens application to javafx.graphics, javafx.fxml;
}
