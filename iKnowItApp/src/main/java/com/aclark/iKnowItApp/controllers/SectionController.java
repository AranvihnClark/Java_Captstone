package com.aclark.iKnowItApp.controllers;

import com.aclark.iKnowItApp.dtos.SectionDto;
import com.aclark.iKnowItApp.dtos.PostDto;
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

    // End-points

    @GetMapping("/user/{userId}")
    public List<SectionDto> getAllUserSections(@PathVariable Long userId) {
        return sectionService.getAllUserSections(userId);
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
}
