package com.example.finaltodo.Home;

import com.example.finaltodo.Database.DBUtil;
import com.example.finaltodo.MainApplication;
import com.example.finaltodo.ToDo.ToDo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private ObservableList<ToDo> toDosArray = FXCollections.observableArrayList();
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    @FXML
    private Label helloLabel;
    @FXML
    private TableView<ToDo> table;
    @FXML
    private TableColumn<ToDo, Integer> idCol;
    @FXML
    private TableColumn<ToDo, String> statusCol;
    @FXML
    private TableColumn<ToDo, String> descriptionCol;
    @FXML
    private TextField idField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private ChoiceBox<String> statusField;
    @FXML
    private Button addToDo;
    @FXML
    private Button updateToDo;
    @FXML
    private Button deleteToDo;
    @FXML
    private Button logOutBtn;


    public void initialize(URL location, ResourceBundle resources){
        helloLabel.setText("Welcome, " + DBUtil.getLoggedInUser().getName() + "!");

        // Setting Status Field Values
        statusField.getItems().add("To Do");
        statusField.getItems().add("In Progress");
        statusField.getItems().add("Done");
        statusField.setValue("To Do");

        setToDos();
        showToDos();

        // Listening to Click Events of buttons
        addToDo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                addOrEditToDo(true);
            }
        });
        updateToDo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                addOrEditToDo(false);
            }
        });
        deleteToDo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                deleteToDo();
            }
        });
        logOutBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logOut();
            }
        });
    }

    // Getting ToDos from DB with User ID
    public void setToDos(){
        try {
            Statement statement = DBUtil.getConnection().createStatement();
            int userID = DBUtil.getLoggedInUser().getId();
            String query = "select  * from todos where user_id = \"" + userID + "\"";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                String status = resultSet.getString("status");
                ToDo todo = new ToDo(id, description,status);
                toDosArray.add(todo);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    // Displaying ToDos List in Table
    public void showToDos(){
     for (ToDo toDo : toDosArray){
            idCol.setCellValueFactory(new PropertyValueFactory<ToDo, Integer>("id"));
            statusCol.setCellValueFactory(new PropertyValueFactory<ToDo, String>("status"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<ToDo, String>("description"));

            table.setItems(toDosArray);
        }
    }

    // Getting index of Item in an array using its ID
    public int indexOfItemById(ObservableList<ToDo> array, int targetId) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getId() == targetId) {
                return i;
            }
        }
        return -1;
    }

    public void addOrEditToDo(boolean adding){
        String idValue = idField.getText();
        String statusValue = statusField.getValue();
        String descriptionValue = descriptionField.getText();

        // Check Empty Field Errors
        boolean error = idValue.equals("") || descriptionValue.equals("");
        if(error) {
            showAlert(Alert.AlertType.ERROR, "All Fields are required.");
        }else{
            try{
                int userID = DBUtil.getLoggedInUser().getId();
                ToDo toDo = new ToDo(Integer.parseInt(idValue), descriptionValue, statusValue);

                String insertQuery = "insert into todos (id, user_id, description, status) VALUES (?, ?, ?, ?)";
                String updateQuery = "update todos set description = ?, status = ? WHERE id = ? AND user_id = ?";
                String query = adding ? insertQuery : updateQuery;

                PreparedStatement statement = DBUtil.getConnection().prepareStatement(query);

                if(adding) {
                    // Adding new To Do
                    statement.setInt(1, toDo.getId());
                    statement.setInt(2, userID);
                    statement.setString(3, toDo.getDescription());
                    statement.setString(4, toDo.getStatus());
                } else{
                    // Updating new To Do
                    statement.setString(1, toDo.getDescription());
                    statement.setString(2, toDo.getStatus());
                    statement.setInt(3, toDo.getId());
                    statement.setInt(4, userID);
                }

                int rowsAffected = statement.executeUpdate();

                // Check the result
                String reqStatus = adding ? "Adding" : "Updating";
                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.CONFIRMATION, reqStatus + " To Do is Successfull!");

                    if(adding){
                        toDosArray.add(toDo);
                    }else{
                        int index = indexOfItemById(toDosArray, toDo.getId());
                        toDosArray.set(index, toDo);
                    }
                    showToDos();
                    resetFields();
                } else {
                     showAlert(Alert.AlertType.ERROR, reqStatus + " Failed!");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, e.getMessage());
            }
        }

    }

    public void deleteToDo(){
        String idValue = idField.getText();
        if(idValue.equals("")){
            showAlert(Alert.AlertType.ERROR, "ID Field is required.");
        }else{
            try {
                int parsedId = Integer.parseInt(idValue);
                int index = indexOfItemById(toDosArray,  parsedId);
                if(index != -1){
                    try{
                        String query = "delete from todos where id = ?;";
                        PreparedStatement statement = DBUtil.getConnection().prepareStatement(query);
                        statement.setInt(1, parsedId);

                        int rowsAffected = statement.executeUpdate();

                        // Check the result
                        if (rowsAffected > 0) {
                            showAlert(Alert.AlertType.CONFIRMATION, "Deleting To Do is Successfull!");
                            toDosArray.remove(index);
                            showToDos();
                            resetFields();
                        }else{
                            showAlert(Alert.AlertType.ERROR, "Deleting Failed!");
                        }
                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, e.getMessage());
                    }
                }else{
                    showAlert(Alert.AlertType.ERROR, "Please Enter Valid ID.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "ID must be a number.");
            }
        }
    }

    // Reset Fields after Adding, Updating or Deleting
    public void resetFields(){
        idField.setText("");
        descriptionField.setText("");
        statusField.setValue("To Do");
    }

    public void logOut(){
        try{
            MainApplication.switchScene("login.fxml");
        }
        catch (IOException e){
            showAlert(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    // Displaying an Alert For errors or confirmation
    private void showAlert(Alert.AlertType type, String msg){
        alert.setAlertType(type);
        alert.setContentText(msg);
        alert.show();
    }
}
