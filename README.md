# Programming-Pals
Final project submission for CS1530

1) CD into the root directory of the project

- ```cd <path-to-folder>/Programming-Pals```

3) prepare project

- ```mvn clean package```

3) launching the .jar files from the root directory

- To launch StudentView.java:
  - Mac
    - ```java --module-path "JavaFX_Mac/lib" --add-modules javafx.controls,javafx.fxml -jar target/student-view-jar-with-dependencies.jar```
  - Windows
    - ```java --module-path "JavaFX_Win/lib" --add-modules javafx.controls,javafx.fxml -jar target/student-view-jar-with-dependencies.jar```
- To launch TeacherView.java:
  - Mac
    - ```java --module-path "JavaFX_Mac/lib" --add-modules javafx.controls,javafx.fxml -jar target/teacher-view-jar-with-dependencies.jar```
  - Windows
    - ```java --module-path "JavaFX_Win/lib" --add-modules javafx.controls,javafx.fxml -jar target/teacher-view-jar-with-dependencies.jar```
