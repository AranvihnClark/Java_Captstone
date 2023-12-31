package com.aclark.iKnowItApp.services;

import com.aclark.iKnowItApp.dtos.SectionDto;
import com.aclark.iKnowItApp.dtos.UserDto;
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
public class SectionServiceImpl implements SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    @Transactional
    public List<SectionDto> getAllSections() {
        // A list of sections is created based on all sections of the userOptional (user) from the database.
        List<Section> sections = sectionRepository.findAll();

        // The .stream() lets us search for all sections and the .map() converts each section found into a new SectionDto.
        // This is needed as one, we want to return a list of SectionDto and, two, we need it to be so because we don't want to use the actual sections themselves but a copy of them.
        // The .collect() is used to create an Object Collection that holds the list of SectionDto.
        List<SectionDto> sectionDtoList = sections.stream().map(section -> new SectionDto(section)).collect(Collectors.toList());

        if (!sections.isEmpty()) {
            return sectionDtoList;
        } else {
            // Returns an empty list if no 'Section's have been created.
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public void addSection(SectionDto sectionDto, Long userId) {

        // We need an optional for users as we will be using their id as the identifier for users to call their sections.
        Optional<User> userOptional = userRepository.findById(userId);

        Section section = new Section(sectionDto);

        if (userOptional.isPresent()) {
            section.setUser(userOptional.get());

            try {
                // We need our template html file (our source file)
                File source = new File("C:/Users/Kuma/Documents/Perficient/DevmountainBP/Specializations/Java_Capstone/iKnowItApp/src/main/resources/static/sections/template_section.html");

                // Somewhat convoluted method to create a file path I liked.
                String basePath = "C:/Users/Kuma/Documents/Perficient/DevmountainBP/Specializations/Java_Capstone/iKnowItApp/src/main/resources/static/sections/";

                // We need to get the file name and split up any spaces to match our naming conventions when creating files.
                String[] buildPathSplit = sectionDto.getSectionTitle().toLowerCase().split(" ");

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
                section.setSectionHtmlName("section_" + buildName + ".html");

                // Then we create the actual html file in our path (also our destination file).
                File newSectionHtml = new File (basePath + section.getSectionHtmlName());

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

        // Then, of course, we add this Section to our database.
        sectionRepository.saveAndFlush(section);
    }

    @Override
    @Transactional
    public void deleteSection(Long sectionId) {

        // The user is already logged in and choosing the section they want to delete so we, in theory, only need the section's id.
        // This searches for the user's sections based on the section's id.
        Optional<Section> sectionOptional = sectionRepository.findById(sectionId);

        // Needed to make sure no posts exists in the section.
        List<Post> postList = postRepository.findAll();

        for (int i = 0; i < postList.size(); i++) {
            if (!postList.get(i).getSection().getId().equals(sectionId)) {
                postList.remove(i);
                i--;
            }
        }

        // Leaving this as below for practice.
        // If the section exists, it will be deleted.
        if (sectionOptional.isPresent()) {

            // Checks to make sure there are no posts in the section - mainly so a person doesn't accidentally delete a section by mistake.
            if (postList.isEmpty()) {
                sectionRepository.delete(sectionOptional.get());

                // For readability, putting file path in a variable.
                String htmlPath = "C:/Users/Kuma/Documents/Perficient/DevmountainBP/Specializations/Java_Capstone/iKnowItApp/src/main/resources/static/sections/";

                // Then we locate where we save our file and delete it.
                File deletedObj = new File(htmlPath + sectionOptional.get().getSectionHtmlName());

                // The below is just in case if for some reason deleting failed, so we can see what file it was for.
                System.out.println();
                if (deletedObj.delete()) {
                    System.out.println("Deleted section file: " + deletedObj.getName());
                } else {
                    System.out.println("Failed to delete a section file: " + deletedObj.getName());
                }
                System.out.println();
            } else {
                System.out.println("\nUnable to delete section '" + sectionOptional.get().getSectionTitle() + "' due to posts existing in said section.\n");
            }
        }
    }

    @Override
    @Transactional
    public void updateSection(SectionDto sectionDto) {
        // Searches for the section we want to update.
        Optional<Section> sectionOptional = sectionRepository.findById(sectionDto.getId());

        // If the section exists, we will update the section as below.
        // Changed the below to Intellij's format for practice.
        sectionOptional.ifPresent(section -> {
            section.setSectionTitle(sectionDto.getSectionTitle());

            // Need to update html name in our database and in the file.
            StringBuilder htmlName = new StringBuilder();
            String basePath = "C:/Users/Kuma/Documents/Perficient/DevmountainBP/Specializations/Java_Capstone/iKnowItApp/src/main/resources/static/sections/";

            // We also need to save the old file name to change it as well.
            String oldName = section.getSectionHtmlName();

            htmlName.append("section_");

            for (String s : sectionDto.getSectionTitle().toLowerCase().split(" ")) {
                htmlName.append(s.replaceAll("[^a-zA-Z0-9]", ""));
                htmlName.append("_");
            }

            htmlName.deleteCharAt(htmlName.length() - 1);
            htmlName.append(".html");

            section.setSectionHtmlName(htmlName.toString());

            File updateFile = new File(basePath + oldName);

            File renameFile = new File(basePath + htmlName);

            boolean isUpdated = updateFile.renameTo(renameFile);

            if (isUpdated) {
                System.out.println(oldName + " was changed to " + htmlName);
            } else {
                System.out.println("Renaming failed.");
            }

            sectionRepository.saveAndFlush(section);
        });
    }

    @Override
    @Transactional
    public Optional<SectionDto> findSection(Long sectionId) {
        // First we need to create an optional to search for the section to avoid nulls.
        Optional<Section> sectionOptional = sectionRepository.findById(sectionId);

        // If the section is exists, we will return the section to the user.
        // Left the code below as is and didn't use IntelliJ's functional style option.
        if (sectionOptional.isPresent()) {

            // Optional is used to avoid a null return (as below we return an empty optional).
            // SectionDto is used because we can't return a Section as it is not a data transfer object. <-- Need to remember this.
            return Optional.of(new SectionDto(sectionOptional.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public List<String> getToSection(Long sectionId) {
        List<String> response = new ArrayList<>();

        Optional<Section> sectionOptional = sectionRepository.findById(sectionId);

        if (sectionOptional.isPresent()) {
            response.add("http://localhost:8080/" + sectionOptional.get().getSectionHtmlName());
            response.add(String.valueOf(sectionOptional.get().getId()));
        } else {
            response.add("Section does not exist.");
        }

        return response;
    }

    @Override
    @Transactional
    public Optional<UserDto> getUserInfo(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);


        if (userOptional.isPresent()) {
            Optional<UserDto> optionalUserDto = Optional.of(new UserDto(userOptional.get()));

            optionalUserDto.get().setPassword(null);

            return optionalUserDto;
        } else {
            return Optional.empty();
        }

    }
}