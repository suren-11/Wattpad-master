package com.seekerscloud.pos.controller;

import com.jfoenix.controls.JFXButton;
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
        text = text.toLowerCase(); // String Pool==> Strings are immutable
        ArrayList<Product> productList= Database.productTable;
        ObservableList<ProductTM> obList= FXCollections.observableArrayList();
        for (Product p:productList
        ) {
            if (p.getDescription().toLowerCase().contains(text)){
                Button btn= new Button("Delete");
                ProductTM tm = new ProductTM(p.getCode(),p.getDescription(),p.getUnitPrice(),p.getQtyOnHand(),btn);
                obList.add(tm);

                btn.setOnAction(e->{
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> val = alert.showAndWait();
                    if (val.get()==ButtonType.YES){
                        Database.productTable.remove(p);
                        new Alert(Alert.AlertType.CONFIRMATION, "Product Deleted!").show();
                        setTableData(searchText);
                    }

                });
            }


        }
        tblProducts.setItems(obList);
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
        if (!Database.productTable.isEmpty()){
            Product c= Database.productTable.get(Database.productTable.size()-1);
            String id = c.getCode();
            String dataArray[] = id.split("-");
            id=dataArray[1];
            int oldNumber= Integer.parseInt(id);
            oldNumber++;
            if (oldNumber<9){
                txtCode.setText("D-00"+oldNumber);
            }else if(oldNumber<99){
                txtCode.setText("D-0"+oldNumber);
            }else{
                txtCode.setText("D-"+oldNumber);
            }

        }else{
            txtCode.setText("D-001");
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
            if (Database.productTable.add(product)){
                new Alert(Alert.AlertType.CONFIRMATION, "Products Saved!").show();
                setTableData(searchText);
                setItemCode();
                clear();
            }else{
                new Alert(Alert.AlertType.CONFIRMATION, "Try Again").show();
            }
        }else{
            for(Product p :Database.productTable){
                if (txtCode.getText().equalsIgnoreCase(p.getCode())){
                    p.setDescription(txtDescription.getText());
                    p.setUnitPrice(Double.parseDouble(txtUnitPrice.getText()));
                    p.setQtyOnHand(Integer.parseInt(txtQtyOnHand.getText()));
                    new Alert(Alert.AlertType.CONFIRMATION, "Product Updated!").show();
                    setTableData(searchText);
                    clear();
                }
            }
        }
    }
}
