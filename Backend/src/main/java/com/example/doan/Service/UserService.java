package com.example.doan.Service;

import com.example.doan.Dto.UserDto;

public interface UserService {
    boolean register(UserDto userDto);
    UserDto findUserByUserName(String username);
}
