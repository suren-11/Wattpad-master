package com.seekerscloud.pos.controller;

import com.jfoenix.controls.JFXTextField;
import com.seekerscloud.pos.db.Database;
import com.seekerscloud.pos.model.CartItem;
import com.seekerscloud.pos.model.Customer;
import com.seekerscloud.pos.model.Order;
import com.seekerscloud.pos.view.tm.ItemDetailsTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;

public class OrderDetailsFormController {
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtSalary;
    public JFXTextField txtOrderId;
    public JFXTextField txtCost;
    public JFXTextField txtDate;
    public TableView<ItemDetailsTM> tblItemDetails;
    public TableColumn colProductCode;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colTotal;
    public AnchorPane context;

    public void initialize(){
        colProductCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }


    public void loadData(String orderId){
        if (orderId==null){
            removeUi();
            return;
        }

       Order o= Database.orderTable.stream().filter(e->e.getOrderId().equals(orderId))
                .findFirst().orElse(null);

        if (o!=null){

            Customer c= Database.customerTable.stream().filter(e->e.getId().equals(o.getCustomer()))
                    .findFirst().orElse(null);
            if (c!=null){
                //===============
                txtId.setText(c.getId());
                txtName.setText(c.getName());
                txtAddress.setText(c.getAddress());
                txtSalary.setText(String.valueOf(c.getSalary()));
                //===============
                txtOrderId.setText(o.getOrderId());
                txtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(o.getPlacedDate()));
                txtCost.setText(String.valueOf(o.getTotal()));
                //===============
                ObservableList<ItemDetailsTM> tmList = FXCollections.observableArrayList();
                for (CartItem item: o.getItems()
                     ) {
                    tmList.add(new ItemDetailsTM(item.getCode(),
                            item.getUnitPrice(), item.getQty(), (item.getQty())* item.getUnitPrice()));
                }
                tblItemDetails.setItems(tmList);
                //===============

            }else{
                removeUi();
            }

        }else{
            removeUi();
        }


    }

    private void removeUi() {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.close();
        new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
    }

}
