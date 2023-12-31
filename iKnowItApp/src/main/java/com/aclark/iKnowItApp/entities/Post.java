package com.aclark.iKnowItApp.entities;

import com.aclark.iKnowItApp.dtos.PostDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column
    private String postTitle;

    @Column(columnDefinition = "text")
    private String postBody;

    @Column(columnDefinition = "boolean default false")
    private Boolean isAnswered;

    @Column
    private String postHtmlName;

    // Creates 'user_id' column in Posts table.
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // Prevents infinite recursion when delivering resource as Json through RESTful API endpoint.
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "section_id", nullable = false)
    @JsonBackReference
    private Section section;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    // Prevents infinite recursion
    @JsonBackReference
    private Set<Comment> commentSet = new HashSet<>();

    public Post(PostDto postDto) {
        if (postDto.getPostTitle() != null) {
            this.postTitle = postDto.getPostTitle();
        }
        if (postDto.getPostBody() != null) {
            this.postBody = postDto.getPostBody();
        }
        if (postDto.getPostHtmlName() != null) {
            this.postHtmlName = postDto.getPostHtmlName();
        }
        this.isAnswered = false;
    }
}
