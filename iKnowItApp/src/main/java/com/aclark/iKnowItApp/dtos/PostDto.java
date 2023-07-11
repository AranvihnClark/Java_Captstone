package com.aclark.iKnowItApp.dtos;

import com.aclark.iKnowItApp.entities.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;
    private String postTitle;
    private String postBody;
    private Boolean isAnswered;
    private String postHtmlName;

    private UserDto userDto;
    private SectionDto sectionDto;

    private Set<CommentDto> commentDtoSet = new HashSet<>();

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
        if (post.getPostHtmlName() != null) {
            this.postHtmlName = post.getPostHtmlName();
        }
        if (post.getIsAnswered() != null) {
            this.isAnswered = post.getIsAnswered();
        }
        if (post.getUser() != null) {
            this.userDto = new UserDto(post.getUser());
            this.userDto.setPassword(null);
        }
        if (post.getSection() != null) {
            this.sectionDto = new SectionDto((post.getSection()));
        }
    }
}
