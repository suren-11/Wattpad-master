package com.seekerscloud.pos.bo.custom;

import com.seekerscloud.pos.dto.CartItemDto;

import java.util.ArrayList;

public interface CartItemBo {
    public boolean saveCart(CartItemDto dto);
    public boolean updateCart(CartItemDto dto);
    public boolean deleteCart(String id);
    public ArrayList<CartItemDto> setCart(String text);
}
