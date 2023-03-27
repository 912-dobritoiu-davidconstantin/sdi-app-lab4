package com.sdi.app.service;

import com.sdi.app.dto.*;
import com.sdi.app.model.Author;
import com.sdi.app.model.Book;
import com.sdi.app.model.LibraryBook;
import com.sdi.app.repository.AuthorRepository;
import com.sdi.app.repository.BookRepository;
import com.sdi.app.repository.LibraryBookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final AuthorRepository authorRepository;

    @Autowired
    private final LibraryBookRepository libraryBookRepository;

    @Autowired
    private ModelMapper modelMapper;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, LibraryBookRepository libraryBookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.libraryBookRepository = libraryBookRepository;
        this.modelMapper = modelMapper;
    }

    public List<BookWithAuthorIDDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(book -> modelMapper.map(book, BookWithAuthorIDDTO.class))
                .collect(Collectors.toList());
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


    public Book createBook(BookWithAuthorIDDTO bookDTO) {
        Author author = authorRepository.findById(bookDTO.getAuthorId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author not found"));
        Book book = new Book(null, bookDTO.getTitle(), bookDTO.getYear(), bookDTO.getPrice(), bookDTO.getRating(), author, new HashSet<>());
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, BookWithAuthorIDDTO bookDTO) {
        Author author = authorRepository.findById(bookDTO.getAuthorId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author not found"));
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        book.setTitle(bookDTO.getTitle());
        book.setYear(bookDTO.getYear());
        book.setPrice(bookDTO.getPrice());
        book.setRating(bookDTO.getRating());
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
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



}
