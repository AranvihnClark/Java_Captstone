package com.aclark.iKnowItApp.repositories;

import com.aclark.iKnowItApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Supposedly this will let JPA run an SQL query based on 'username'.
    Optional<User> findByUsername(String username);
}
