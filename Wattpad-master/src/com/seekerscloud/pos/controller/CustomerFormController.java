package com.seekerscloud.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.seekerscloud.pos.db.DBConnection;
import com.seekerscloud.pos.db.Database;
import com.seekerscloud.pos.model.Customer;
import com.seekerscloud.pos.view.tm.CustomerTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class CustomerFormController {
    public AnchorPane customerFormContext;
    public TextField txtId;
    public TextField txtSalary;
    public TextField txtAddress;
    public TextField txtName;
    public TableView<CustomerTM> tblCustomer;
    public TableColumn colCustomerId;
    public TableColumn colCustomerName;
    public TableColumn colAddress;
    public TableColumn colSalary;
    public TableColumn colOption;
    public JFXButton btnSaveUpdate;
    public TextField txtSearch;
    private String searchText="";

    public void initialize(){
        setTableData(searchText);
        setCustomerId();

        //==============
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        //==============

        //=============Listeners=============
        tblCustomer.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (null!=newValue){
                        setCustomerData(newValue);
                    }

        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText=newValue;
            setTableData(searchText);
        });

        //=============Listeners=============

    }
    private void setCustomerData(CustomerTM tm){
        txtId.setText(tm.getId());
        txtName.setText(tm.getName());
        txtAddress.setText(tm.getAddress());
        txtSalary.setText(String.valueOf(tm.getSalary()));

        btnSaveUpdate.setText("Update Customer");

    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashBoardForm", "Dashboard");
    }

    private void setUi(String location,String title) throws IOException {
        Stage window = (Stage)customerFormContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml")))
        );
    }

    public void saveUpdateOnAction(ActionEvent actionEvent) {

                Customer customer = new Customer(
                txtId.getText(),
                txtName.getText(),
                txtAddress.getText(),
                Double.parseDouble(txtSalary.getText())
        );

        if (btnSaveUpdate.getText().equalsIgnoreCase("Save Customer")){
            //save
            try{
                String sql ="INSERT INTO Customer VALUES (?,?,?,?)";
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1, customer.getId());
                statement.setString(2, customer.getName());
                statement.setString(3, customer.getAddress());
                statement.setDouble(4, customer.getSalary());
                if (statement.executeUpdate()>0){
                    new Alert(Alert.AlertType.CONFIRMATION, "Customer Saved!").show();
                    setTableData(searchText);
                    setCustomerId();
                    clear();
                }else{
                    new Alert(Alert.AlertType.CONFIRMATION, "Try Again").show();
                }
            }catch (ClassNotFoundException | SQLException e){
                e.printStackTrace();
            }

        }else{
            try {
                String sql = "UPDATE Customer SET name  = ?, address = ?, salary = ? WHERE id = ?";
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1, customer.getName());
                statement.setString(2, customer.getAddress());
                statement.setDouble(3, customer.getSalary());
                statement.setString(4, customer.getId());

                if (statement.executeUpdate()>0){
                    new Alert(Alert.AlertType.CONFIRMATION, "Customer Updated!").show();
                    setTableData(searchText);
                    clear();
                }else {
                    new Alert(Alert.AlertType.WARNING, "Try Again").show();
                }
            }catch (ClassNotFoundException | SQLException e){
                e.printStackTrace();
            }

        }

    }

    private void clear(){
        btnSaveUpdate.setText("Save Customer");

        txtName.clear();
        txtAddress.setText("");
        txtSalary.clear();
        setCustomerId();
    }

    private void setTableData(String text){
        text = "%"+text.toLowerCase()+"%"; // String Pool==> Strings are immutable
        try {

            ObservableList<CustomerTM> obList= FXCollections.observableArrayList();
            String sql="SELECT * FROM Customer WHERE name LIKE ? || address LIKE ?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,text);
            statement.setString(2,text);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Button btn= new Button("Delete");
                CustomerTM tm = new CustomerTM(set.getString(1),set.getString(2),set.getString(3),set.getDouble(4),btn);
                obList.add(tm);

                btn.setOnAction(e->{
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> val = alert.showAndWait();
                    if (val.get()==ButtonType.YES){
                        try {
                            String sql1="DELETE FROM Customer WHERE id = ?";
                            PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
                            statement1.setString(1, tm.getId());
                            if (statement1.executeUpdate()>0){
                                new Alert(Alert.AlertType.CONFIRMATION, "Customer Deleted!").show();
                                setCustomerId();
                                setTableData(searchText);
                            }else {
                                new Alert(Alert.AlertType.WARNING, "Try Again").show();
                            }

                        }catch (ClassNotFoundException | SQLException s){
                            s.printStackTrace();
                        }

                    }

                });
            }
            tblCustomer.setItems(obList);
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }

    }

    private void setCustomerId(){
        //get last saved customer
        // catch the id (C-001)
        // separate the numbers from the character
        // increment the separated number
        // concat the character again to the incremented number (C-002)
        // set CustomerId
        try{
            String sql="SELECT * FROM Customer ORDER BY id DESC";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet set = statement.executeQuery();

            if (set.next()){
//                txtId.setText("C-001");
                String id = set.getString(1);
                String[] dataArray = id.split("-");// => ["C","002"]; // java string class=> split
                String ids=dataArray[1]; // 002
                int oldNumber= Integer.parseInt(ids); // 2 => 00 remove
                oldNumber++; // 3
                if (oldNumber<9){
                    txtId.setText("C-00"+oldNumber);
                }else if(oldNumber<99){
                    txtId.setText("C-0"+oldNumber);
                }else{
                    txtId.setText("C-"+oldNumber);
                }
            }else {
                txtId.setText("C-001");
            }
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }

    }

    public void newCustomerOnAction(ActionEvent actionEvent) {
        clear();
    }
}
