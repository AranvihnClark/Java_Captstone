package com.aclark.iKnowItApp.controllers;

import com.aclark.iKnowItApp.dtos.PostDto;
import com.aclark.iKnowItApp.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // End-points

    @GetMapping("/user/{userId}")
    public List<PostDto> getAllUserPosts(@PathVariable Long userId) {
        return postService.getAllUserPosts(userId);
    }

    @PostMapping("user/{userId}")
    public void addPost(@RequestBody PostDto postDto, @PathVariable Long userId) {
        postService.addPost(postDto, userId);
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
}
