package com.sdi.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.sdi.app.dto.LibraryStatisticsDTO;
import com.sdi.app.model.Book;
import com.sdi.app.model.Library;
import com.sdi.app.model.LibraryBook;
import com.sdi.app.repository.LibraryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LibraryServiceTests {

    @Mock
    private LibraryRepository libraryRepository;

    @Test
    public void testGetLibrariesWithBookCount() {
        // create test data
        Library library1 = new Library(1L, "Library 1", "Description 1", "Location 1", 3, "Owner 1", new HashSet<>());
        Library library2 = new Library(2L, "Library 2", "Description 2", "Location 2", 5, "Owner 2", new HashSet<>());
        Book book1 = new Book(1L, "Book 1", 2021, 10.0, 4, null, new HashSet<>());
        Book book2 = new Book(2L, "Book 2", 2022, 20.0, 5, null, new HashSet<>());
        LibraryBook libraryBook1 = new LibraryBook(1L, book1, library1, null, null);
        LibraryBook libraryBook2 = new LibraryBook(2L, book2, library2, null, null);
        library1.getBooks().add(libraryBook1);
        library2.getBooks().add(libraryBook2);
        List<Library> libraries = Arrays.asList(library1, library2);

        // mock repository
        when(libraryRepository.findAll()).thenReturn(libraries);

        // call service method
        LibraryService libraryService = new LibraryService(libraryRepository);
        List<LibraryStatisticsDTO> result = libraryService.getLibrariesWithBookCount();

        // verify result
        List<LibraryStatisticsDTO> expected = Arrays.asList(
                new LibraryStatisticsDTO(1L, 1), // library1 has 1 book
                new LibraryStatisticsDTO(2L, 1)  // library2 has 1 book
        );
        assertEquals(expected, result);
    }
}
