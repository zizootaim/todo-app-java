package com.example.finaltodo.SignUp;

import com.example.finaltodo.Database.DBUtil;
import com.example.finaltodo.MainApplication;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    @FXML
    TextField userNameField;
    @FXML
    PasswordField passwordField;
    @FXML
    Button signUpBtn;

    public void initialize(URL location, ResourceBundle resources){
        signUpBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                signUp();
            }
        });
    }

    // Adding User to DB
    void signUp(){
        String name = userNameField.getText();
        String password = passwordField.getText();

        boolean error = name.equals("") || password.equals("");
        if(error){
            showAlert(Alert.AlertType.ERROR, "All Fields are required.");
        }else{
            try{
                String query = "insert into users (user_id, username, password) VALUES (?, ?, ?)";

                PreparedStatement statement = DBUtil.getConnection().prepareStatement(query);

                int id = getRandomNumber(1,1000);
                statement.setInt(1, id);
                statement.setString(2, name);
                statement.setString(3, password);

                int rowsAffected = statement.executeUpdate();

                // Check the result
                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.CONFIRMATION, "User Addition is Successfull!");
                    try{
                        DBUtil.setUser(id, name);
                        MainApplication.switchScene("home.fxml");
                    }
                    catch (IOException e){
                        showAlert(Alert.AlertType.ERROR, e.getMessage());
                    }
                }
            }catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, e.getMessage());
            }
        }
    }

    public int getRandomNumber(int min, int max) {
        Random random = new Random();

        return random.nextInt((max - min) + 1) + min;
    }

    private void showAlert(Alert.AlertType type, String msg){
        alert.setAlertType(type);
        alert.setContentText(msg);
        alert.show();
    }
}
