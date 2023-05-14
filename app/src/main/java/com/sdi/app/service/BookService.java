package com.sdi.app.service;

import com.sdi.app.dto.*;
import com.sdi.app.exception.UserNotAuthorizedException;
import com.sdi.app.exception.UserNotFoundException;
import com.sdi.app.model.*;
import com.sdi.app.repository.AuthorRepository;
import com.sdi.app.repository.BookRepository;
import com.sdi.app.repository.LibraryBookRepository;
import com.sdi.app.repository.UserRepository;
import javax.persistence.EntityNotFoundException;
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
public class BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final LibraryBookRepository libraryBookRepository;

    private final UserRepository userRepository;

    private ModelMapper modelMapper;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, LibraryBookRepository libraryBookRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.libraryBookRepository = libraryBookRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public Page<BookWithAuthorIDDTO> getAllBooks(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Book> books = bookRepository.findAll(pageRequest);
        List<BookWithAuthorIDDTO> bookDTOs = new ArrayList<>(books.stream()
                .map(book -> modelMapper.map(book, BookWithAuthorIDDTO.class)).toList());
        return new PageImpl<>(bookDTOs, pageRequest, books.getTotalElements());
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found"));
        Author author = authorRepository.findById(book.getAuthor().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author not found"));
        Set<LibraryBook> libraryBooks = findLibraryBooksByBookId(id);


        BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
        AuthorDTO authorDTO = modelMapper.map(author, AuthorDTO.class);
        Set<LibraryBooksDTO> libraryBookDTOs = libraryBooks.stream()
                .map(libraryBook -> modelMapper.map(libraryBook, LibraryBooksDTO.class))
                .collect(Collectors.toSet());

        bookDTO.setAuthor(authorDTO);
        bookDTO.setLibraries(libraryBookDTOs);

        return bookDTO;
    }


    public Book createBook(BookWithAuthorIDDTO bookDTO, Long userID) {
        Author author = authorRepository.findById(bookDTO.getAuthorId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author not found"));
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));

        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        if (!userOrModOrAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        Book book = new Book(null, bookDTO.getTitle(), bookDTO.getYear(), bookDTO.getPrice(), bookDTO.getRating(), author, new HashSet<>(), user);
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, BookWithAuthorIDDTO bookDTO, Long userID) {
        Author author = authorRepository.findById(bookDTO.getAuthorId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author not found"));
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));
        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        if (!userOrModOrAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        book.setTitle(bookDTO.getTitle());
        book.setYear(bookDTO.getYear());
        book.setPrice(bookDTO.getPrice());
        book.setRating(bookDTO.getRating());
        book.setAuthor(author);
        book.setUser(user);
        return bookRepository.save(book);
    }

    public void deleteBook(Long id, Long userID) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));

        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        if (!userOrModOrAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }

        bookRepository.delete(book);
    }

    public List<BookWithAuthorIDDTO> filterByPrice(double price)
    {
        List<Book> books = bookRepository.findAll(Sort.by(Sort.Direction.DESC, "price"));
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getPrice() > price)
                result.add(book);
        }
        return result.stream()
                .map(book -> modelMapper.map(book, BookWithAuthorIDDTO.class))
                .collect(Collectors.toList());
    }

    public Set<LibraryBook> findLibraryBooksByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + bookId + " not found"));
        assert false;
        return book.getLibraries();
    }

    public long countBooks() {
        return bookRepository.count();
    }

}
