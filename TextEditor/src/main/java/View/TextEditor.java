package View;

import Model.TextDocumentModel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class TextEditor extends Application {
    private TextDocumentModel documentModel;
    private TextArea textArea;

    @Override
    public void start(Stage primaryStage) {
        documentModel = new TextDocumentModel();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(5));

        textArea = new TextArea();
        root.setCenter(textArea);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Document");
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                saveDocument(file);
            }
        });


        Button loadButton = new Button("Load");
        loadButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Document");
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                loadDocument(file);
            }
        });

        Button browseButton = new Button("Browse Folder");
        browseButton.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Browse Folder");
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory != null) {
                browseFolder(selectedDirectory);
            }
        });

        BorderPane buttonContainer = new BorderPane();
        buttonContainer.setTop(saveButton);
        buttonContainer.setLeft(loadButton);
        buttonContainer.setBottom(browseButton);
        root.setBottom(buttonContainer);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Text Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveDocument(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            String text = textArea.getText();
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDocument(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            String text = stringBuilder.toString();
            textArea.setText(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void browseFolder(File directory) {
        StringBuilder stringBuilder = new StringBuilder();
        browseFolderRecursive(directory, stringBuilder);
        String text = stringBuilder.toString();
        textArea.setText(text);
    }

    private void browseFolderRecursive(File directory, StringBuilder stringBuilder) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    browseFolderRecursive(file, stringBuilder);
                } else {
                    stringBuilder.append(file.getAbsolutePath()).append("\n");
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}