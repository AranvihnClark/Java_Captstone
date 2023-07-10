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

    private Long postId; // May not be needed, we'll see.

    public CommentDto(Comment comment) {
        if (comment.getId() != null) {
            this.id = comment.getId();
            this.postId = comment.getPost().getId();
        }
        if (comment.getCommentBody() != null) {
            this.commentBody = comment.getCommentBody();
        }
    }
}
