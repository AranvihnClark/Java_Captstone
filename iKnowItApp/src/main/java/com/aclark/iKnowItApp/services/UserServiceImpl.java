package com.aclark.iKnowItApp.services;

import com.aclark.iKnowItApp.dtos.UserDto;
import com.aclark.iKnowItApp.entities.User;
import com.aclark.iKnowItApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * This adds a user to the database.
     * Normally, we would check for a duplicate using email address and other identifying parameters but this is just for practice.
     *
     * @param userDto the user's inputted data to create a User object.
     * @return a list string that lets the user know a user was added
     */

    @Override
    @Transactional
    public List<String> addUser(UserDto userDto) {
        // Create a new array list.
        List<String> response = new ArrayList<>();

        // We are creating a new user based on the user input (using userDto to transfer data).
        User user = new User(userDto);

        // Confirms if user's username is not already taken.
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            response.add("User already exists.");
        } else {
            userRepository.saveAndFlush(user);
            response.add("http://localhost:8080/login.html");
        }

        // Then we add a string to our list to let the user know they were successful.
        // In JS, this 'add' of ours will be [0] in our array.
        response.add("http://localhost:8080/login.html");

        return response;
    }

    /**
     * If user login is correct, pushes the user into the homepage and tracks user action via a cookie.
     *
     * @param userDto the inputted user info to check if they exist in our database.
     * @return a string based on whether the userDto matches a user in our database.
     */

    @Override
    public List<String> userLogin(UserDto userDto) {
        // Create a new array list.
        List<String> response = new ArrayList<>();

        // Used to find a username in our database without grabbing a null value that would break the app.
        Optional<User> userOptional = userRepository.findByUsername(userDto.getUsername());

        // Checking to see if the username entered exists in our database.
        if (userOptional.isPresent()) {

            // Checking to see if the password entered matches the username's password in our database.
            // If yes:
            if (passwordEncoder.matches(userDto.getPassword(), userOptional.get().getPassword())) {
                // This will be [0] of our JS code when we reference it later.
                response.add("http://localhost:8080/home.html");

                // Adds the username's string value into our array list.
                // This will be [1]of our JS code when we reference it later.
                response.add(String.valueOf(userOptional.get().getId()));
            } else {
                response.add("Username or password is incorrect.");
            }
        } else {
            response.add("Username or password is incorrect.");
        }

        return response;
    }
}
