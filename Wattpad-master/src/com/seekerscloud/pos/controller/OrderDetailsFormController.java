package com.seekerscloud.pos.controller;

import com.jfoenix.controls.JFXTextField;
import com.seekerscloud.pos.db.DBConnection;
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        try {
            if (orderId == null) {
                removeUi();
                return;
            }

            String sql1 = "SELECT customer,date FROM `order` WHERE  orderId = ?";
            PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
            statement1.setString(1,orderId);
            ResultSet set1 = statement1.executeQuery();

            if (set1.next()){
                String customerId = set1.getString(1);
                String date = set1.getString(2);

                String sql2 = "SELECT * FROM Customer WHERE id = ?";
                PreparedStatement statement2 = DBConnection.getInstance().getConnection().prepareStatement(sql2);
                statement2.setString(1, customerId);
                ResultSet set2 = statement2.executeQuery();
                if (set2.next()){
                    txtId.setText(customerId);
                    txtName.setText(set2.getString(2));
                    txtAddress.setText(set2.getString(3));
                    txtSalary.setText(set2.getString(4));

                    txtOrderId.setText(orderId);
                    txtDate.setText(date);

                    String sql3 = "SELECT totalCost FROM `Order` WHERE orderId= ?";
                    PreparedStatement statement4 = DBConnection.getInstance().getConnection().prepareStatement(sql3);
                    statement4.setString(1,orderId);
                    ResultSet set4 = statement4.executeQuery();

                    if (set4.next()){
                        double totalCost = set4.getDouble(1);
                        String sql = "SELECT o.orderId, d.itemCode, d.orderId, d.unitPrice, d.qty FROM `Order` o INNER JOIN `Order Details` d ON o.orderId = d.orderId AND o.orderId = ?";
                        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                        statement.setString(1, orderId);
                        ResultSet set = statement.executeQuery();
                        ObservableList<ItemDetailsTM> tmList = FXCollections.observableArrayList();

                        while (set.next()) {
                            double tempUnitPrice = set.getDouble(4);
                            int tempQty = set.getInt(5);
                            double tempTotal = tempUnitPrice * tempQty;
                            tmList.add(new ItemDetailsTM(set.getString(2), tempUnitPrice, tempQty, tempTotal));
                            txtCost.setText(String.valueOf(totalCost));
                        }
                        tblItemDetails.setItems(tmList);
                    }
                }

            }
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }


    }

    private void removeUi() {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.close();
        new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
    }

}
