package com.seekerscloud.pos.bo.custom;

import com.seekerscloud.pos.dto.UserDto;

import java.util.ArrayList;

public interface UserBo {
    public boolean saveUser(UserDto dto);
    public boolean updateUser(UserDto dto);
    public boolean deleteUser(String id);
    public ArrayList<UserDto> setUser(String text);
}
