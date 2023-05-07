package com.sdi.app.controller;

import com.sdi.app.config.JwtUtils;
import com.sdi.app.dto.LibraryAllDTO;
import com.sdi.app.dto.LibraryDTO;
import com.sdi.app.dto.LibraryStatisticsDTO;
import com.sdi.app.model.Library;
import com.sdi.app.model.User;
import com.sdi.app.service.LibraryService;
import com.sdi.app.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:8080", "https://lively-mochi-dbc1b6.netlify.app"})
@RestController
@RequestMapping("/api/libraries")
public class LibraryController {

    private final LibraryService libraryService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public LibraryController(LibraryService libraryService, JwtUtils jwtUtils, UserService userService) {
        this.libraryService = libraryService;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @GetMapping
    public Page<LibraryAllDTO> getAllLibraries(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "100") int size) {
        return libraryService.getAllLibraries(page, size);
    }
    @GetMapping("/{id}")
    public LibraryDTO getLibraryById(@PathVariable Long id) {
        return libraryService.getLibraryById(id);
    }

    @PostMapping
    public Library createLibrary(@RequestBody LibraryDTO libraryDTO, @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        return libraryService.createLibrary(libraryDTO, user.getId());
    }

    @PutMapping("/{id}")
    public Library updateLibrary(@PathVariable Long id, @RequestBody LibraryDTO libraryDTO, @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        return libraryService.updateLibrary(id, libraryDTO, user.getId());
    }

    @DeleteMapping("/{id}")
    public void deleteLibrary(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        libraryService.deleteLibrary(id, user.getId());
    }

    @GetMapping("/getLibrariesTop")
    public Page<LibraryStatisticsDTO> getBooksTop(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "100") int size)
    {
        return libraryService.getLibrariesWithBookCount(page, size);
    }

    @GetMapping("/count")
    public long countLibraries() {
        return libraryService.countLibraries();
    }
}