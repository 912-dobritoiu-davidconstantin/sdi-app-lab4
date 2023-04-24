package com.sdi.app.controller;

import com.sdi.app.dto.AuthorDTO;
import com.sdi.app.dto.LibraryBookDTO;
import com.sdi.app.dto.LibraryBookWithNamesDTO;
import com.sdi.app.model.LibraryBook;
import com.sdi.app.service.LibraryBookService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/librarybook")
public class LibraryBookController {

    private final LibraryBookService libraryBookService;

    public LibraryBookController(LibraryBookService libraryBookService) {
        this.libraryBookService = libraryBookService;
    }

    @GetMapping
    public Page<LibraryBookWithNamesDTO> getAllLibraryBooks(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "100") int size) {
        return libraryBookService.getAllLibraryBooks(page, size);
    }
    @GetMapping("/{id}")
    public LibraryBookWithNamesDTO getLibraryBookById(@PathVariable Long id) {
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

    @GetMapping("/count")
    public long countLibraryBooks() {
        return libraryBookService.countLibraryBooks();
    }
}
