package com.sdi.app.controller;

import com.sdi.app.dto.LibraryDTO;
import com.sdi.app.dto.LibraryStatisticsDTO;
import com.sdi.app.model.Library;
import com.sdi.app.service.LibraryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/libraries")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public List<LibraryDTO> getAllLibraries() {
        return libraryService.getAllLibraries();
    }

    @GetMapping("/{id}")
    public LibraryDTO getLibraryById(@PathVariable Long id) {
        return libraryService.getLibraryById(id);
    }

    @PostMapping
    public Library createLibrary(@RequestBody LibraryDTO libraryDTO) {
        return libraryService.createLibrary(libraryDTO);
    }

    @PutMapping("/{id}")
    public Library updateLibrary(@PathVariable Long id, @RequestBody LibraryDTO libraryDTO) {
        return libraryService.updateLibrary(id, libraryDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteLibrary(@PathVariable Long id) {
        libraryService.deleteLibrary(id);
    }

    @GetMapping("/topBooks")
    public List<LibraryStatisticsDTO> getBooksTop()
    {
        return libraryService.getLibrariesWithBookCount();
    }
}