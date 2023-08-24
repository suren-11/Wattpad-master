package com.seekerscloud.pos.view.tm;

public class ItemDetailsTM {
    private String code;
    private double price;
    private int qty;
    private double total;

    public ItemDetailsTM() {
    }

    public ItemDetailsTM(String code, double price, int qty, double total) {
        this.code = code;
        this.price = price;
        this.qty = qty;
        this.total = total;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
