package com.example.proteinidinterface.controller;


import com.example.proteinidinterface.config.UserAuthProvider;
import com.example.proteinidinterface.dto.CredentialsDto;
import com.example.proteinidinterface.dto.SignUpDto;
import com.example.proteinidinterface.dto.UserDto;
import com.example.proteinidinterface.model.Search;
import com.example.proteinidinterface.model.User;
import com.example.proteinidinterface.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final UserAuthProvider userAuthProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
        UserDto user = userService.login(credentialsDto);
        user.setToken(userAuthProvider.createToken(user));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto) {
        UserDto createdUser = userService.register(signUpDto);
        createdUser.setToken(userAuthProvider.createToken(createdUser));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }

    @PostMapping("/searches")
    public List<Search> listSearches(@RequestParam("username") String username) {
        User user = userService.findByUsername(username);
        List<Search> searches = user.getSearches();
        System.out.println("test");
        for (Search search : searches) {
            search.setUser(null);
            search.setSearchResult(null);
        }
        return searches;
    }
}
