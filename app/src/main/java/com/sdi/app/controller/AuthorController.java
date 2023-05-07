package com.sdi.app.controller;

import com.sdi.app.config.JwtUtils;
import com.sdi.app.dto.*;
import com.sdi.app.model.Author;
import com.sdi.app.model.User;
import com.sdi.app.service.AuthorService;
import com.sdi.app.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:8080", "https://lively-mochi-dbc1b6.netlify.app"})
@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public AuthorController(AuthorService authorService, UserService userService, JwtUtils jwtUtils) {
        this.authorService = authorService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
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
    public Author createAuthor(@RequestBody AuthorDTO authorDTO, @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        return authorService.createAuthor(authorDTO, user.getId());
    }

    @PutMapping("/{id}")
    public Author updateAuthor(@PathVariable Long id, @RequestBody AuthorDTO authorDTO, @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        return authorService.updateAuthor(id, authorDTO, user.getId());
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        authorService.deleteAuthor(id, user.getId());
    }

    @GetMapping("/filterAuthorsByNumberOfBooks")
    public Page<AuthorStatisticsDTO> filterBooks(@RequestParam int count,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "100") int size) {
        return authorService.getAuthorBookCounts(count, page, size);
    }

    @GetMapping("/getAuthorsTop")
    public Page<AuthorStatisticsDTO> getAuthorsTop(
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "100") int size) {
        return authorService.getAuthorsTop(page, size);
    }

    @PostMapping("/{authorId}/books")
    public ResponseEntity<AuthorResponseDTO>addBooksToAuthor(@PathVariable Long authorId,
                                                              @RequestBody List<BookRequestDTO> bookRequestDTOs,
                                                             @RequestHeader("Authorization") String token) {
        Author author = authorService.addBooksToAuthor(authorId, bookRequestDTOs);
        AuthorResponseDTO authorResponseDTO = new AuthorResponseDTO(author);
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        return ResponseEntity.ok().body(authorResponseDTO);
    }

    @GetMapping("/count")
    public long countAuthors() {
        return authorService.countAuthors();
    }
}