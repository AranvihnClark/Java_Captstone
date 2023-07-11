package com.aclark.iKnowItApp.dtos;

import com.aclark.iKnowItApp.entities.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String commentBody;

    private UserDto userDto;
    private PostDto postDto;

    public CommentDto(Comment comment) {
        if (comment.getId() != null) {
            this.id = comment.getId();
        }
        if (comment.getCommentBody() != null) {
            this.commentBody = comment.getCommentBody();
        }
        if (comment.getUser() != null) {
            this.userDto = new UserDto(comment.getUser());
        }
        if (comment.getPost() != null) {
            this.postDto = new PostDto((comment.getPost()));
        }
    }
}
