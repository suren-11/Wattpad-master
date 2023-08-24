package com.seekerscloud.pos.view.tm;

import javafx.scene.control.Button;

public class OrderDetailsTM {
    private String orderId;
    private String date;
    private double total;
    private String customer;
    private Button btn;

    public OrderDetailsTM() {
    }

    public OrderDetailsTM(String orderId, String date, double total, String customer, Button btn) {
        this.orderId = orderId;
        this.date = date;
        this.total = total;
        this.customer = customer;
        this.btn = btn;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
