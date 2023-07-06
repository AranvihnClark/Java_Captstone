package com.aclark.iKnowItApp.repositories;

import com.aclark.iKnowItApp.entities.Post;
import com.aclark.iKnowItApp.entities.Section;
import com.aclark.iKnowItApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Allows for JPA to search our database for all posts based on an id we provide.
    List<Post> findAllBySectionEquals(Section section);
}
