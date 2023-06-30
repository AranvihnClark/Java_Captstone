package com.aclark.iKnowItApp.services;

import com.aclark.iKnowItApp.dtos.SectionDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SectionService {
    @Transactional
    List<SectionDto> getAllUserSections(Long userId);

    @Transactional
    void addSection(SectionDto sectionDto, Long userId);

    @Transactional
    void deleteSection(Long sectionId);

    @Transactional
    void updateSection(SectionDto sectionDto);

    @Transactional
    Optional<SectionDto> findSection(Long sectionId);
}
