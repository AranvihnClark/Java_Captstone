package com.aclark.iKnowItApp.entities;

import com.aclark.iKnowItApp.dtos.PostDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
//    [UNSURE] - not sure if needed to create user_id column.
//    @Column(name = "user_id")
//    private Long userId;

    @Column
    private String postTitle;

    @Column(columnDefinition = "text")
    private String postBody;

    @Column
    private String postHtmlPath;

    // Creates 'user_id' column in Posts table.
    @ManyToOne
    @JsonBackReference // Prevents infinite recursion when delivering resource as Json through RESTful API endpoint.
    private User user;

    public Post(PostDto postDto) {
        if (postDto.getPostTitle() != null) {
            this.postTitle = postDto.getPostTitle();

            // Somewhat convoluted method to create a file path I liked.
            String basePath = "C:/Users/Kuma/Documents/Perficient/DevmountainBP/Specializations/Java_Capstone/iKnowItApp/src/main/resources/static/posts";

            // We want to get rid of any special characters due to fact that they would get in the way of file naming.
            basePath = basePath.replaceAll("[^a-zA-Z0-9]", "");

            // We need to get the file name and split up any spaces to match our naming conventions when creating files.
            String[] basePathSplit = this.postTitle.toLowerCase().split(" ");

            // We create a string builder to put the string back together.
            StringBuilder buildPath = new StringBuilder();

            // We enforce our naming convention with a loop and appends.
            for (String s : basePathSplit) {
                buildPath.append(s);
                buildPath.append("_");
            }

            // We remove the last underscore.
            // Probably a better way but this was what I came up with on the fly.
            buildPath.deleteCharAt(buildPath.length() - 1);

            // Then we build our path to store.
            this.postHtmlPath = basePath + buildPath;
        }
        if (postDto.getPostBody() != null) {
            this.postTitle = postDto.getPostBody();
        }
    }

//    [EXTRA] - maybe. Not sure if this is needed yet or not.
//    @Column
//    private String threadPath;
}
