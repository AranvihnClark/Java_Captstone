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

    @Column(unique = true)
    private String username;

    @Column
    private String password;

//    [EXTRA]
//    @Column
//    private Boolean isAdmin;
//
//    @Column
//    private String emailAddress;
//
//    @Column
//    private String nickname;
//
//    @Column
//    private String imageUrl;

//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    // Prevents infinite recursion
//    @JsonManagedReference
//    private Set<Section> sectionSet = new HashSet<>();
/*
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    // Prevents infinite recursion
    @JsonManagedReference
    private Set<Comment> commentSet = new HashSet<>();
*/
    // This creates a new User using the UserDto (or a user's input).
    public User(UserDto userDto){
        if (userDto.getUsername() != null){
            this.username = userDto.getUsername();
        }
        if (userDto.getPassword() != null){
            this.password = userDto.getPassword();
        }
    }

}