package com.seekerscloud.pos.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.seekerscloud.pos.db.DBConnection;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        try {
            String sql = "SELECT email,password FROM `User` WHERE email = ?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,txtUserName.getText());
            ResultSet set = statement.executeQuery();
            String username = txtUserName.getText().trim();
            String password = txtPassword.getText().trim();
            while (set.next()){
                String realUserName = set.getString(1);
                String realPassword = set.getString(2);
                if (username.equals(realUserName)) { /* we must encrypt them*/
                    if (password.equals(realPassword)){
                        setUi("DashBoardForm", set.getString(1));
                    }else {
                        new Alert(Alert.AlertType.WARNING,
                                "Password is Incorrect!").show();
                    }
                } return;
            }
            // find the user with inserted email => user table

            new Alert(Alert.AlertType.INFORMATION,
                    "Can't find any user details with this username").show();
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }

    }
}
