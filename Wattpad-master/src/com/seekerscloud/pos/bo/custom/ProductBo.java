package com.seekerscloud.pos.bo.custom;

import com.seekerscloud.pos.dto.ProductDto;

import java.util.ArrayList;

public interface ProductBo {
    public boolean saveProduct(ProductDto dto);
    public boolean updateProduct(ProductDto dto);
    public boolean deleteProduct(String id);
    public ArrayList<ProductDto> setProduct(String text);
}
