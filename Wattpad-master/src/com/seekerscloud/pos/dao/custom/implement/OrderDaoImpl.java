package com.seekerscloud.pos.dao.custom.implement;

import com.seekerscloud.pos.dao.CrudUtil;
import com.seekerscloud.pos.dao.custom.OrderDao;
import com.seekerscloud.pos.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderDaoImpl implements OrderDao {
    @Override
    public boolean save(Order order) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO `Order` VALUES (?,?,?,?)",order.getOrderId(),
                new SimpleDateFormat("dd-MM-yyyy").format(new Date()),order.getTotal(),order.getCustomer());
    }

    @Override
    public boolean update(Order order) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM `Order` WHERE orderId = ?",s);
    }

    @Override
    public ResultSet setId() throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("SELECT orderId FROM `Order` ORDER BY orderId DESC LIMIT 1");
    }

    @Override
    public ResultSet loadData() throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("SELECT * FROM `Order`");
    }

    @Override
    public ResultSet loadOrderData(String s) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM `Order` WHERE orderId = ?",s);
    }

    @Override
    public ResultSet selectedCustomer(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("SELECT * FROM Customer WHERE id = ?",id);
    }

    @Override
    public ResultSet selectedTotal(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("SELECT totalCost FROM `Order` WHERE orderId= ?",id);
    }

    @Override
    public ResultSet detailLoad(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("SELECT o.orderId, d.itemCode, d.orderId, d.unitPrice, d.qty FROM `Order` o INNER JOIN `Order Details` d ON o.orderId = d.orderId AND o.orderId = ?",id);
    }
}
