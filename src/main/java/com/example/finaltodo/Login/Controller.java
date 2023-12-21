package com.example.finaltodo.Login;

import com.example.finaltodo.Database.DBUtil;
import com.example.finaltodo.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;

public class Controller {
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    Button loginBtn;
    @FXML
    Button signUpBtn;

    // Getting User from DB and Compare its Credentials
    boolean checkUser(String name, String pass){
        boolean valid = false;
        try {
           Statement statement = DBUtil.getConnection().createStatement();
           String query = "select  * from users where username = \"" + name + "\"" + " and password = \"" + pass + "\"";
           ResultSet resultSet = statement.executeQuery(query);
           while (resultSet.next()){
               int id = resultSet.getInt("user_id");
               DBUtil.setUser(id, name);
               valid = true;
           }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, e.getMessage());
        }
        return valid;
    }

    // Log In Function
    @FXML
    void onLogIn(){
        String usernameText = username.getText();
        String passwordText = password.getText();

        boolean error = usernameText.equals("") || passwordText.equals("");

        if(error){
            showAlert(Alert.AlertType.ERROR, "All Fields are required.");
        }else{
            if(checkUser(usernameText, passwordText)){
                try{
                    MainApplication.switchScene("home.fxml");
                }
                catch (IOException e){
                    showAlert(Alert.AlertType.ERROR, e.getMessage());
                }
            }else {
                showAlert(Alert.AlertType.ERROR, "Username or Password is wrong.");
            }
        }
    }

    @FXML
    void onSignUp() {
        try{
            MainApplication.switchScene("signup.fxml");
        }
        catch (IOException e){
            showAlert(Alert.AlertType.ERROR, e.getMessage());
        }
    }
    private void showAlert(Alert.AlertType type, String msg){
        alert.setAlertType(type);
        alert.setContentText(msg);
        alert.show();
    }
}
