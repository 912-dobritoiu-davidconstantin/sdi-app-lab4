package com.sdi.app.repository;

import com.sdi.app.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsById(Long id);

    List<Author> findByUserId(Long id);
}