package com.aclark.iKnowItApp.controllers;

import com.aclark.iKnowItApp.dtos.SectionDto;
import com.aclark.iKnowItApp.dtos.UserDto;
import com.aclark.iKnowItApp.services.PostService;
import com.aclark.iKnowItApp.services.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/sections")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @Autowired
    private PostService postService;
    // End-points

    @GetMapping("")
    public List<SectionDto> getAllSections() {
        return sectionService.getAllSections();
    }

    @PostMapping("user/{userId}")
    public void addSection(@RequestBody SectionDto sectionDto, @PathVariable Long userId) {
        sectionService.addSection(sectionDto, userId);
    }

    @DeleteMapping("/{sectionId}")
    public void deleteSection(@PathVariable Long sectionId) {
        sectionService.deleteSection(sectionId);
    }

    @PutMapping
    public void updateSection(@RequestBody SectionDto sectionDto) {
        sectionService.updateSection(sectionDto);
    }

    @GetMapping("/{sectionId}")
    public Optional<SectionDto> findSection(@PathVariable Long sectionId) {
        return sectionService.findSection(sectionId);
    }
    @PostMapping ("/{sectionId}")
    public List<String> getToSection(@PathVariable Long sectionId) {
        return sectionService.getToSection(sectionId);
    }

    @GetMapping("user/{userId}")
    public Optional<UserDto> getUserInfo(@PathVariable Long userId) {
        return sectionService.getUserInfo(userId);
    }

}
