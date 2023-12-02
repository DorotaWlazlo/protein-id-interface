package com.example.proteinidinterface.controller;


import com.example.proteinidinterface.dto.CredentialsDto;
import com.example.proteinidinterface.dto.UserDto;
import com.example.proteinidinterface.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
        UserDto user = userService.login(credentialsDto);
        return ResponseEntity.ok(user);
    }
}
