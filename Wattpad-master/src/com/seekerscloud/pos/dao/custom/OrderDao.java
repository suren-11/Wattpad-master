package com.seekerscloud.pos.dao.custom;

import com.seekerscloud.pos.dao.CrudDao;
import com.seekerscloud.pos.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface OrderDao extends CrudDao<Order , String> {
    public ResultSet setId() throws SQLException, ClassNotFoundException;
    public ResultSet loadData() throws SQLException, ClassNotFoundException;
    public ResultSet loadOrderData(String id) throws SQLException, ClassNotFoundException;
    public ResultSet selectedCustomer(String id) throws SQLException, ClassNotFoundException;
    public ResultSet selectedProducts(String id) throws SQLException, ClassNotFoundException;
    public ResultSet selectedTotal(String id) throws SQLException, ClassNotFoundException;

    public ResultSet detailLoad(String id) throws SQLException, ClassNotFoundException;
}
