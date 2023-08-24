package com.seekerscloud.pos.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.seekerscloud.pos.db.Database;
import com.seekerscloud.pos.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class LoginFormController {
    public AnchorPane loginFormContext;
    public JFXTextField txtUserName;
    public JFXPasswordField txtPassword;

    public void createAnAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUi("SignUpForm", "Signup Form");
    }

    private void setUi(String location, String title) throws IOException {
        Stage window = (Stage) loginFormContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml")))
        );
    }

    public void loginOnAction(ActionEvent actionEvent) throws IOException {
        // get user details from the interface
        String username = txtUserName.getText().trim();
        String password = txtPassword.getText().trim();
        // find the user with inserted email => user table
        for (User u : Database.userTable) {
            System.out.println(u.getEmail());
            if (u.getEmail().equals(username)) {
                // check passwords
                // if correct => redirect to the dashboard otherwise the system must show an error.
                if (u.getPassword().equals(password)){ /* we must encrypt them*/
                    setUi("DashBoardForm", u.getEmail());
                }else{
                    new Alert(Alert.AlertType.WARNING,
                            "Password is Incorrect!").show();
                }
                return;
            }
        }
        new Alert(Alert.AlertType.INFORMATION,
                "Can't find any user details with this username").show();

    }
}
