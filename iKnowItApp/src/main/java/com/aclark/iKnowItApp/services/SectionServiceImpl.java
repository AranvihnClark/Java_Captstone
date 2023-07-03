package com.aclark.iKnowItApp.services;

import com.aclark.iKnowItApp.dtos.SectionDto;
import com.aclark.iKnowItApp.entities.Section;
import com.aclark.iKnowItApp.entities.User;
import com.aclark.iKnowItApp.repositories.SectionRepository;
import com.aclark.iKnowItApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public List<SectionDto> getAllUserSections(Long userId) {

        // We need an optional for users as we will be using their id as the identifier
        Optional<User> userOptional = userRepository.findById(userId);

        // Now to check if the user id exists.
        if (userOptional.isPresent()) {

            // A list of sections is created based on all sections of the userOptional (user) from the database.
            List<Section> sections = sectionRepository.findAllByUserEquals(userOptional.get());

            // The .stream() lets us search for all notes and the .map() converts each section found into a new SectionDto.
            // This is needed as one, we want to return a list of SectionDto and, two, we need it to be so because we don't want to use the actual sections themselves but a copy of them.
            // The .collect() is used to create an Object Collection that holds the list of SectionDto.
            List<SectionDto> sectionDtoList = sections.stream().map(section -> new SectionDto(section)).collect(Collectors.toList());

            return sectionDtoList;
        }

        // Returns an empty list if no 'Section's have been created.
        return Collections.emptyList();
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
                // First we need to remove spaces and add underscores for naming conventions.
                String[] fileNameSplit = sectionDto.getSectionTitle().toLowerCase().split(" ");

                // We create a string builder to append our naming conventions together.
                StringBuilder fileName = new StringBuilder();

                // We append the rules below in our for loop.
                for (String s : fileNameSplit) {
                    fileName.append(s).append("_");
                }

                // We remove the last underscore.
                // Probably a better way but this was what I came up with on the fly.
                fileName.deleteCharAt(fileName.length() - 1);

                // We create a directory in our project first, just in case if it is deleted somehow.
                Files.createDirectories(Paths.get("C:/Users/Kuma/Documents/Perficient/DevmountainBP/Specializations/Java_Capstone/iKnowItApp/src/main/resources/static/sections/"));

                // Then we create the actual html file in our path.
                File newSectionHtml = new File ("C:/Users/Kuma/Documents/Perficient/DevmountainBP/Specializations/Java_Capstone/iKnowItApp/src/main/resources/static/sections/" + fileName + ".html");

                // Because we are creating a new file in a try/catch statement, I only the if statement here for if the file was created.
                if (newSectionHtml.createNewFile()) {
                    System.out.println("Html file created: " + newSectionHtml.getName());
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

        // The user is already logged in and choosing the note they want to delete so we, in theory, only need the section's id.
        // This searches for the user's sections based on the section's id.
        Optional<Section> sectionOptional = sectionRepository.findById(sectionId);

        // Leaving this as below for practice.
        // If the section exists, it will be deleted.
        if (sectionOptional.isPresent()) {
            sectionRepository.delete(sectionOptional.get());

            // We need to get the file name and split up any spaces to match our naming conventions when creating files.
            String[] deletedObjFileNameSplit = sectionOptional.get().getSectionTitle().toLowerCase().split(" ");

            // We create a string builder to put the string back together.
            StringBuilder deletedObjFileName = new StringBuilder();

            // We enforce our naming convention with a loop and appends.
            for (String s : deletedObjFileNameSplit) {
                deletedObjFileName.append(s);
                deletedObjFileName.append("_");
            }

            // We remove the last underscore.
            // Probably a better way but this was what I came up with on the fly.
            deletedObjFileName.deleteCharAt(deletedObjFileName.length() - 1);

            // Then we locate where we save our file and delete it.
            File deletedObj = new File("C:/Users/Kuma/Documents/Perficient/DevmountainBP/Specializations/Java_Capstone/iKnowItApp/src/main/resources/static/sections/" + deletedObjFileName + ".html");

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
    public void updateSection(SectionDto sectionDto) {
        // Searches for the note we want to update.
        Optional<Section> sectionOptional = sectionRepository.findById(sectionDto.getId());

        // If the note exists, we will update the note as below.
        // Changed the below to Intellij's format for practice.
        sectionOptional.ifPresent(section -> {
            section.setSectionTitle(sectionDto.getSectionTitle());
            sectionRepository.saveAndFlush(section);
        });
    }

    @Override
    @Transactional
    public Optional<SectionDto> findSection(Long sectionId) {
        // First we need to create an optional to search for the note to avoid nulls.
        Optional<Section> sectionOptional = sectionRepository.findById(sectionId);

        // If the note is exists, we will return the note to the user.
        // Left the code below as is and didn't use IntelliJ's functional style option.
        if (sectionOptional.isPresent()) {

            // I did have to look at the instructions' picture for this and had to reconstruct this method.
            // I was originally going to just have the return type be just 'Note'.
            // But looking at the instructions' example, I see that we use Optional<NoteDto> for two reasons.
            // Optional is used to avoid a null return (as below we return an empty optional).
            // NoteDto is used because we can't return a Note as it is not a data transfer object. <-- Need to remember this.
            return Optional.of(new SectionDto(sectionOptional.get()));
        } else {
            return Optional.empty();
        }
    }
}
