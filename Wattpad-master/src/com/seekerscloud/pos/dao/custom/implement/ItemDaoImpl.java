package com.seekerscloud.pos.dao.custom.implement;

import com.seekerscloud.pos.dao.custom.ItemDao;
import com.seekerscloud.pos.entity.Product;

public class ItemDaoImpl implements ItemDao {
    @Override
    public boolean save(Product product) {
        return false;
    }

    @Override
    public boolean update(Product product) {
        return false;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }
}
