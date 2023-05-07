package com.sdi.app.controller;

import com.sdi.app.config.JwtUtils;
import com.sdi.app.dto.BookDTO;
import com.sdi.app.dto.BookWithAuthorIDDTO;
import com.sdi.app.model.Book;
import com.sdi.app.model.User;
import com.sdi.app.service.BookService;
import com.sdi.app.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:8080", "https://lively-mochi-dbc1b6.netlify.app"})
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public BookController(BookService bookService, JwtUtils jwtUtils, UserService userService) {
        this.bookService = bookService;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @GetMapping
    public Page<BookWithAuthorIDDTO> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "100") int size) {
        return bookService.getAllBooks(page, size);
    }
    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/filter/{price}")
    public List<BookWithAuthorIDDTO> filterBooks(@PathVariable double price){
        return bookService.filterByPrice(price);
    }

    @PostMapping
    public Book createBook(@RequestBody BookWithAuthorIDDTO bookDTO, @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        return bookService.createBook(bookDTO, user.getId());
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody BookWithAuthorIDDTO bookDTO, @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        return bookService.updateBook(id, bookDTO, user.getId());
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        bookService.deleteBook(id, user.getId());
    }

    @GetMapping("/count")
    public long countBooks() {
        return bookService.countBooks();
    }
}
