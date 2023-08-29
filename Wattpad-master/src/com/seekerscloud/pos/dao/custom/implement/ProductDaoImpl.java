package com.seekerscloud.pos.dao.custom.implement;

import com.seekerscloud.pos.dao.CrudUtil;
import com.seekerscloud.pos.dao.custom.ProductDao;
import com.seekerscloud.pos.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductDaoImpl implements ProductDao {
    @Override
    public boolean save(Product product) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Product VALUES (?,?,?,?)",
                product.getCode(),product.getDescription(),product.getUnitPrice(),product.getQtyOnHand());
    }

    @Override
    public boolean update(Product product) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Product SET description  = ?, unitPrice = ?, qtyOnHand = ? WHERE code = ?",
                product.getDescription(),product.getUnitPrice(),product.getQtyOnHand(),product.getCode());
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Product WHERE code = ?",s);
    }

    @Override
    public ArrayList<Product> setData(String text) throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT * FROM Product WHERE description LIKE ?",text);
        ArrayList<Product> list = new ArrayList<>();
        while (set.next()){
            list.add(new Product(
                    set.getString(1),set.getString(2),set.getDouble(3),set.getInt(4)
            ));
        }
        return list;
    }

    @Override
    public ResultSet setId() throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("SELECT * FROM Product ORDER BY code DESC");
    }

    @Override
    public ResultSet loadCodes() throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("SELECT code FROM Product");
    }
}
