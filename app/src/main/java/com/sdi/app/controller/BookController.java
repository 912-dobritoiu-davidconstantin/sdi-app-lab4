package com.sdi.app.controller;

import com.sdi.app.dto.AuthorDTO;
import com.sdi.app.dto.BookDTO;
import com.sdi.app.dto.BookWithAuthorIDDTO;
import com.sdi.app.model.Book;
import com.sdi.app.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
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
    public Book createBook(@RequestBody BookWithAuthorIDDTO bookDTO) {
        return bookService.createBook(bookDTO);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody BookWithAuthorIDDTO bookDTO) {
        return bookService.updateBook(id, bookDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}
