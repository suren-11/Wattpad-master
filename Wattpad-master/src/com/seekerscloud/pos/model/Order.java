package com.seekerscloud.pos.model;

import java.util.ArrayList;
import java.util.Date;

public class Order {
    private String orderId;
    private Date placedDate;
    private double total;
    private String customer;
    private ArrayList<CartItem> items;

    public Order() {
    }

    public Order(String orderId, Date placedDate, double total, String customer, ArrayList<CartItem> items) {
        this.orderId = orderId;
        this.placedDate = placedDate;
        this.total = total;
        this.customer = customer;
        this.items = items;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getPlacedDate() {
        return placedDate;
    }

    public void setPlacedDate(Date placedDate) {
        this.placedDate = placedDate;
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

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<CartItem> items) {
        this.items = items;
    }
}
