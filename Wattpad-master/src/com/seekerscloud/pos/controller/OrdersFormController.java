package com.seekerscloud.pos.controller;

import com.seekerscloud.pos.dao.custom.OrderDao;
import com.seekerscloud.pos.dao.custom.implement.OrderDaoImpl;
import com.seekerscloud.pos.db.DBConnection;
import com.seekerscloud.pos.db.Database;
import com.seekerscloud.pos.model.Order;
import com.seekerscloud.pos.view.tm.OrderDetailsTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Optional;

public class OrdersFormController {
    public AnchorPane ordersContext;
    public TableView<OrderDetailsTM> tblOrders;
    public TableColumn colOrderId;
    public TableColumn colOrderDate;
    public TableColumn colTotal;
    public TableColumn colCustomerId;
    public TableColumn colOption;
    public void initialize(){
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customer"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        loadData();

        //=====================
        tblOrders.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue!=null){
                        try {
                            openDetailsUi(newValue);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

        });
        //=====================

    }

    private void openDetailsUi(OrderDetailsTM value) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/OrderDetailsForm.fxml"));
            Parent parent = fxmlLoader.load();
            OrderDetailsFormController detailsController = fxmlLoader.getController();
            detailsController.loadData(value.getOrderId());
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.show();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void loadData() {
        try {
            ResultSet set = new OrderDaoImpl().loadData();
            ObservableList<OrderDetailsTM> tmList = FXCollections.observableArrayList();

            while (set.next()){
                Button btn = new Button("Delete");
                OrderDetailsTM tm = new OrderDetailsTM(
                        set.getString(1),
                        set.getString(2),
                        set.getDouble(3), set.getString(4), btn);
                tmList.add(tm);

                btn.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> val = alert.showAndWait();
                    if (val.get() == ButtonType.YES) {
                        try {
                            if (new OrderDaoImpl().delete(tm.getOrderId())){
                                new Alert(Alert.AlertType.CONFIRMATION, "Order Deleted!").show();
                                loadData();
                            }else {
                                new Alert(Alert.AlertType.WARNING,"Try Again").show();
                            }
                        }catch (ClassNotFoundException | SQLException d){
                            d.printStackTrace();
                        }
                    }

                });
            }
            tblOrders.setItems(tmList);
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashBoardForm", "Dashboard");
    }
    private void setUi(String location,String title) throws IOException {
        Stage window = (Stage)ordersContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml")))
        );
    }
}
