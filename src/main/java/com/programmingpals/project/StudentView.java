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

import java.util.Random;

public class StudentView extends Application {

    @Override
    public void start(Stage stage) {
        Random rand = new Random();

        // List of challenges
        ListView<String> challengeList = new ListView<>();
        challengeList.getItems().addAll("Challenge 1", "Challenge 2", "Challenge 3");
        challengeList.setPrefWidth(150);

        // Text area for writing solution
        TextArea solutionArea = new TextArea();
        solutionArea.setPromptText("Write your solution here...");
        solutionArea.setPrefWidth(250);

        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Autograder");
                alert.setHeaderText("Assignment Submitted");
                alert.setContentText("Your solution earned a " + rand.nextInt(101) + "%");
                alert.showAndWait();
            }
        });

        // Horizontal layout for list and text area
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(challengeList, solutionArea);

        // Vertical layout for the entire view
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(hbox, submitButton);

        // Scene and Stage
        Scene scene = new Scene(layout, 450, 400); // Adjust the size as needed
        stage.setScene(scene);
        stage.setTitle("Student View");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
