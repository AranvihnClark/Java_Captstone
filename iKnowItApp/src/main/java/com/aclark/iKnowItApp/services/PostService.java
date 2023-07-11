package com.aclark.iKnowItApp.services;

import com.aclark.iKnowItApp.dtos.PostDto;
import com.aclark.iKnowItApp.dtos.SectionDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PostService {
    @Transactional
    List<PostDto> getAllSectionPosts(Long sectionId);

    @Transactional
    void addPost(PostDto postDto, Long sectionId, Long userId);

    @Transactional
    void deletePost(Long postId);

    @Transactional
    Optional<PostDto> findPost(Long postId);

    @Transactional
    void updateIsAnswered(PostDto postDto);

    @Transactional
    SectionDto getSection(Long sectionId);

    @Transactional
    List<String> getToPost(Long postId);
}
