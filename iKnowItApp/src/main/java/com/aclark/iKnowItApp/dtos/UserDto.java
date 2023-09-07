package com.aclark.iKnowItApp.dtos;

import com.aclark.iKnowItApp.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {

    // The variables below that a user from the User table can have.
    // Note that the id is autogenerated, so we don't need to mess with it in the constructor below.
    private Long id;
    private String username;
    private String password;
    private String emailAddress;
    private Boolean isAdmin;
    private String nickname;
    private String imageUrl;

    // Creates dto based on user info in our database.
    public UserDto(User user) {
        if (user.getId() != null) {
            this.id = user.getId();
        }
        if (user.getUsername() != null) {
            this.username = user.getUsername();
        }
        if (user.getPassword() != null) {
            this.password = user.getPassword();
        }
        if (user.getEmailAddress() != null) {
            this.emailAddress = user.getEmailAddress();
        }
        if (user.getIsAdmin() != null) {
            this.isAdmin = user.getIsAdmin();
        }
        if (user.getNickname() != null) {
            this.nickname = user.getNickname();
        }
        if (user.getImageUrl() != null) {
            this.imageUrl = user.getImageUrl();
        }
    }
}