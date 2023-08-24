package com.seekerscloud.pos.controller;

import com.jfoenix.controls.JFXButton;
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
            if (Database.customerTable.add(customer)){
                new Alert(Alert.AlertType.CONFIRMATION, "Customer Saved!").show();
                setTableData(searchText);
                setCustomerId();
                clear();
            }else{
                new Alert(Alert.AlertType.CONFIRMATION, "Try Again").show();
            }
        }else{
            for(Customer c :Database.customerTable){
                if (txtId.getText().equalsIgnoreCase(c.getId())){
                    c.setName(txtName.getText());
                    c.setAddress(txtAddress.getText());
                    c.setSalary(Double.parseDouble(txtSalary.getText()));
                    new Alert(Alert.AlertType.CONFIRMATION, "Customer Updated!").show();
                    setTableData(searchText);
                    clear();
                }
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
        text = text.toLowerCase(); // String Pool==> Strings are immutable
        ArrayList<Customer> customerList=Database.customerTable;
        ObservableList<CustomerTM> obList= FXCollections.observableArrayList();
        for (Customer c:customerList
             ) {
            if (c.getName().toLowerCase().contains(text) || c.getAddress().toLowerCase().contains(text)){
                Button btn= new Button("Delete");
                CustomerTM tm = new CustomerTM(c.getId(),c.getName(),c.getAddress(),c.getSalary(),btn);
                obList.add(tm);

                btn.setOnAction(e->{
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> val = alert.showAndWait();
                    if (val.get()==ButtonType.YES){
                        Database.customerTable.remove(c);
                        new Alert(Alert.AlertType.CONFIRMATION, "Customer Deleted!").show();
                        setTableData(searchText);
                    }

                });
            }


        }
        tblCustomer.setItems(obList);
    }

    private void setCustomerId(){
        //get last saved customer
        // catch the id (C-001)
        // separate the numbers from the character
        // increment the separated number
        // concat the character again to the incremented number (C-002)
        // set CustomerId
        if (!Database.customerTable.isEmpty()){
            Customer c= Database.customerTable.get(Database.customerTable.size()-1); //[10,20] size=2 => last => size-1 = 1 [10,20]
            String id = c.getId(); // C-002
            String dataArray[] = id.split("-");// => ["C","002"]; // java string class=> split
            id=dataArray[1]; // 002
            int oldNumber= Integer.parseInt(id); // 2 => 00 remove
            oldNumber++; // 3
            if (oldNumber<9){
                txtId.setText("C-00"+oldNumber);
            }else if(oldNumber<99){
                txtId.setText("C-0"+oldNumber);
            }else{
                txtId.setText("C-"+oldNumber);
            }

        }else{
            txtId.setText("C-001");
        }
    }

    public void newCustomerOnAction(ActionEvent actionEvent) {
        clear();
    }
}
