package com.aclark.iKnowItApp.dtos;

import com.aclark.iKnowItApp.entities.Section;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionDto {

    private Long id;
    private String sectionTitle;
    private String sectionHtmlName;

    private UserDto userDto;
    private Set<PostDto> postDtoSet = new HashSet<>();

    public SectionDto(Section section) {
        if (section.getId() != null) {
            this.id = section.getId();
        }
        if (section.getSectionTitle() != null) {
            this.sectionTitle = section.getSectionTitle();
        }
        if (section.getSectionHtmlName() != null) {
            this.sectionHtmlName = section.getSectionHtmlName();
        }
        if (section.getUser() != null) {
            this.userDto = new UserDto(section.getUser());
            this.userDto.setPassword(null);
        }
    }
}
