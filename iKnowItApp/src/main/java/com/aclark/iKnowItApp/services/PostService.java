package com.aclark.iKnowItApp.services;

import com.aclark.iKnowItApp.dtos.PostDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PostService {
    @Transactional
    List<PostDto> getAllPosts();

    @Transactional
    void addPost(PostDto postDto, Long userId);

    @Transactional
    void deletePost(Long postId);

    @Transactional
    void updatePostTitle(PostDto postDto);

    @Transactional
    void updatePostBody(PostDto postDto);

    @Transactional
    Optional<PostDto> findPost(Long postId);
}
