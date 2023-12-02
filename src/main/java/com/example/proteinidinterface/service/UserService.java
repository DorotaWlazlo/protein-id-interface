package com.example.proteinidinterface.service;

import com.example.proteinidinterface.dto.CredentialsDto;
import com.example.proteinidinterface.dto.SignUpDto;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByEmail(credentialsDto.login())
                        .orElseGet(() -> userRepository.findByUsername(credentialsDto.login())
                                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND)));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
            return UserMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto userDto) {
        Optional<User> optionalUser = userRepository.findByUsername(userDto.username());

        if (optionalUser.isPresent()) {
            throw new AppException("Username already exists", HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser2 = userRepository.findByEmail(userDto.email());

        if (optionalUser2.isPresent()) {
            throw new AppException("Account associated with this email already exists", HttpStatus.BAD_REQUEST);
        }

        User user = UserMapper.signUpToUser(userDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.password())));

        User savedUser = userRepository.save(user);

        return UserMapper.toUserDto(savedUser);
    }
}
