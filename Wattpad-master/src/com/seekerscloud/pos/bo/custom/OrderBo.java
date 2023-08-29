package com.seekerscloud.pos.bo.custom;

import com.seekerscloud.pos.dto.OrderDto;

import java.util.ArrayList;

public interface OrderBo {
    public boolean saveOrder(OrderDto dto);
    public boolean updateOrder(OrderDto dto);
    public boolean deleteOrder(String id);
    public ArrayList<OrderDto> setOrder(String text);
}
