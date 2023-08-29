package com.seekerscloud.pos.dao.custom.implement;

import com.seekerscloud.pos.dao.CrudUtil;
import com.seekerscloud.pos.dao.custom.CustomerDao;
import com.seekerscloud.pos.entity.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDaoImpl implements CustomerDao {
    @Override
    public boolean save(Customer c) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Customer VALUES (?,?,?,?)",
                c.getId(),c.getName(),c.getAddress(),c.getSalary());
    }

    @Override
    public boolean update(Customer customer) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Customer SET name  = ?, address = ?, salary = ? WHERE id = ?",
                customer.getName(),customer.getAddress(),customer.getSalary(),customer.getId());
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Customer WHERE id = ?",s);
    }

    @Override
    public ArrayList<Customer> setData(String text) throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT * FROM Customer WHERE name LIKE ? || address LIKE ?",
                text,text);
        ArrayList<Customer> list = new ArrayList<>();
        while (set.next()){
            list.add(new Customer(set.getString(1),
                    set.getString(2),
                    set.getString(3),
                    set.getDouble(4)));
        }
        return list;
    }

    @Override
    public ResultSet setId() throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("SELECT * FROM Customer ORDER BY id DESC");
    }
}
