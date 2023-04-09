package com.sdi.app.controller;

import com.sdi.app.dto.*;
import com.sdi.app.model.Author;
import com.sdi.app.model.Book;
import com.sdi.app.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public Page<AuthorDTO> getAllAuthors(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "100") int size) {
        return authorService.getAllAuthors(page, size);
    }

    @GetMapping("/{id}")
    public AuthorWithBookDTO getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @PostMapping
    public Author createAuthor(@RequestBody AuthorDTO authorDTO) {
        return authorService.createAuthor(authorDTO);
    }

    @PutMapping("/{id}")
    public Author updateAuthor(@PathVariable Long id, @RequestBody AuthorDTO authorDTO) {
        return authorService.updateAuthor(id, authorDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }

    @GetMapping("/filterAuthorsByNumberOfBooks")
    public List<AuthorStatisticsDTO> getBooksTop() { return authorService.getAuthorBookCounts(); }

    @PostMapping("/{authorId}/books")
    public ResponseEntity<AuthorResponseDTO>addBooksToAuthor(@PathVariable Long authorId,
                                                              @RequestBody List<BookRequestDTO> bookRequestDTOs) {
        Author author = authorService.addBooksToAuthor(authorId, bookRequestDTOs);
        AuthorResponseDTO authorResponseDTO = new AuthorResponseDTO(author);
        return ResponseEntity.ok().body(authorResponseDTO);
    }

    @GetMapping("/count")
    public Long countAuthors() {
        return authorService.countAuthors();
    }
}