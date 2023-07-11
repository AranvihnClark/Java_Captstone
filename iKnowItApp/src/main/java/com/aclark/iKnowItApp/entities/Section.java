package com.aclark.iKnowItApp.entities;

import com.aclark.iKnowItApp.dtos.SectionDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.HashSet;
import java.util.Set;

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
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // Prevents infinite recursion when delivering resource as Json through RESTful API endpoint.
    private User user;

    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    // Prevents infinite recursion
    @JsonBackReference
    private Set<Post> postSet = new HashSet<>();

    public Section(SectionDto sectionDto) {
        if (sectionDto.getSectionTitle() != null) {
            this.sectionTitle = sectionDto.getSectionTitle();
        }
        if (sectionDto.getSectionHtmlName() != null) {
            this.sectionHtmlName = sectionDto.getSectionHtmlName();
        }
    }

//    [EXTRA] - maybe. Not sure if this is needed yet or not.
//    @Column
//    private String threadPath;
}
