package com.seekerscloud.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.seekerscloud.pos.db.DBConnection;
import com.seekerscloud.pos.db.Database;
import com.seekerscloud.pos.model.CartItem;
import com.seekerscloud.pos.model.Customer;
import com.seekerscloud.pos.model.Order;
import com.seekerscloud.pos.model.Product;
import com.seekerscloud.pos.view.tm.CartTM;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

public class PlaceOrderFormController {
    public AnchorPane placeOrderContext;
    public ComboBox<String> cmbCustomerCodes;
    public ComboBox<String> cmbItemCodes;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtSalary;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHand;
    public TextField txtOrderId;
    public TextField txtOrderDate;
    public TextField txtOrderTotal;
    public TextField txtItemCount;
    public TextField txtQty;
    public TableView<CartTM> tblCart;
    public TableColumn<CartTM, String> colCode;
    public TableColumn colDesc;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colTotal;
    public TableColumn colOption;
    public JFXButton addToCartButton;

    public void initialize() {

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        loadCustomerIds();
        loadItemCodes();
        setDate();
        generateOrderId();

        //==============Listeners============
        cmbCustomerCodes.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    setCustomerData(newValue);
                });
        cmbItemCodes.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    setProductData(newValue);
                });
        //==============Listeners============

    }

    private void setDate() {
        txtOrderDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    private void setProductData(String code) {
        try {
            txtQty.requestFocus();
            String sql = "SELECT * FROM Product WHERE code = ?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,code);
            ResultSet set = statement.executeQuery();
            if (set.next()){
                txtDescription.setText(set.getString(2));
                txtUnitPrice.setText(String.valueOf(set.getDouble(3)));
                txtQtyOnHand.setText(String.valueOf(set.getInt(4)));
            }
        }catch (ClassNotFoundException | SQLException s){
            s.printStackTrace();
        }
    }

    private void setCustomerData(String id) {
        try {
            txtQty.requestFocus();
            String sql = "SELECT * FROM Customer WHERE id = ?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,id);
            ResultSet set = statement.executeQuery();
            if (set.next()){
                txtName.setText(set.getString(2));
                txtAddress.setText(set.getString(3));
                txtSalary.setText(String.valueOf(set.getDouble(4)));
            }
        }catch (ClassNotFoundException | SQLException s){
            s.printStackTrace();
        }
    }

    private void loadCustomerIds() {
        try {
            String sql = "SELECT id FROM Customer";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            ArrayList<String> idList = new ArrayList<>();
            while (set.next()) {
                idList.add(set.getString(1));
            }
            ObservableList<String> obList = FXCollections.observableArrayList(idList);
            cmbCustomerCodes.setItems(obList);
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    private void loadItemCodes() {
        try {
            String sql = "SELECT code FROM Product";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            ArrayList<String> codeList = new ArrayList<>();
            while (set.next()){
                codeList.add(set.getString(1));
            }
            ObservableList<String> obList = FXCollections.observableArrayList(codeList);
            cmbItemCodes.setItems(obList);
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashBoardForm", "Dashboard");
    }

    private void setUi(String location, String title) throws IOException {
        Stage window = (Stage) placeOrderContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml")))
        );
    }

    ObservableList<CartTM> tmList = FXCollections.observableArrayList();

    public void addToCart(ActionEvent actionEvent) {
        int qty = Integer.parseInt(txtQty.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        double total = qty * unitPrice;

        if (!checkQty(qty)){
            new Alert(Alert.AlertType.WARNING, "Invalid QTY").show();
            return;
        }

        Button btn = new Button("Remove");
        CartTM tm = new CartTM(cmbItemCodes.getValue(),
                txtDescription.getText(), unitPrice, qty, total, btn);

        CartTM existTm = isExists(cmbItemCodes.getValue());
        if (existTm != null) {

            if (!checkQty(existTm.getQty() + qty)){
                new Alert(Alert.AlertType.WARNING, "Invalid QTY").show();
                return;
            }

            existTm.setQty(existTm.getQty() + qty);
            existTm.setTotal(existTm.getTotal() + total);
        } else {
            tmList.add(tm);
        }

        btn.setOnAction(e->{
            tmList.remove(tm);
            tblCart.refresh();
            setTotalAndCount();
        });

        tblCart.setItems(tmList);
        tblCart.refresh();
        setTotalAndCount();
        clearFields();
    }
    private boolean checkQty(int qty){
      /*  if (Integer.parseInt(txtQtyOnHand.getText())< qty){
            new Alert(Alert.AlertType.WARNING, "Invalid QTY").show();
            return false; // can't add
        }
        return true; // add more*/
       // return Integer.parseInt(txtQtyOnHand.getText()) >= qty;
        return Integer.parseInt(txtQtyOnHand.getText())< qty?false:true;
    }

    private void clearFields() {
        txtDescription.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
        txtQty.clear();
        cmbItemCodes.requestFocus();
    }

    private CartTM isExists(String id) {
        /*for (CartTM tm : tmList
        ) {
            if (tm.getCode().equals(id)) {
                return tm;
            }
        }
        return null;*/

        return tmList.stream().filter(e -> e.getCode().equals(id)).findFirst().orElse(null);
    }

    private void setTotalAndCount(){
        double cost=0;
       /* txtOrderTotal.setText(String.valueOf(0));
        tmList.forEach(e->{
            //txtOrderTotal.setText((Double.parseDouble(txtOrderTotal.getText())+e.getTotal())+"");
            txtOrderTotal.setText(String.valueOf(Double.parseDouble(txtOrderTotal.getText())+e.getTotal()));
        });*/
        for (CartTM tm:tmList
             ) {
            cost+=tm.getTotal();
        }
        txtOrderTotal.setText(String.valueOf(cost));
        txtItemCount.setText(String.valueOf(tmList.size()));
    }

    public void addToCartData(ActionEvent actionEvent) {
        addToCartButton.fire();
    }

    public void newCustomerOnAction(ActionEvent actionEvent) throws IOException {
        setUi("CustomerForm","Customer Management");
    }

    private void placeOrder(){
        ArrayList<CartItem> items= new ArrayList<>();
        for (CartTM tm:tmList
             ) {
            items.add(new CartItem(tm.getCode(),tm.getQty(),tm.getUnitPrice()));
        }
        Order order = new Order(txtOrderId.getText(),new Date(),
                Double.parseDouble(txtOrderTotal.getText()),
                cmbCustomerCodes.getValue(),items);
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO `Order` VALUES (?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, order.getOrderId());
            statement.setString(2, String.valueOf(new Date()));
            statement.setDouble(3,order.getTotal());
            statement.setString(4, order.getCustomer());

            if (statement.executeUpdate()>0){
                boolean allQtyUpdated = manageQty(items);
                if (allQtyUpdated) {
                    conn.commit();
                    new Alert(Alert.AlertType.INFORMATION,
                            "Order Placed!").show();
                    generateOrderId();
                    setFreshUI();
                } else {
                    conn.setAutoCommit(true);
                    conn.rollback();
                    new Alert(Alert.AlertType.WARNING,"Try Again");
                }
            }else {
                conn.setAutoCommit(true);
                conn.rollback();
                new Alert(Alert.AlertType.WARNING,"Try Again").show();
            }
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    private void setFreshUI(){
        cmbCustomerCodes.setValue(null);
        txtName.clear();
        txtAddress.clear();
        txtSalary.clear();
        txtOrderTotal.setText("0");
        txtItemCount.setText("");

        tmList.clear();
        tblCart.refresh();

    }

    private void generateOrderId() {
        try {
            String sql = "SELECT orderId FROM `Order` ORDER BY orderId DESC LIMIT 1";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            if (set.next()){
                String id = set.getString(1);
                //String dataArray[] = id.split("a-z"); // a001 b001
                String dataArray[] = id.split("[a-zA-Z]"); // A001 b001
                id=dataArray[1];
                int oldNumber= Integer.parseInt(id);
                oldNumber++;
                if (oldNumber<9){
                    txtOrderId.setText("B00"+oldNumber);
                }else if(oldNumber<99){
                    txtOrderId.setText("B0"+oldNumber);
                }else{
                    txtOrderId.setText("B"+oldNumber);
                }
            }else {
                txtOrderId.setText("B001");
            }

        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    private boolean manageQty(ArrayList<CartItem> items){

            try {
                for (CartItem i:items
                ) {
                    String sql = "INSERT INTO `Order Details` VALUES (?,?,?,?)";
                    PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                    statement.setString(1, i.getCode());
                    statement.setString(2, txtOrderId.getText());
                    statement.setDouble(3, i.getUnitPrice());
                    statement.setInt(4, i.getQty());

                    boolean orderDetailsSaved = statement.executeUpdate() > 0;
                    if (orderDetailsSaved) {
                        boolean qtyUpdated = qtyUpdate(i);
                        if (!qtyUpdated) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }catch (ClassNotFoundException | SQLException e){
                e.printStackTrace();
            }
        return true;
    }

    private boolean qtyUpdate(CartItem i){
        try {
            String sql = "UPDATE Product SET qtyOnHand = (qtyOnHand - ?) WHERE code = ?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setInt(1,i.getQty());
            statement.setString(2,i.getCode());
            return statement.executeUpdate()>0;
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void placeOrderOnAction(ActionEvent actionEvent) {
        placeOrder();
    }

}

