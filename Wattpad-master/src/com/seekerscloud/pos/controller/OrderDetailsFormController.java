package com.seekerscloud.pos.controller;

import com.jfoenix.controls.JFXTextField;
import com.seekerscloud.pos.dao.DaoFactory;
import com.seekerscloud.pos.dao.DaoTypes;
import com.seekerscloud.pos.dao.custom.OrderDao;
import com.seekerscloud.pos.dao.custom.implement.OrderDaoImpl;
import com.seekerscloud.pos.db.DBConnection;
import com.seekerscloud.pos.db.Database;
import com.seekerscloud.pos.entity.Order;
import com.seekerscloud.pos.model.CartItem;
import com.seekerscloud.pos.model.Customer;
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
    private OrderDao orderDao = DaoFactory.getInstance().getDto(DaoTypes.Order);

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
            ResultSet set1 = orderDao.loadOrderData(orderId);
            if (set1.next()){
                String customerId = set1.getString(1);
                String date = set1.getString(2);
                ResultSet set2 = orderDao.selectedCustomer(customerId);
                if (set2.next()){
                    txtId.setText(customerId);
                    txtName.setText(set2.getString(2));
                    txtAddress.setText(set2.getString(3));
                    txtSalary.setText(set2.getString(4));

                    txtOrderId.setText(orderId);
                    txtDate.setText(date);

                    ResultSet set4 = orderDao.selectedTotal(orderId);
                    if (set4.next()){
                        double totalCost = set4.getDouble(1);
                        ResultSet set = orderDao.detailLoad(orderId);
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
