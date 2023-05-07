package com.sdi.app.repository;

import com.sdi.app.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LibraryRepository extends JpaRepository<Library, Long> {
    List<Library> findByUserId(Long id);
}
