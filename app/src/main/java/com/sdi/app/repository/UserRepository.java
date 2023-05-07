package com.sdi.app.repository;

import com.sdi.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    @Query(value = "SELECT * FROM users " +
            "WHERE to_tsvector('english', username) @@ to_tsquery('english', replace(?1, ' ', ':* & ') || ':*') " +
            "OR username LIKE ('%' || ?1 || '%') ORDER BY ts_rank(to_tsvector('english', username), " +
            "to_tsquery('english', replace(?1, ' ', ':* & ') || ':*')) DESC LIMIT 20", nativeQuery = true)
    List<User> findTop20BySearchTerm(String searchTerm);

}