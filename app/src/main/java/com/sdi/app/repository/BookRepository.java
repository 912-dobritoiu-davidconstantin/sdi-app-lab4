package com.sdi.app.repository;

import com.sdi.app.model.Author;
import com.sdi.app.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    int countByAuthor(Author author);
}
