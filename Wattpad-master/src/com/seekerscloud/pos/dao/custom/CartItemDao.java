package com.seekerscloud.pos.dao.custom;

import com.seekerscloud.pos.dao.CrudDao;
import com.seekerscloud.pos.entity.CartItem;

import java.sql.SQLException;

public interface CartItemDao<T,ID > {
    public boolean save(T t,ID id) throws SQLException, ClassNotFoundException;
    public boolean update(T t) throws SQLException, ClassNotFoundException;

}
