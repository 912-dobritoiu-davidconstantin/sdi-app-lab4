package com.sdi.app.repository;

import com.sdi.app.dto.AuthorDTO;
import com.sdi.app.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}