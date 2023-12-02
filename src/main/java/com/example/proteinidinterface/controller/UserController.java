package com.example.proteinidinterface.controller;

import com.example.proteinidinterface.model.ConfigForm;
import com.example.proteinidinterface.model.User;
import com.example.proteinidinterface.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class UserController {
    @Autowired
    private UserService userService;

}
