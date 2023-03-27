package com.sdi.app.controller;

import com.sdi.app.dto.BookDTO;
import com.sdi.app.dto.BookWithAuthorIDDTO;
import com.sdi.app.model.Book;
import com.sdi.app.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookWithAuthorIDDTO> getAllBooks() {
        return bookService.getAllBooks();
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
