package com.aclark.iKnowItApp.repositories;

import com.aclark.iKnowItApp.entities.Comment;
import com.aclark.iKnowItApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Allows for JPA to search our database for all comments based on an id we provide.
    List<Comment> findAllByUserEquals(User user);
}
