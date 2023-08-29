package com.seekerscloud.pos.bo;

import com.seekerscloud.pos.dao.DaoTypes;
import com.seekerscloud.pos.dao.custom.implement.CartItemImpl;
import com.seekerscloud.pos.dao.custom.implement.CustomerDaoImpl;
import com.seekerscloud.pos.dao.custom.implement.OrderDaoImpl;
import com.seekerscloud.pos.dao.custom.implement.ProductDaoImpl;

public class BoFactory {
    private static BoFactory daoFactory;
    private BoFactory(){}
    public static BoFactory getInstance(){
        return daoFactory ==null?(daoFactory = new BoFactory()):daoFactory;
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
