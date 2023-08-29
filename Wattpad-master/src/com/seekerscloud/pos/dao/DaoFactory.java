package com.seekerscloud.pos.dao;

import com.seekerscloud.pos.dao.custom.implement.CartItemImpl;
import com.seekerscloud.pos.dao.custom.implement.CustomerDaoImpl;
import com.seekerscloud.pos.dao.custom.implement.OrderDaoImpl;
import com.seekerscloud.pos.dao.custom.implement.ProductDaoImpl;

public class DaoFactory {
    private static DaoFactory daoFactory;
    private DaoFactory(){}
    public static DaoFactory getInstance(){
        return daoFactory ==null?(daoFactory = new DaoFactory()):daoFactory;
    }
    public <T> T getDto(DaoTypes types){
        switch (types){
            case Customer:
                return (T) new CustomerDaoImpl();
            case Product:
                return (T) new ProductDaoImpl();
            case Order:
                return (T) new OrderDaoImpl();
            case CartItem:
                return (T) new CartItemImpl();
            default:
                return null;
        }
    }
}
