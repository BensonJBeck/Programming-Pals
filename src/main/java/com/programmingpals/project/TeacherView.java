package com.programmingpals.project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
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
        // Dropdown for selecting a student
    	ComboBox<String> studentSelector = new ComboBox<>();
    	loadStudents(studentSelector);

        // Text area for viewing student progress
        TextArea progressArea = new TextArea();
        progressArea.setEditable(false);
        progressArea.setText("Select a student to view their progress."); // Default text

        // Text field for adding a new challenge
        TextField newChallengeField = new TextField();
        newChallengeField.setPromptText("Challenge name...");

        // Button for submitting a new challenge
        Button addChallengeButton = new Button("Add Challenge");
        addChallengeButton.setOnAction(e -> {
            String newChallenge = newChallengeField.getText();
            // TODO: challenge logic
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
