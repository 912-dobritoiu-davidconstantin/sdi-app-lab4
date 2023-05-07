package com.sdi.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sdi.app.dto.AuthorStatisticsDTO;
import com.sdi.app.model.Author;
import com.sdi.app.model.Book;
import com.sdi.app.model.User;
import com.sdi.app.repository.AuthorRepository;
import com.sdi.app.repository.BookRepository;
import com.sdi.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
public class AuthorServiceTests {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookService bookService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testGetAuthorBookCounts() {
        // create test data
        User user1 = new User("user1", "password");
        User user2 = new User("user2", "password");
        Author author1 = new Author(1L, "Author 1", "author1@example.com", "Bio 1", "Country 1", new ArrayList<>(), user1);
        Author author2 = new Author(2L, "Author 2", "author2@example.com", "Bio 2", "Country 2", new ArrayList<>(), user2);
        Book book1 = new Book(1L, "Book 1", 2021, 10.0, 4, author1, null, user1);
        Book book2 = new Book(2L, "Book 2", 2022, 20.0, 5, author1, null, user1);
        Book book3 = new Book(3L, "Book 3", 2022, 30.0, 5, author2, null, user2);
        List<Author> authors = Arrays.asList(author1, author2);
        List<Book> books = Arrays.asList(book1, book2, book3);

        // mock repository
        PageRequest pageable = PageRequest.of(0, 100);
        when(authorRepository.findAll(pageable)).thenReturn(new PageImpl<>(authors));
        when(bookRepository.countByAuthor(author1)).thenReturn(2);
        when(bookRepository.countByAuthor(author2)).thenReturn(1);

        // call service method
        AuthorService authorService = new AuthorService(authorRepository, bookRepository, bookService, userRepository, modelMapper);
        Page<AuthorStatisticsDTO> result = authorService.getAuthorBookCounts(0, 0, 100);

        // verify result
        List<AuthorStatisticsDTO> expected = Arrays.asList(
                new AuthorStatisticsDTO(1L, "Author 1", 2),
                new AuthorStatisticsDTO(2L, "Author 2", 1)
        );
        assertEquals(expected, result.getContent());
    }
}
