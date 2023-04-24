package com.sdi.app.controller;

import com.sdi.app.dto.AuthorDTO;
import com.sdi.app.dto.LibraryAllDTO;
import com.sdi.app.dto.LibraryDTO;
import com.sdi.app.dto.LibraryStatisticsDTO;
import com.sdi.app.model.Library;
import com.sdi.app.service.LibraryService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/libraries")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
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

    @GetMapping("/getLibrariesTop")
    public List<LibraryStatisticsDTO> getBooksTop(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "100") int size)
    {
        return libraryService.getLibrariesWithBookCount(page, size);
    }
}