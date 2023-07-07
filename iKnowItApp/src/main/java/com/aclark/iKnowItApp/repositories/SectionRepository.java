package com.aclark.iKnowItApp.repositories;

import com.aclark.iKnowItApp.entities.Section;
import com.aclark.iKnowItApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    // Allows for JPA to search our database for all sections based on an id we provide.
    List<Section> findAllByUserEquals(User user);
}
