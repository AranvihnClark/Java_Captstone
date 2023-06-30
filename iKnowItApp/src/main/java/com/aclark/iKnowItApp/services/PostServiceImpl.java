package com.aclark.iKnowItApp.services;

import com.aclark.iKnowItApp.dtos.PostDto;
import com.aclark.iKnowItApp.entities.Post;
import com.aclark.iKnowItApp.entities.User;
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
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public List<PostDto> getAllUserPosts(Long userId) {

        // We need an optional for users as we will be using their id as the identifier
        Optional<User> userOptional = userRepository.findById(userId);

        // Now to check if the user id exists.
        if (userOptional.isPresent()) {

            // A list of posts is created based on all posts of the userOptional (user) from the database.
            List<Post> posts = postRepository.findAllByUserEquals(userOptional.get());

            // The .stream() lets us search for all notes and the .map() converts each post found into a new PostDto.
            // This is needed as one, we want to return a list of PostDto and, two, we need it to be so because we don't want to use the actual posts themselves but a copy of them.
            // The .collect() is used to create an Object Collection that holds the list of PostDto.
            List<PostDto> postDtoList = posts.stream().map(post -> new PostDto(post)).collect(Collectors.toList());

            return postDtoList;
        }

        // Returns an empty list if no 'Post's have been created.
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public void addPost(PostDto postDto, Long userId) {

        // We need an optional for users as we will be using their id as the identifier for users to call their posts.
        Optional<User> userOptional = userRepository.findById(userId);

        Post post = new Post(postDto);

        if (userOptional.isPresent()) {
            post.setUser(userOptional.get());
        }

        // Then, of course, we add this Post to our database.
        postRepository.saveAndFlush(post);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {

        // The user is already logged in and choosing the note they want to delete so we, in theory, only need the post's id.
        // This searches for the user's posts based on the post's id.
        Optional<Post> postOptional = postRepository.findById(postId);

        // Leaving this as below for practice.
        // If the note exists, it will be deleted.
        if (postOptional.isPresent()) {
            postRepository.delete(postOptional.get());
        }
    }

    @Override
    @Transactional
    public void updatePostTitle(PostDto postDto) {
        // Searches for the note we want to update.
        Optional<Post> postOptional = postRepository.findById(postDto.getId());

        // If the note exists, we will update the note as below.
        // Changed the below to Intellij's format for practice.
        postOptional.ifPresent(post -> {
            post.setPostTitle(postDto.getPostTitle());
            postRepository.saveAndFlush(post);
        });
    }

    @Override
    @Transactional
    public void updatePostBody(PostDto postDto) {
        // Searches for the note we want to update.
        Optional<Post> postOptional = postRepository.findById(postDto.getId());

        // If the note exists, we will update the note as below.
        // Changed the below to Intellij's format for practice.
        postOptional.ifPresent(post -> {
            post.setPostBody(postDto.getPostBody());
            postRepository.saveAndFlush(post);
        });
    }

    @Override
    @Transactional
    public Optional<PostDto> findPost(Long postId) {
        // First we need to create an optional to search for the note to avoid nulls.
        Optional<Post> postOptional = postRepository.findById(postId);

        // If the note is exists, we will return the note to the user.
        // Left the code below as is and didn't use IntelliJ's functional style option.
        if (postOptional.isPresent()) {

            // I did have to look at the instructions' picture for this and had to reconstruct this method.
            // I was originally going to just have the return type be just 'Note'.
            // But looking at the instructions' example, I see that we use Optional<NoteDto> for two reasons.
            // Optional is used to avoid a null return (as below we return an empty optional).
            // NoteDto is used because we can't return a Note as it is not a data transfer object. <-- Need to remember this.
            return Optional.of(new PostDto(postOptional.get()));
        } else {
            return Optional.empty();
        }
    }
}
