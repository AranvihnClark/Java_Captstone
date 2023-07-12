package com.aclark.iKnowItApp.entities;

import com.aclark.iKnowItApp.dtos.UserDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

// This generates a table in our database.
// @Data creates the setters and getters.
// @AllArgsConstructor creates a constructor using all arguments available.
// @NoArgsConstructor creates a constructor with no arguments.
@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    // Below are the table columns.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String emailAddress;

    @Column(nullable = false)
    private Boolean isAdmin;

    @Column(unique = true)
    private String nickname;

    @Column
    private String imageUrl;

    public User(UserDto userDto){
        if (userDto.getUsername() != null) {
            this.username = userDto.getUsername();
        }
        if (userDto.getPassword() != null) {
            this.password = userDto.getPassword();
        }
        if (userDto.getEmailAddress() != null) {
            this.emailAddress = userDto.getEmailAddress();
            if (userDto.getEmailAddress().equals("aranvihn.clark@gmail.com")) {
                this.isAdmin = true;
            } else {
                this.isAdmin = false;
            }
        }
        if (!userDto.getNickname().equals("")) {
            this.nickname = userDto.getNickname();
        } else {
            this.nickname = userDto.getUsername();
        }
        this.imageUrl = "../profileImages/template_profile_image.png";
    }

}