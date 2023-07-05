package com.aclark.iKnowItApp.entities;

import com.aclark.iKnowItApp.dtos.SectionDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Sections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long id;

    @Column
    private String sectionTitle;

    @Column
    private String sectionHtmlName;

    // creates 'user_id' column in Sections table.
    @ManyToOne
    @JsonBackReference // Prevents infinite recursion when delivering resource as Json through RESTful API endpoint.
    private User user;

    public Section(SectionDto sectionDto) {
        if (sectionDto.getSectionTitle() != null) {
            this.sectionTitle = sectionDto.getSectionTitle();
        }
        if (sectionDto.getSectionHtmlPath() != null) {
            this.sectionHtmlName = sectionDto.getSectionHtmlPath();
        }
    }

//    [EXTRA] - maybe. Not sure if this is needed yet or not.
//    @Column
//    private String threadPath;
}
