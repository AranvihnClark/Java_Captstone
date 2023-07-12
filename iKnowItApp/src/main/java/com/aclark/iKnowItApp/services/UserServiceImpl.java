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
     * We will be checking for duplicate use of usernames, nicknames, and emails.
     * If any exists, the user-to-be will be alerted of such.
     * When signing up is completed successfully, the user will be transported to the login page.
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

        // Confirms if user's username is not already taken or empty.
        if (user.getUsername().isEmpty()) {
            response.add("Username cannot be left blank.");
            response.add("error");
        } else if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            response.add("User already exists.");
            response.add("error");

        // Confirms if user's password is not empty.
        } else if (passwordEncoder.matches("", user.getPassword())) {
            response.add("Password cannot be left blank");
            response.add("error");

        // Confirms if password is at least 8 characters long
        } else if (user.getPassword() == null) {
            response.add("Password cannot be shorter than 8 characters in length");
            response.add("error");

            // Confirms if the user's nickname is not already taken.
        } else if (userRepository.findByNickname(user.getNickname()).isPresent()) {
            response.add("Nickname is already in use.");
            response.add("error");

        // Confirms if user's email address is not already taken or empty.
        } else if (user.getEmailAddress().isEmpty()) {
            response.add("Email Address cannot be left blank");
            response.add("error");
        } else if (userRepository.findByEmailAddress(user.getEmailAddress()).isPresent()) {
            response.add("Email Address is already in use.");
            response.add("error");
        } else {

            // If none of the above are duplicates/empty entries, we can save the new user into our database.
            userRepository.saveAndFlush(user);

            // This response will let our JS locate our html page to the path below.
            response.add("http://localhost:8080/login.html");
        }
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
                System.out.println("\nHERE: " + userOptional.get().getId() + "\n");
            } else {
                response.add("Username or password is incorrect.");
            }
        } else {

            // Keeping the response the same for both so that we don't get some crazy person trying to find if a username exists.
            response.add("Username or password is incorrect.");
        }

        return response;
    }
}
