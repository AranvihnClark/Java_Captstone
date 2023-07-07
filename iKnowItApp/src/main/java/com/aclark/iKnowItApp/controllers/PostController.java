package com.aclark.iKnowItApp.controllers;

import com.aclark.iKnowItApp.dtos.PostDto;
import com.aclark.iKnowItApp.services.PostService;
import com.aclark.iKnowItApp.services.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private SectionService sectionService;

    // End-points

    @GetMapping("/sections/{sectionId}")
    public List<PostDto> getAllSectionPosts(@PathVariable Long sectionId) {
        return postService.getAllSectionPosts(sectionId);
    }

    @PostMapping("/sections/{sectionId}/users/{userId}")
    public void addPost(@RequestBody PostDto postDto, @PathVariable Long sectionId, @PathVariable Long userId) {
        postService.addPost(postDto, sectionId, userId);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
    }

    @PutMapping
    public void updatePost(@RequestBody PostDto postDto) {
        postService.updatePostTitle(postDto);
        postService.updatePostBody(postDto);
    }

    @GetMapping("/{postId}")
    public Optional<PostDto> findPost(@PathVariable Long postId) {
        return postService.findPost(postId);
    }

    @PutMapping("/{postId}")
    public void updateIsAnswered(@RequestBody PostDto postDto) {
        postService.updateIsAnswered(postDto);
    }
}
