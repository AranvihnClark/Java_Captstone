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
    @Column(name = "comment_id")
    private Long id;

    @Column(columnDefinition = "text")
    private String commentBody;

    @Column(columnDefinition = "boolean default false")
    private Boolean knewIt;

    @Column(columnDefinition = "boolean default false")
    private Boolean almostKnewIt;

    @Column
    private Long likes;

    // Creates 'user_id' column in Posts table.
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // Prevents infinite recursion when delivering resource as Json through RESTful API endpoint.
    private User user;

    // Creates 'post_id' column in Posts table.
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonBackReference // Prevents infinite recursion when delivering resource as Json through RESTful API endpoint.
    private Post post;

    public Comment(CommentDto commentDto) {
        if (commentDto.getCommentBody() != null) {
            this.commentBody = commentDto.getCommentBody();
        }
        this.knewIt = false;
        this.almostKnewIt = false;
        this.likes = 0L;
    }
}
