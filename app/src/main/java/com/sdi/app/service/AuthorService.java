package com.sdi.app.service;

import com.sdi.app.dto.*;
import com.sdi.app.exception.UserNotAuthorizedException;
import com.sdi.app.exception.UserNotFoundException;
import com.sdi.app.model.*;
import com.sdi.app.repository.AuthorRepository;
import com.sdi.app.repository.BookRepository;
import com.sdi.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    private final BookService bookService;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository, BookService bookService, UserRepository userRepository, ModelMapper modelMapper) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public Page<AuthorDTO> getAllAuthors(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Author> authors = authorRepository.findAll(pageRequest);
        List<AuthorDTO> authorDTOs = authors.stream()
                .map(author -> {
                    AuthorDTO authorDTO = modelMapper.map(author, AuthorDTO.class);
                    int numBooks = author.getBooks().size();
                    authorDTO.setBooksCount(numBooks);
                    return authorDTO;
                })
                .collect(Collectors.toList());
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

    public Author createAuthor(AuthorDTO authorDTO, Long userID) {
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));
        Author author = new Author(null, authorDTO.getName(), authorDTO.getEmail(), authorDTO.getBio(), authorDTO.getCountry(), new ArrayList<>(), user);
        authorDTO.setUsername(user.getUsername());

        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        if (!userOrModOrAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }

        return authorRepository.save(author);
    }

    public Author updateAuthor(Long id, AuthorDTO authorDTO, Long userID) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));
        boolean isUser = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_USER
        );
        if (!isUser) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }

        if (!Objects.equals(user.getId(), author.getUser().getId())) {
            boolean modOrAdmin = user.getRoles().stream().anyMatch((role) ->
                    role.getName() == ERole.ROLE_ADMIN || role.getName() == ERole.ROLE_MODERATOR
            );

            if (!modOrAdmin) {
                throw new UserNotAuthorizedException(String.format(user.getUsername()));
            }
        }

        author.setName(authorDTO.getName());
        author.setEmail(authorDTO.getEmail());
        author.setBio(authorDTO.getBio());
        author.setCountry(authorDTO.getCountry());
        author.setBooks(author.getBooks());
        author.setUser(user);
        return authorRepository.save(author);
    }

    public void deleteAuthor(Long id, Long userID) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));

        boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
        );
        if (!isAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }

        authorRepository.delete(author);
    }

    public Page<AuthorStatisticsDTO> getAuthorBookCounts(int books, int pageNumber, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        Page<Author> authorsPage = authorRepository.findAll(pageable);
        List<Author> authors = authorsPage.getContent();
        List<AuthorStatisticsDTO> authorBookCountDTOs = new ArrayList<>();
        for (Author author : authors) {
            int bookCount = bookRepository.countByAuthor(author);
            if (bookCount >= books) {
                AuthorStatisticsDTO authorBookCountDTO = new AuthorStatisticsDTO(author.getId(), author.getName(), bookCount);
                authorBookCountDTOs.add(authorBookCountDTO);
            }
        }
        return new PageImpl<>(authorBookCountDTOs, pageable, authorsPage.getTotalElements());
    }

    public Page<AuthorStatisticsDTO> getAuthorsTop(int pageNumber, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "books");
        Page<Author> authorsPage = authorRepository.findAll(pageable);
        List<Author> authors = authorsPage.getContent();
        List<AuthorStatisticsDTO> authorBookCountDTOs = new ArrayList<>();
        for (Author author : authors) {
            int bookCount = author.getBooks().size();
            AuthorStatisticsDTO authorBookCountDTO = new AuthorStatisticsDTO(author.getId(), author.getName(), bookCount);
            authorBookCountDTOs.add(authorBookCountDTO);
        }
        return new PageImpl<>(authorBookCountDTOs, pageable, authorsPage.getTotalElements());
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
