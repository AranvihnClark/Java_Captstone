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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
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
            List<Comment> comments = commentRepository.findAll(Sort.by("id"));
            Comment knewItComment = new Comment();
            for (int i = 0; i < comments.size(); i++) {
                if (!comments.get(i).getPost().getId().equals(postId)) {
                    comments.remove(i);
                    i--;
                } else if (comments.get(i).getKnewIt()) {
                    knewItComment = (comments.get(i));
                    comments.remove(i);
                    i--;
                }
            }
            comments.add(0,knewItComment);

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
    @Override
    @Transactional
    public void updatePost(PostDto postDto) {

        // Searches for the post we want to update.
        Optional<Post> postOptional = postRepository.findById(postDto.getId());

        // If the post exists, we will update the post as below.
        // Changed the below to Intellij's format for practice.
        postOptional.ifPresent(post -> {

            post.setPostBody(postDto.getPostBody());
            post.setPostTitle(postDto.getPostTitle());

            // Need to update html name in our database and in the file.
            StringBuilder htmlName = new StringBuilder();
            String basePath = "C:/Users/Kuma/Documents/Perficient/DevmountainBP/Specializations/Java_Capstone/iKnowItApp/src/main/resources/static/posts/";

            // We also need to save the old file name to change it as well.
            String oldName = post.getPostHtmlName();

            htmlName.append("post_");

            for (String s : postDto.getPostTitle().toLowerCase().split(" ")) {
                htmlName.append(s.replaceAll("[^a-zA-Z0-9]", ""));
                htmlName.append("_");
            }

            htmlName.deleteCharAt(htmlName.length() - 1);
            htmlName.append(".html");

            post.setPostHtmlName(htmlName.toString());

            File updateFile = new File(basePath + oldName);

            File renameFile = new File(basePath + htmlName);

            boolean isUpdated = updateFile.renameTo(renameFile);

            if (isUpdated) {
                System.out.println(oldName + " was changed to " + htmlName);
            } else {
                System.out.println("Renaming failed.");
            }

            postRepository.saveAndFlush(post);
        });
    }

    // Update a comment's 'knewIt' boolean.
    @Override
    @Transactional
    public void updateCommentKnowIt(CommentDto commentDto) {
        // Searches for the comment we want to update.
        Optional<Comment> commentOptional = commentRepository.findById(commentDto.getId());

        // If the comment exists, we will update the comment as below.
        // Changed the below to Intellij's format for practice.
        commentOptional.ifPresent(comment -> {
            comment.setKnewIt(commentDto.getKnewIt());
            commentRepository.saveAndFlush(comment);

            // Then we need to also consider the post's 'isAnswered' variable as well.
            comment.getPost().setIsAnswered(true);
            postRepository.saveAndFlush(comment.getPost());
        });
    }
}
