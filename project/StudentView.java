package com.programmingpals.project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;

import java.lang.reflect.Type;

import java.io.*;
import java.util.*;

public class StudentView extends Application {
	
	private Map<String, StudentData> studentsData = new HashMap<>();
    private String currentStudentName = "not set";
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    public void setCurrentStudentName(String name) {
    	
    	if (name == null || name.trim().isEmpty()) 
    	{
    		currentStudentName = "anonymous";
    	}
    	else
    	{
    		currentStudentName = name.trim();
    	}
    }


    
    public String getCurrentStudentName()
    {
    	return currentStudentName;
    }
    
    @Override
    public void start(Stage stage) {
        Random rand = new Random();
        
        // figure out a way to self identify
        // right now the code uses "test name 1" as seen above
        // need a way to say who you are so your submissions
        // go to the right place
        TextInputDialog loginScreen = new TextInputDialog();
        loginScreen.setTitle("Student Login");
        loginScreen.setHeaderText("Please enter username. Leave blank for anonymity.");
        loginScreen.setContentText("Full name:");
        // System.out.println("reached");
        
        Optional<String> inputName = loginScreen.showAndWait();
        setCurrentStudentName(inputName.orElse(null));        

        // List of challenges
        ListView<String> challengeList = new ListView<>();
        loadChallenges(challengeList);
        challengeList.setPrefWidth(350);

        // Text area for writing solution
        TextArea solutionArea = new TextArea();
        solutionArea.setPromptText("Write your solution here...");
        solutionArea.setPrefWidth(250);

        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String selectedChallenge = challengeList.getSelectionModel().getSelectedItem();
                int score = rand.nextInt(101); // Generate a random score

                // Create and add the challenge attempt
                addChallengeAttempt(selectedChallenge, score);

                // Show alert
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Autograder");
                alert.setHeaderText("Assignment Submitted");
                alert.setContentText("Your solution for '" + selectedChallenge + "' earned a " + score + "%");
                alert.showAndWait();
            }
        });

        // Horizontal layout
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(challengeList, solutionArea);

        // Vertical layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(hbox, submitButton);

        // Main scene
        Scene scene = new Scene(layout, 700, 400);
        stage.setScene(scene);
        if (currentStudentName == null)
        {
        	stage.setTitle("Logged In As: Anonymous");
        }
        else 
        {
        	stage.setTitle("Logged In As: " + currentStudentName);
        }
        stage.show();
        
        // Load data
        loadStudentData();
    }
    
    private void addChallengeAttempt(String challengeName, int score) {
        ChallengeAttempt newAttempt = new ChallengeAttempt();
        newAttempt.ChallengeName = challengeName;
        newAttempt.Score = score;

        StudentData studentData = studentsData.getOrDefault(currentStudentName, new StudentData());
        boolean found = false;
        for (ChallengeAttempt attempt : studentData.ChallengeAttempts) {
            if (attempt.ChallengeName.equals(challengeName)) {
                // Update only if score is higher
                if (score > attempt.Score) {
                    attempt.Score = score;
                }
                found = true;
                break;
            }
        }

        // Didn't find existing attempt
        if (!found) {
            studentData.ChallengeAttempts.add(newAttempt);
        }

        studentsData.put(currentStudentName, studentData);

        // Save the updated data
        saveStudentsData();
    }

    private void saveStudentsData() {
        try (Writer writer = new FileWriter(Utils.STUDENTS_DATABASE)) {
            gson.toJson(studentsData, writer);
        } catch (IOException e) {
            e.printStackTrace();
            Utils.showAlert("Error saving students data.");
        }
    }
    
    private void loadStudentData() {
    	// Load current student data into main map
        Type type = new TypeToken<Map<String, StudentData>>() {}.getType();
        try (FileReader reader = new FileReader(Utils.STUDENTS_DATABASE)) {
            studentsData = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            Utils.showAlert("Error loading students data. Creating default student 'user'.");
            
            // create students.json
            String initialUserData = "{\n" +
                    "  \"user\": {\n" +
                    "    \"ChallengeAttempts\": [\n" +
                    "      {\n" +
                    "        \"ChallengeName\": \"Calculate the factorial of a number\",\n" +
                    "        \"Score\": 27\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"ChallengeName\": \"Implement the Fibonacci sequence\",\n" +
                    "        \"Score\": 22\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"ChallengeName\": \"Find the intersection of two arrays\",\n" +
                    "        \"Score\": 31\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}";

                try (FileWriter writer = new FileWriter("students.json")) {
                    writer.write(initialUserData);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Utils.showAlert("Error creating students.json file.");
                }
            }

            if (studentsData == null) {
                studentsData = new HashMap<>();
            }
        }
    
    private void loadChallenges(ListView<String> listView) {
        // Local current challenge list
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> challenges;

        try (FileReader reader = new FileReader(Utils.CHALLENGES_DATABASE)) {
            challenges = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            Utils.showAlert("Error loading challenges. Creating default challenges.");

            // create challenges.json
            List<String> defaultChallenges = Arrays.asList(
                "Reverse a string",
                "Calculate the factorial of a number",
                "Find the largest number in an array",
                "Sort an array of integers",
                "Check if a string is a palindrome",
                "Implement the Fibonacci sequence",
                "Find the common elements in two arrays",
                "Implement a binary search algorithm",
                "Calculate the sum of digits of a number",
                "Check if a number is prime",
                "Find the first non-repeated character in a string",
                "Implement a basic calculator for addition and subtraction",
                "Count the number of vowels in a string",
                "Convert a binary number to decimal",
                "Merge two sorted arrays",
                "Find the second highest number in an array",
                "Remove duplicates from an array",
                "Rotate an array to the right by k steps",
                "Find the intersection of two arrays",
                "Convert a decimal number to binary",
                "sort a list",
                "newChallenge2"
            );
            
            try (FileWriter writer = new FileWriter(Utils.CHALLENGES_DATABASE)) {
                gson.toJson(defaultChallenges, writer);
                challenges = defaultChallenges;
            } catch (IOException e2) {
                e2.printStackTrace();
                Utils.showAlert("Error creating challenges.json.");
                return;
            }
        }

        // Add to list
        if (challenges != null) {
            listView.getItems().addAll(challenges);
        }
    }

    public static void main(String[] args) {
    	// Launch form
        launch(args);
    }
}
