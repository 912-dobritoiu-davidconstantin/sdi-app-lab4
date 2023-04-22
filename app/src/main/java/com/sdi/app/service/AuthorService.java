package com.sdi.app.service;

import com.sdi.app.dto.*;
import com.sdi.app.model.Author;
import com.sdi.app.model.Book;
import com.sdi.app.model.LibraryBook;
import com.sdi.app.repository.AuthorRepository;
import com.sdi.app.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Pageable;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    @Autowired
    private final AuthorRepository authorRepository;

    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final BookService bookService;
    @Autowired
    private ModelMapper modelMapper;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository, BookService bookService) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    public Page<AuthorDTO> getAllAuthors(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Author> authors = authorRepository.findAll(pageRequest);
        List<AuthorDTO> authorDTOs = authors.stream()
                .map(author -> modelMapper.map(author, AuthorDTO.class))
                .collect(Collectors.toList());
        Collections.shuffle(authorDTOs);
        return new PageImpl<>(authorDTOs, pageRequest, authors.getTotalElements());
    }

    public AuthorWithBookDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author not found"));
        List<Book> books = author.getBooks();
        List<BookForAuthorDTO> addBooks = new ArrayList<>();
        for (Book book : books) {
            BookForAuthorDTO bookDTO = modelMapper.map(book, BookForAuthorDTO.class);

            Set<LibraryBook> libraryBooks = bookService.findLibraryBooksByBookId(bookDTO.getId());
            Set<LibrariesBookDTO> libraryBookDTOs = libraryBooks.stream()
                    .map(libraryBook -> modelMapper.map(libraryBook, LibrariesBookDTO.class))
                    .collect(Collectors.toSet());

            bookDTO.setLibraries(libraryBookDTOs);
            addBooks.add(bookDTO);
        }

        AuthorWithBookDTO authorDTO = modelMapper.map(author, AuthorWithBookDTO.class);
        authorDTO.setBooks(addBooks);

        return authorDTO;
    }

    public Author createAuthor(AuthorDTO authorDTO) {
        Author author = new Author(null, authorDTO.getName(), authorDTO.getEmail(), authorDTO.getBio(), authorDTO.getCountry(), new ArrayList<>());
        return authorRepository.save(author);
    }

    public Author updateAuthor(Long id, AuthorDTO authorDTO) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
        author.setName(authorDTO.getName());
        author.setEmail(authorDTO.getEmail());
        author.setBio(authorDTO.getBio());
        author.setCountry(authorDTO.getCountry());
        author.setBooks(author.getBooks());
        return authorRepository.save(author);
    }

    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
        authorRepository.delete(author);
    }

    public List<AuthorStatisticsDTO> getAuthorBookCounts(int books, int pageNumber, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by("booksCount").descending());
        List<Author> authors = authorRepository.findAll(pageable).getContent();
        List<AuthorStatisticsDTO> authorBookCountDTOs = new ArrayList<>();
        for (Author author : authors) {
            int bookCount = bookRepository.countByAuthor(author);
            if (bookCount >= books) {
                AuthorStatisticsDTO authorBookCountDTO = new AuthorStatisticsDTO(author.getId(), author.getName(), bookCount);
                authorBookCountDTOs.add(authorBookCountDTO);
            }
        }
        return authorBookCountDTOs;
    }


    public Author addBooksToAuthor(Long authorId, List<BookRequestDTO> bookRequestDTOs) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));

        List<Book> books = new ArrayList<>();
        for (BookRequestDTO bookRequestDTO : bookRequestDTOs) {
            Book book = new Book();
            book.setTitle(bookRequestDTO.getTitle());
            book.setYear(bookRequestDTO.getYear());
            book.setPrice(bookRequestDTO.getPrice());
            book.setRating(bookRequestDTO.getRating());
            book.setAuthor(author);
            books.add(book);
        }
        author.getBooks().addAll(books);

        return authorRepository.save(author);
    }

    public long countAuthors() {
        return authorRepository.count();
    }
}
