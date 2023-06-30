package com.aclark.iKnowItApp.entities;

import com.aclark.iKnowItApp.dtos.CommentDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comments_id")
    private Long id;
//    [UNSURE] - not sure if needed to create user_id column.
//    @Column(name = "user_id")
//    private Long userId;

    @Column(columnDefinition = "text")
    private String commentBody;

    // Creates 'user_id' column in Posts table.
    @ManyToOne
    @JsonBackReference // Prevents infinite recursion when delivering resource as Json through RESTful API endpoint.
    private User user;

    public Comment(CommentDto commentDto) {
        if (commentDto.getCommentBody() != null) {
            this.commentBody = commentDto.getCommentBody();
        }
    }

//    [EXTRA] - maybe. Not sure if this is needed yet or not.
//    @Column
//    private String threadPath;
}