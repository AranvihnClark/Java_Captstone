package com.aclark.iKnowItApp.controllers;

import com.aclark.iKnowItApp.dtos.CommentDto;
import com.aclark.iKnowItApp.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // End-points

    @GetMapping("/user/{userId}")
    public List<CommentDto> getAllUserComments(@PathVariable Long userId) {
        return commentService.getAllUserComments(userId);
    }

    @PostMapping("user/{userId}")
    public void addComment(@RequestBody CommentDto commentDto, @PathVariable Long userId) {
        commentService.addComment(commentDto, userId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

    @PutMapping
    public void updateComment(@RequestBody CommentDto commentDto) {
        commentService.updateComment(commentDto);
    }

    @GetMapping("/{commentId}")
    public Optional<CommentDto> findComment(@PathVariable Long commentId) {
        return commentService.findComment(commentId);
    }
}
