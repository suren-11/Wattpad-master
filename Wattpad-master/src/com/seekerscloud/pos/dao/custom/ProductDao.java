package com.seekerscloud.pos.dao.custom;

import com.seekerscloud.pos.dao.CrudDao;
import com.seekerscloud.pos.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ProductDao extends CrudDao<Product, String> {
    public ArrayList<Product> setData(String text) throws SQLException, ClassNotFoundException;

    public ResultSet setId() throws SQLException, ClassNotFoundException;
}
