package com.seekerscloud.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.seekerscloud.pos.db.DBConnection;
import com.seekerscloud.pos.db.Database;
import com.seekerscloud.pos.model.Customer;
import com.seekerscloud.pos.model.Product;
import com.seekerscloud.pos.view.tm.CustomerTM;
import com.seekerscloud.pos.view.tm.ProductTM;
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

public class ProductFormController {
    public AnchorPane productContext;
    public TextField txtCode;
    public TextField txtQtyOnHand;
    public TextField txtUnitPrice;
    public TextField txtDescription;
    public TextField txtSearch;
    public JFXButton btnSaveUpdate;
    public TableView<ProductTM> tblProducts;
    public TableColumn colProductCode;
    public TableColumn colProductDescription;
    public TableColumn colProductUnitPrice;
    public TableColumn colQtyOnHand;
    public TableColumn colOption;
    private String searchText="";
    public void initialize(){
        setTableData(searchText);
        setItemCode();

        //==============
        colProductCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colProductDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colProductUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        //==============

        //=============Listeners=============
        tblProducts.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (null!=newValue){
                        setProductData(newValue);
                    }

                });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText=newValue;
            setTableData(searchText);
        });

        //=============Listeners=============

    }

    private void setProductData(ProductTM tm) {
        txtCode.setText(tm.getCode());
        txtDescription.setText(tm.getDescription());
        txtUnitPrice.setText(String.valueOf(tm.getUnitPrice()));
        txtQtyOnHand.setText(String.valueOf(tm.getQtyOnHand()));

        btnSaveUpdate.setText("Update Product");
    }

    private void setTableData(String text){
        text = "%"+text.toLowerCase()+"%"; // String Pool==> Strings are immutable
        try {

            ObservableList<ProductTM> obList= FXCollections.observableArrayList();
            String sql="SELECT * FROM Product WHERE description LIKE ?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,text);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Button btn= new Button("Delete");
                ProductTM tm = new ProductTM(set.getString(1),set.getString(2),set.getDouble(3),set.getInt(4),btn);
                obList.add(tm);

                btn.setOnAction(e->{
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> val = alert.showAndWait();
                    if (val.get()==ButtonType.YES){
                        try {
                            String sql1="DELETE FROM Product WHERE code = ?";
                            PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
                            statement1.setString(1, tm.getCode());
                            if (statement1.executeUpdate()>0){
                                new Alert(Alert.AlertType.CONFIRMATION, "Product Deleted!").show();
                                setTableData(searchText);
                                setItemCode();
                            }else {
                                new Alert(Alert.AlertType.WARNING, "Try Again").show();
                            }
                        }catch (ClassNotFoundException | SQLException s){
                            s.printStackTrace();
                        }
                    }
                });
            }
            tblProducts.setItems(obList);
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashBoardForm", "Dashboard");
    }
    private void setUi(String location,String title) throws IOException {
        Stage window = (Stage)productContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml")))
        );
    }
    public void newProductOnAction(ActionEvent actionEvent) {
        clear();
    }
    private void clear(){
        btnSaveUpdate.setText("Save Customer");
        txtDescription.clear();
        txtUnitPrice.setText("");
        txtQtyOnHand.clear();
        setItemCode();
    }

    private void setItemCode(){
        try{
            String sql="SELECT * FROM Product ORDER BY code DESC";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet set = statement.executeQuery();

            if (set.next()){
//                txtCode.setText("I-001");
                String code = set.getString(1);
                String[] dataArray = code.split("-");// => ["I","001"]; // java string class=> split
                String codes=dataArray[1]; // 001
                int oldNumber= Integer.parseInt(codes); // 1 => 00 remove
                oldNumber++; // 2
                if (oldNumber<9){
                    txtCode.setText("I-00"+oldNumber);
                }else if(oldNumber<99){
                    txtCode.setText("I-0"+oldNumber);
                }else{
                    txtCode.setText("I-"+oldNumber);
                }
            }else {
                txtCode.setText("I-001");
            }
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    public void saveUpdateOnAction(ActionEvent actionEvent) {
        Product product = new Product(
                txtCode.getText(),
                txtDescription.getText(),
                Double.parseDouble(txtUnitPrice.getText()),
                Integer.parseInt(txtQtyOnHand.getText())
        );

        if (btnSaveUpdate.getText().equalsIgnoreCase("Save Product")){
            //save
            try{
                String sql ="INSERT INTO Product VALUES (?,?,?,?)";
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1, product.getCode());
                statement.setString(2, product.getDescription());
                statement.setDouble(3, product.getUnitPrice());
                statement.setInt(4, product.getQtyOnHand());
                if (statement.executeUpdate()>0){
                    setItemCode();
                    new Alert(Alert.AlertType.CONFIRMATION, "Product Saved!").show();
                    setTableData(searchText);
                    clear();
                }else{
                    new Alert(Alert.AlertType.CONFIRMATION, "Try Again").show();
                }
            }catch (ClassNotFoundException | SQLException e){
                e.printStackTrace();
            }
        }else{
            try {
                String sql = "UPDATE Product SET description  = ?, unitPrice = ?, qtyOnHand = ? WHERE code = ?";
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1, product.getDescription());
                statement.setDouble(2, product.getUnitPrice());
                statement.setInt(3, product.getQtyOnHand());
                statement.setString(4, product.getCode());

                if (statement.executeUpdate()>0){
                    new Alert(Alert.AlertType.CONFIRMATION, "Product Updated!").show();
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
}
