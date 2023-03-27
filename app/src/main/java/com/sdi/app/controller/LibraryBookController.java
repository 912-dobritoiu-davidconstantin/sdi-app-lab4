package com.sdi.app.controller;

import com.sdi.app.dto.LibraryBookDTO;
import com.sdi.app.model.LibraryBook;
import com.sdi.app.service.LibraryBookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library-book")
public class LibraryBookController {

    private final LibraryBookService libraryBookService;

    public LibraryBookController(LibraryBookService libraryBookService) {
        this.libraryBookService = libraryBookService;
    }

    @GetMapping
    public List<LibraryBookDTO> getAllLibraryBooks() {
        return libraryBookService.getAllLibraryBooks();
    }

    @GetMapping("/{id}")
    public LibraryBookDTO getLibraryBookById(@PathVariable Long id) {
        return libraryBookService.getLibraryBookById(id);
    }

    @PostMapping
    public LibraryBook createLibraryBook(@RequestBody LibraryBookDTO libraryBookIDDTO) {
        return libraryBookService.createLibraryBook(libraryBookIDDTO);
    }

    @PutMapping("/{id}")
    public LibraryBook updateLibraryBook(@PathVariable Long id, @RequestBody LibraryBookDTO libraryBookIDDTO) {
        return libraryBookService.updateLibraryBook(id, libraryBookIDDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteLibraryBook(@PathVariable Long id) {
        libraryBookService.deleteLibraryBook(id);
    }
}
