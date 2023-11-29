package com.programmingpals.project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.io.FileReader;

import java.lang.reflect.Type;

public class TeacherView extends Application {
	
	private Map<String, StudentData> studentsData = new HashMap<>();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
    @Override
    public void start(Stage stage) {
    	
        // Text area for viewing student progress
        TextArea progressArea = new TextArea();
        progressArea.setEditable(false);
        progressArea.setText("Select a student to view their progress."); // Default text

        // Text field for adding a new challenge
        TextField newChallengeField = new TextField();
        newChallengeField.setPromptText("Challenge name...");
        
        // Dropdown for selecting a student
    	ComboBox<String> studentSelector = new ComboBox<>();
    	loadStudents(studentSelector);
    	studentSelector.setPrefWidth(200);
    	studentSelector.valueProperty().addListener((observable, oldValue, newValue) -> {
    	    if (newValue != null) {
    	        Type type = new TypeToken<Map<String, StudentData>>() {}.getType();
    	        Map<String, StudentData> data;

    	        // Load data from file
    	        try (FileReader reader = new FileReader(Utils.STUDENTS_DATABASE)) {
    	            data = gson.fromJson(reader, type);
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	            Utils.showAlert("Error loading students data.");
    	            return;
    	        }
    	        
    	        if (data == null) {
    	        	return;
    	        }
    	        
    	        // Add students & data to main map
    	        for (Map.Entry<String,StudentData> entry : data.entrySet()) {
    	        	studentsData.put(entry.getKey(), entry.getValue());
    	        }
    	        
    	        StudentData studentChallenges = studentsData.get(newValue);
    	        
    	        if(studentChallenges != null) {
    	        	progressArea.setText(studentChallenges.toString());
    	        }
    	        
    	        else {
    	        	Utils.showAlert("Student has not completed any challenges!");
    	        	return;
    	        }
    	    	// TODO:
    	        // newValue holds the text of the newly selected item
    	    	// use this to parse student data from studentsData and display information
    	    	// studentsData should be up to date with all student information by this point
    	    	// Fetch the json data under the key newValue and display it to progress 
    	    }
    	});

        // Button for submitting a new challenge
        Button addChallengeButton = new Button("Add Challenge");
        addChallengeButton.setOnAction(e -> {
            String newChallenge = newChallengeField.getText().trim();
            if (!newChallenge.isEmpty()) {
                addNewChallenge(newChallenge);
            }
            newChallengeField.clear();
        });
        
        // Text field for adding a new student
        TextField newStudentField = new TextField();
        newStudentField.setPromptText("Enter new student name...");

        // Button for adding a new student
        Button addStudentButton = new Button("Add Student");
        addStudentButton.setOnAction(e -> {
        	// Get student name
            String newStudent = newStudentField.getText();
            if (!studentsData.containsKey(newStudent)) {
            	// Add to dropdown
                newStudentField.clear();
                studentSelector.getItems().add(newStudent);
                
                // Add to database
                createStudentData(newStudent);
            } else {
            	Utils.showAlert("Student already exists!");
            }
        });

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(studentSelector, progressArea, newChallengeField, addChallengeButton, newStudentField, addStudentButton);

        // Main scene
        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Teacher View");
        stage.show();
    }
    
    private void addNewChallenge(String newChallenge) {
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> challenges;

        // Load existing challenges
        try (FileReader reader = new FileReader(Utils.CHALLENGES_DATABASE)) {
            challenges = gson.fromJson(reader, type);
            if (challenges == null) {
                challenges = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Utils.showAlert("Error loading challenges.");
            return;
        }

        // Add new challenge
        if (!challenges.contains(newChallenge)) {
            challenges.add(newChallenge);
            try (FileWriter writer = new FileWriter(Utils.CHALLENGES_DATABASE)) {
                gson.toJson(challenges, writer);
            } catch (IOException e) {
                e.printStackTrace();
                Utils.showAlert("Error saving challenges.");
            }
        } else {
            Utils.showAlert("Challenge already exists.");
        }
    }
    
    private void loadStudents(ComboBox<String> comboBox) {
        Type type = new TypeToken<Map<String, StudentData>>() {}.getType();
        Map<String, StudentData> data;

        // Load data from file
        try (FileReader reader = new FileReader(Utils.STUDENTS_DATABASE)) {
            data = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            Utils.showAlert("Error loading students data.");
            return;
        }
        
        if (data == null)
        	return;
        
        // Add students & data to main map
        for (Map.Entry<String,StudentData> entry : data.entrySet()) {
        	studentsData.put(entry.getKey(), entry.getValue());
        }
        	
        // Add items to combo box
        comboBox.getItems().clear();
        if (data != null) {
            comboBox.getItems().addAll(data.keySet());
        }
    }
    
    private void createStudentData(String studentName) {
        if (studentsData.containsKey(studentName)) {
            Utils.showAlert("Student already exists!");
            return;
        }

        // Create new student data and add to main map
        StudentData studentData = new StudentData();
        studentsData.put(studentName, studentData);

        // Append to students file
        try (Writer writer = new FileWriter(Utils.STUDENTS_DATABASE)) {
            gson.toJson(studentsData, writer);
        } catch (IOException e) {
            e.printStackTrace();
            Utils.showAlert("Error saving students data.");
        }
    }
    
    public static void main(String[] args) {
    	// Launch form
        launch(args);
    }
}
