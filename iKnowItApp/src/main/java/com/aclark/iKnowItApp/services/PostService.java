package com.aclark.iKnowItApp.services;

import com.aclark.iKnowItApp.dtos.PostDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PostService {
    @Transactional
    List<PostDto> getAllUserPosts(Long userId);

    @Transactional
    void addPost(PostDto postDto, Long userId);

    @Transactional
    void deletePost(Long postId);

    @Transactional
    void updatePost(PostDto postDto);

    @Transactional
    Optional<PostDto> findPost(Long postId);
}
