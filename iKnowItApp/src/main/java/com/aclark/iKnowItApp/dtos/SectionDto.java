package com.aclark.iKnowItApp.dtos;

import com.aclark.iKnowItApp.entities.Section;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionDto {

    private Long id;
    private String sectionTitle;
    private String sectionHtmlName;

    private UserDto userDto;

    // Doing this for JS since it can't use Java objects - or at least I can't figure it out lol
    private Long userId;
    private String userName;

    public SectionDto(Section section) {
        if (section.getId() != null) {
            this.id = section.getId();
            this.userId = section.getUser().getId();
            this.userName = section.getUser().getUsername();
        }
        if (section.getSectionTitle() != null) {
            this.sectionTitle = section.getSectionTitle();
        }
        if (section.getSectionHtmlName() != null) {
            this.sectionHtmlName = section.getSectionHtmlName();
        }
    }
}
