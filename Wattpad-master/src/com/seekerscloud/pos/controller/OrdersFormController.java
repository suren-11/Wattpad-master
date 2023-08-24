package com.seekerscloud.pos.controller;

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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/OrderDetailsForm.fxml"));
        Parent parent = fxmlLoader.load();
        OrderDetailsFormController detailsController = fxmlLoader.getController();
        detailsController.loadData(value.getOrderId());
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.show();
    }

    private void loadData() {
        ObservableList<OrderDetailsTM> tmList= FXCollections.observableArrayList();
        for (Order o : Database.orderTable
                ) {
            Button btn= new Button("Delete");
            OrderDetailsTM tm= new OrderDetailsTM(
                    o.getOrderId(),
                    new SimpleDateFormat("yyyy-MM-dd").format(o.getPlacedDate()),
                    o.getTotal(),o.getCustomer(),btn);
            tmList.add(tm);

            btn.setOnAction(e->{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> val = alert.showAndWait();
                if (val.get()==ButtonType.YES){
                    Database.orderTable.remove(o);
                    new Alert(Alert.AlertType.CONFIRMATION, "Order Deleted!").show();
                    loadData();
                }

            });

        }
        tblOrders.setItems(tmList);
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
