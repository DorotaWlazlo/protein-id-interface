package com.example.proteinidinterface.service;

import com.example.proteinidinterface.dto.CredentialsDto;
import com.example.proteinidinterface.dto.UserDto;
import com.example.proteinidinterface.exception.AppException;
import com.example.proteinidinterface.mapper.UserMapper;
import com.example.proteinidinterface.model.User;
import com.example.proteinidinterface.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import java.nio.CharBuffer;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByEmail(credentialsDto.login())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
            return UserMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
