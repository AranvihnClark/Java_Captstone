package com.aclark.iKnowItApp.repositories;

import com.aclark.iKnowItApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // This will let JPA run an SQL query based on the 'username' database table.
    Optional<User> findByUsername(String username);

    // This will let JPA run an SQL query based on the 'nickname' database table.
    Optional<User> findByNickname(String nickname);

    // This will let JPA run an SQL query based on the 'emailAddress' database table.
    Optional<User> findByEmailAddress(String emailAddress);
}
