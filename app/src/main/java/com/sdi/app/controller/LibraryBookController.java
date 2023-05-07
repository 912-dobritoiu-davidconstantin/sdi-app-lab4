package com.sdi.app.controller;

import com.sdi.app.config.JwtUtils;
import com.sdi.app.dto.LibraryBookDTO;
import com.sdi.app.dto.LibraryBookWithNamesDTO;
import com.sdi.app.model.LibraryBook;
import com.sdi.app.model.User;
import com.sdi.app.service.LibraryBookService;
import com.sdi.app.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:8080", "https://lively-mochi-dbc1b6.netlify.app"})
@RestController
@RequestMapping("/api/librarybook")
public class LibraryBookController {

    private final LibraryBookService libraryBookService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public LibraryBookController(LibraryBookService libraryBookService, JwtUtils jwtUtils, UserService userService) {
        this.libraryBookService = libraryBookService;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
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
    public LibraryBook createLibraryBook(@RequestBody LibraryBookDTO libraryBookIDDTO, @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        return libraryBookService.createLibraryBook(libraryBookIDDTO, user.getId());
    }

    @PutMapping("/{id}")
    public LibraryBook updateLibraryBook(@PathVariable Long id, @RequestBody LibraryBookDTO libraryBookIDDTO, @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        return libraryBookService.updateLibraryBook(id, libraryBookIDDTO, user.getId());
    }

    @DeleteMapping("/{id}")
    public void deleteLibraryBook(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        libraryBookService.deleteLibraryBook(id, user.getId());
    }

    @GetMapping("/count")
    public long countLibraryBooks() {
        return libraryBookService.countLibraryBooks();
    }
}
