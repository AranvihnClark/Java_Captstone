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
    private Boolean knewIt;
    private Boolean almostKnewIt;
    private Long likes;

    private UserDto userDto;
    private PostDto postDto;

    public CommentDto(Comment comment) {
        if (comment.getId() != null) {
            this.id = comment.getId();
        }
        if (comment.getCommentBody() != null) {
            this.commentBody = comment.getCommentBody();
        }
        if (comment.getKnewIt() != null) {
            this.knewIt = comment.getKnewIt();
        }
        if (comment.getAlmostKnewIt() != null) {
            this.almostKnewIt = comment.getAlmostKnewIt();
        }
        if (comment.getLikes() != null) {
            this.likes = comment.getLikes();
        }
        if (comment.getUser() != null) {
            this.userDto = new UserDto(comment.getUser());
        }
        if (comment.getPost() != null) {
            this.postDto = new PostDto((comment.getPost()));
        }
    }
}
