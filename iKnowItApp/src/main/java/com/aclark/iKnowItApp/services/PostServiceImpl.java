package com.aclark.iKnowItApp.services;

import com.aclark.iKnowItApp.dtos.PostDto;
import com.aclark.iKnowItApp.dtos.SectionDto;
import com.aclark.iKnowItApp.entities.Post;
import com.aclark.iKnowItApp.entities.Section;
import com.aclark.iKnowItApp.entities.User;
import com.aclark.iKnowItApp.repositories.PostRepository;
import com.aclark.iKnowItApp.repositories.SectionRepository;
import com.aclark.iKnowItApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.aclark.iKnowItApp.configuration.CopyFile.copyFileUsingStream;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Override
    @Transactional
    public List<PostDto> getAllSectionPosts(Long sectionId) {

        // We're going to use an optional for our sections by using their id as the identifier
        Optional<Section> sectionOptional = sectionRepository.findById(sectionId);

        if (sectionOptional.isPresent()) {
            List<Post> posts = postRepository.findAll();

            for (int i = 0; i < posts.size(); i++) {
                if (!posts.get(i).getSection().getId().equals(sectionId)) {
                    posts.remove(i);
                    i--;
                }
            }

            // The .stream() lets us search for all posts and the .map() converts each post found into a new PostDto.
            // This is needed as one, we want to return a list of PostDto and, two, we need it to be so because we don't want to use the actual posts themselves but a copy of them.
            // The .collect() is used to create an Object Collection that holds the list of PostDto.
            List<PostDto> postDtoList = posts.stream().map(post -> new PostDto(post)).collect(Collectors.toList());

            if (!posts.isEmpty()) {
                return postDtoList;
            } else {
                // Returns an empty list if no 'Post's have been created.
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public void addPost(PostDto postDto, Long sectionId, Long userId) {

        // We need an optional for users as we will be using their id as the identifier for users to call their posts.
        Optional<Section> sectionOptional = sectionRepository.findById(sectionId);
        Optional<User> userOptional = userRepository.findById(userId);

        Post post = new Post(postDto);

        if (sectionOptional.isPresent()) {

                // This is how we set the section_id in our Post table database.
                post.setSection(sectionOptional.get());

                // Set to false as default.
                post.setIsAnswered(false);
            if (userOptional.isPresent()) {

                // This is how we set the user_id in our Post table database.
                post.setUser(userOptional.get());
                try {
                    // We need our template html file (our source file)
                    File source = new File("C:/Users/Kuma/Documents/Perficient/DevmountainBP/Specializations/Java_Capstone/iKnowItApp/src/main/resources/static/template_post.html");

                    // Somewhat convoluted method to create a file path I liked.
                    String basePath = "C:/Users/Kuma/Documents/Perficient/DevmountainBP/Specializations/Java_Capstone/iKnowItApp/src/main/resources/static/";

                    // We need to get the file name and split up any spaces to match our naming conventions when creating files.
                    String[] buildPathSplit = postDto.getPostTitle().toLowerCase().split(" ");

                    // We create a string builder to put the string back together.
                    StringBuilder buildName = new StringBuilder();

                    // We enforce our naming convention with a loop and appends.
                    for (String s : buildPathSplit) {
                        buildName.append(s.replaceAll("[^a-zA-Z0-9]", ""));
                        buildName.append("_");
                    }

                    // We remove the last underscore.
                    // Probably a better way but this was what I came up with on the fly.
                    buildName.deleteCharAt(buildName.length() - 1);

                    // Then we set this section's html path manually.
                    post.setPostHtmlName("post_" + buildName + ".html");

                    // Then we create the actual html file in our path (also our destination file).
                    File newSectionHtml = new File (basePath + post.getPostHtmlName());

                    // Because we are creating a new file in a try/catch statement, I only the if statement here for if the file was created.
                    if (newSectionHtml.createNewFile()) {
                        System.out.println("\nHtml file created: " + newSectionHtml.getName() + "\n");

                        copyFileUsingStream(source, newSectionHtml);
                    }
                } catch (IOException e) {
                    // If the file failed to create itself, thus throwing an IO exception, the below will print out.
                    System.out.println("Error in creating section HTML file.\n");
                    e.printStackTrace();
                }
            }
        }

        // Then, of course, we add this Post to our database.
        postRepository.saveAndFlush(post);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {

        // The user is already logged in and choosing the post they want to delete so we, in theory, only need the post's id.
        // This searches for the user's posts based on the post's id.
        Optional<Post> postOptional = postRepository.findById(postId);

        // Leaving this as below for practice.
        // If the post exists, it will be deleted.
        if (postOptional.isPresent()) {
            postRepository.delete(postOptional.get());

            // For readability, putting file path in a variable.
            String htmlPath = "C:/Users/Kuma/Documents/Perficient/DevmountainBP/Specializations/Java_Capstone/iKnowItApp/src/main/resources/static/";

            // Then we locate where we save our file and delete it.
            File deletedObj = new File(htmlPath + postOptional.get().getPostHtmlName());

            // The below is just in case if for some reason deleting failed, so we can see what file it was for.
            System.out.println();
            if (deletedObj.delete()) {
                System.out.println("Deleted section file: " + deletedObj.getName());
            } else {
                System.out.println("Failed to delete a section file: " + deletedObj.getName());
            }
            System.out.println();
        }
    }

    @Override
    @Transactional
    public void updatePostTitle(PostDto postDto) {
        // Searches for the post we want to update.
        Optional<Post> postOptional = postRepository.findById(postDto.getId());

        // If the post exists, we will update the post as below.
        // Changed the below to Intellij's format for practice.
        postOptional.ifPresent(post -> {
            post.setPostTitle(postDto.getPostTitle());

            // Need to update html name in our database and in the file.
            StringBuilder htmlName = new StringBuilder();
            String basePath = "C:/Users/Kuma/Documents/Perficient/DevmountainBP/Specializations/Java_Capstone/iKnowItApp/src/main/resources/static/";

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

    @Override
    @Transactional
    public void updatePostBody(PostDto postDto) {
        // Searches for the post we want to update.
        Optional<Post> postOptional = postRepository.findById(postDto.getId());

        // If the post exists, we will update the post as below.
        // Changed the below to Intellij's format for practice.
        postOptional.ifPresent(post -> {
            post.setPostBody(postDto.getPostBody());
            postRepository.saveAndFlush(post);
        });
    }

    @Override
    @Transactional
    public Optional<PostDto> findPost(Long postId) {
        // First we need to create an optional to search for the post to avoid nulls.
        Optional<Post> postOptional = postRepository.findById(postId);

        // If the post is exists, we will return the post to the user.
        // Left the code below as is and didn't use IntelliJ's functional style option.
        if (postOptional.isPresent()) {

            // I did have to look at the instructions' picture for this and had to reconstruct this method.
            // I was originally going to just have the return type be just 'Post'.
            // But looking at the instructions' example, I see that we use Optional<PostDto> for two reasons.
            // Optional is used to avoid a null return (as below we return an empty optional).
            // PostDto is used because we can't return a Post as it is not a data transfer object. <-- Need to remember this.
            return Optional.of(new PostDto(postOptional.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void updateIsAnswered(PostDto postDto) {
        // Searching for the post we want to be considered answered.
        Optional<Post> postOptional = postRepository.findById(postDto.getId());

        // If the post exists and is not yet answered, we will set the post to 'answered'.

        postOptional.ifPresent(post -> {
            if (!postOptional.get().getIsAnswered()) {
                post.setIsAnswered(true);

            }
        });
    }

    @Override
    @Transactional
    public SectionDto getSection(Long sectionId) {

        // We're going to use the sectionId to locate our section and make a new dto out of it to send to the front end.
        return new SectionDto(sectionRepository.getReferenceById(sectionId));
    }

    @Override
    @Transactional
    public List<String> getToPost(Long postId) {
        List<String> response = new ArrayList<>();

        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            response.add("http://localhost:8080/" + postOptional.get().getPostHtmlName());
            response.add(String.valueOf(postOptional.get().getId()));
        } else {
            response.add("Post does not exist.");
        }

        return response;
    }
}
