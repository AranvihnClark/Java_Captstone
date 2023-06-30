package com.aclark.iKnowItApp.services;

import com.aclark.iKnowItApp.dtos.CommentDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    @Transactional
    List<CommentDto> getAllUserComments(Long userId);

    @Transactional
    void addComment(CommentDto commentDto, Long userId);

    @Transactional
    void deleteComment(Long commentId);

    @Transactional
    void updateComment(CommentDto commentDto);

    @Transactional
    Optional<CommentDto> findComment(Long commentId);
}
