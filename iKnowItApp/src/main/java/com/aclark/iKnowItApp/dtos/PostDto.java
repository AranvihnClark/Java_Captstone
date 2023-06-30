package com.aclark.iKnowItApp.dtos;

import com.aclark.iKnowItApp.entities.Post;
import com.aclark.iKnowItApp.entities.Section;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;
    private String postTitle;
    private String postBody;

    private UserDto userDto;

//    [EXTRA] - Not sure if needed yet.
//    private String postPath;

    public PostDto(Post post) {
        if (post.getId() != null) {
            this.id = post.getId();
        }
        if (post.getPostTitle() != null) {
            this.postTitle = post.getPostTitle();
        }
        if (post.getPostBody() != null) {
            this.postBody = post.getPostBody();
        }
    }
}
