package com.seekerscloud.pos.dao.custom.implement;

import com.seekerscloud.pos.dao.CrudUtil;
import com.seekerscloud.pos.dao.custom.CartItemDao;
import com.seekerscloud.pos.entity.CartItem;

import java.sql.SQLException;

public class CartItemImpl implements CartItemDao<CartItem,String> {


    @Override
    public boolean save(CartItem i, String s) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO `Order Details` VALUES (?,?,?,?)",i.getCode(),s,i.getUnitPrice(),i.getQty());
    }

    @Override
    public boolean update(CartItem i) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Product SET qtyOnHand = (qtyOnHand - ?) WHERE code = ?",i.getQty(),i.getCode());
    }



}
