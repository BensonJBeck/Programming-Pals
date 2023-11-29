package com.programmingpals.project;

import javafx.scene.control.Alert;

public class Utils {
	public static final String STUDENTS_DATABASE = "students.json";
	public static final String CHALLENGES_DATABASE = "challenges.json";
	
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
