package com.aclark.iKnowItApp.services;

import com.aclark.iKnowItApp.dtos.CommentDto;
import com.aclark.iKnowItApp.dtos.PostDto;
import com.aclark.iKnowItApp.dtos.SectionDto;
import com.aclark.iKnowItApp.entities.Comment;
import com.aclark.iKnowItApp.entities.Post;
import com.aclark.iKnowItApp.entities.User;
import com.aclark.iKnowItApp.repositories.CommentRepository;
import com.aclark.iKnowItApp.repositories.PostRepository;
import com.aclark.iKnowItApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    @Transactional
    public List<CommentDto> getAllPostComments(Long postId) {

        // We need an optional for users as we will be using their id as the identifier
        Optional<Post> postOptional = postRepository.findById(postId);

        // Now to check if the user id exists.
        if (postOptional.isPresent()) {

            // A list of comments is created based on all comments of the userOptional (user) from the database.
            List<Comment> comments = commentRepository.findAll();

            for (int i = 0; i < comments.size(); i++) {
                if (!comments.get(i).getPost().getId().equals(postId)) {
                    comments.remove(i);
                    i--;
                }
            }

            // The .stream() lets us search for all notes and the .map() converts each comment found into a new CommentDto.
            // This is needed as one, we want to return a list of CommentDto and, two, we need it to be so because we don't want to use the actual comments themselves but a copy of them.
            // The .collect() is used to create an Object Collection that holds the list of CommentDto.
            List<CommentDto> commentDtoList = comments.stream().map(comment -> new CommentDto(comment)).collect(Collectors.toList());

            if (!comments.isEmpty()) {
                return commentDtoList;
            } else {
                // Returns an empty list if no 'Post's have been created.
                return Collections.emptyList();
            }
        }

        // Returns an empty list if no 'Comment's have been created.
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public void addComment(CommentDto commentDto, Long userId, Long postId) {

        // We need an optional for users as we will be using their id as the identifier for users to call their comments.
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Post> postOptional = postRepository.findById(postId);
        Comment comment = new Comment(commentDto);

        if (postOptional.isPresent()) {
            if (userOptional.isPresent()) {
                comment.setUser(userOptional.get());
                comment.setPost(postOptional.get());
            }
        }

        // Then, of course, we add this Comment to our database.
        commentRepository.saveAndFlush(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {

        // The user is already logged in and choosing the note they want to delete so we, in theory, only need the comment's id.
        // This searches for the user's comments based on the comment's id.
        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        // Leaving this as below for practice.
        // If the note exists, it will be deleted.
        if (commentOptional.isPresent()) {
            commentRepository.delete(commentOptional.get());
        }
    }

    @Override
    @Transactional
    public void updateComment(CommentDto commentDto) {
        // Searches for the note we want to update.
        Optional<Comment> commentOptional = commentRepository.findById(commentDto.getId());

        // If the note exists, we will update the note as below.
        // Changed the below to Intellij's format for practice.
        commentOptional.ifPresent(comment -> {
            comment.setCommentBody(commentDto.getCommentBody());
            commentRepository.saveAndFlush(comment);
        });
    }

    @Override
    @Transactional
    public Optional<CommentDto> findComment(Long commentId) {
        // First we need to create an optional to search for the note to avoid nulls.
        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        // If the note is exists, we will return the note to the user.
        // Left the code below as is and didn't use IntelliJ's functional style option.
        if (commentOptional.isPresent()) {

            // I did have to look at the instructions' picture for this and had to reconstruct this method.
            // I was originally going to just have the return type be just 'Note'.
            // But looking at the instructions' example, I see that we use Optional<NoteDto> for two reasons.
            // Optional is used to avoid a null return (as below we return an empty optional).
            // NoteDto is used because we can't return a Note as it is not a data transfer object. <-- Need to remember this.
            return Optional.of(new CommentDto(commentOptional.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public PostDto getPost(Long postId) {
        // We're going to use the sectionId to locate our section and make a new dto out of it to send to the front end.
        return new PostDto(postRepository.getReferenceById(postId));
    }
}
