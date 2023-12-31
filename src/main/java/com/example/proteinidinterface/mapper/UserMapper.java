package com.example.proteinidinterface.mapper;

import com.example.proteinidinterface.dto.SignUpDto;
import com.example.proteinidinterface.dto.UserDto;
import com.example.proteinidinterface.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static UserDto toUserDto (User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static User signUpToUser (SignUpDto userDto) {
        User user = new User();
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        return user;
    }
}
