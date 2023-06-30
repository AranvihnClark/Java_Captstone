package com.aclark.iKnowItApp.controllers;

import com.aclark.iKnowItApp.dtos.UserDto;
import com.aclark.iKnowItApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    // Remember! Controllers interact with interfaces!!
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * This accepts the user's User input and determines if the user can be created (or in this case just simply agrees to create).
     *
     * @param userDto the user's inputted User data
     * @return a List String based on the userService's addUser method.
     */
    @PostMapping("/register") // Ensures that the method triggers when we reach the '/register' endpoint
    public List<String> addUser(@RequestBody UserDto userDto) { // @RequestBody maps the JSON body object on the request to our DTO (I assume this means what the user inputs in the html becomes usable on the Java end).

        // This hashes the user's inputted password.
        String passHash = passwordEncoder.encode(userDto.getPassword());

        // Then we replace what the user inputted with the hashed password
        userDto.setPassword(passHash);

        // Then we create a user, if they don't already exist based on the addUser method.
        return userService.addUser(userDto);
    }

    @PostMapping("/login")
    public List<String> userLogin(@RequestBody UserDto userDto) {
        return userService.userLogin(userDto);
    }
}
