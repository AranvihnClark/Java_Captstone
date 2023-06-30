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

//    [EXTRA] - Not sure if needed yet.
//    private String threadPath;

    private UserDto userDto;

    public SectionDto(Section section) {
        if (section.getId() != null) {
            this.id = section.getId();
        }
        if (section.getSectionTitle() != null) {
            this.sectionTitle = section.getSectionTitle();
        }
    }
}
