package com.sdi.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import com.sdi.app.dto.AuthorStatisticsDTO;
import com.sdi.app.model.Author;
import com.sdi.app.model.Book;
import com.sdi.app.repository.AuthorRepository;
import com.sdi.app.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthorServiceTests {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookService bookService;

    @Test
    public void testGetAuthorBookCounts() {
        // create test data
        Author author1 = new Author(1L, "Author 1", "author1@example.com", "Bio 1", "Country 1");
        Author author2 = new Author(2L, "Author 2", "author2@example.com", "Bio 2", "Country 2");
        Book book1 = new Book(1L, "Book 1", 2021, 10.0, 4, author1, null);
        Book book2 = new Book(2L, "Book 2", 2022, 20.0, 5, author1, null);
        Book book3 = new Book(3L, "Book 3", 2022, 30.0, 5, author2, null);
        List<Author> authors = Arrays.asList(author1, author2);
        List<Book> books = Arrays.asList(book1, book2, book3);

        // mock repository
        when(authorRepository.findAll()).thenReturn(authors);
        when(bookRepository.countByAuthor(author1)).thenReturn(2);
        when(bookRepository.countByAuthor(author2)).thenReturn(1);

        // call service method
        AuthorService authorService = new AuthorService(authorRepository, bookRepository, bookService);
        List<AuthorStatisticsDTO> result = authorService.getAuthorBookCounts();

        // verify result
        List<AuthorStatisticsDTO> expected = Arrays.asList(
                new AuthorStatisticsDTO(1L, "Author 1", 2),
                new AuthorStatisticsDTO(2L, "Author 2", 1)
        );
        assertEquals(expected, result);
    }
}
