package com.aclark.iKnowItApp.services;

import com.aclark.iKnowItApp.dtos.SectionDto;
import com.aclark.iKnowItApp.entities.Section;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SectionService {
    @Transactional
    List<SectionDto> getAllSections();

    @Transactional
    void addSection(SectionDto sectionDto, Long userId);

    @Transactional
    void deleteSection(Long sectionId);

    @Transactional
    void updateSection(SectionDto sectionDto);

    @Transactional
    Optional<SectionDto> findSection(Long sectionId);

    @Transactional
    List<String> getToSection(Long sectionId);
}
