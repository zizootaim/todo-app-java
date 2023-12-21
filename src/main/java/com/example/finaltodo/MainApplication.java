package com.example.finaltodo;

import com.example.finaltodo.Database.DBUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    private static Stage stage;

    // Loading the GUI
    @Override
    public void start(Stage startStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage = startStage;
        stage.setTitle("To Do App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Establishing DB connection
        DBUtil dbUtil = new DBUtil();
        
        // Launching the GUI
        launch();
    }

    // Switching the scenes of GUI
    public static void switchScene(String sceneName)  throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(sceneName));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene);
        stage.show();
    }
}