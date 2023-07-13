package com.aclark.iKnowItApp.services;

import com.aclark.iKnowItApp.dtos.CommentDto;
import com.aclark.iKnowItApp.dtos.PostDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    @Transactional
    List<CommentDto> getAllPostComments(Long postId);

    @Transactional
    void addComment(CommentDto commentDto, Long userId, Long postId);

    @Transactional
    void deleteComment(Long commentId);

    @Transactional
    void updateComment(CommentDto commentDto);

    @Transactional
    Optional<CommentDto> findComment(Long commentId);

    @Transactional
    PostDto getPost(Long postId);

    @Transactional
    void updatePost(PostDto postDto);

    @Transactional
    void updateCommentKnowIt(CommentDto commentDto);
}
