package com.sdi.app.repository;

import com.sdi.app.model.Author;
import com.sdi.app.model.LibraryBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryBookRepository extends JpaRepository<LibraryBook, Long> {
    List<LibraryBook> findByUserId(Long id);
}
