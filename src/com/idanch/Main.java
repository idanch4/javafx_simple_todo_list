package com.idanch;

import com.idanch.data.ToDoItemsLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void init() {
        try {
            ToDoItemsLoader.getInstance().loadToDoItems();
        }catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 900, 550));
        primaryStage.show();
    }

    @Override
    public void stop() {
        try {
            ToDoItemsLoader.getInstance().saveItemsToFile();
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
