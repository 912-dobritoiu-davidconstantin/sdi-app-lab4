package com.sdi.app.repository;

import com.sdi.app.model.Author;
import com.sdi.app.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    int countByAuthor(Author author);
    List<Book> findByUserId(Long id);
}
