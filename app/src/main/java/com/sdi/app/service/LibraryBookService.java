package com.sdi.app.service;

import com.sdi.app.dto.LibraryBookDTO;
import com.sdi.app.model.Book;
import com.sdi.app.model.Library;
import com.sdi.app.model.LibraryBook;
import com.sdi.app.repository.BookRepository;
import com.sdi.app.repository.LibraryBookRepository;
import com.sdi.app.repository.LibraryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryBookService {

    @Autowired
    private final LibraryBookRepository libraryBookRepository;

    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final LibraryRepository libraryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public LibraryBookService(LibraryBookRepository libraryBookRepository, BookRepository bookRepository, LibraryRepository libraryRepository, ModelMapper modelMapper) {
        this.libraryBookRepository = libraryBookRepository;
        this.bookRepository = bookRepository;
        this.libraryRepository = libraryRepository;
        this.modelMapper = modelMapper;
    }

    public List<LibraryBookDTO> getAllLibraryBooks() {
        List<LibraryBook> libraryBooks = libraryBookRepository.findAll();
        return libraryBooks.stream()
                .map(libraryBook -> modelMapper.map(libraryBook, LibraryBookDTO.class))
                .collect(Collectors.toList());
    }

    public LibraryBookDTO getLibraryBookById(Long id) {
        LibraryBook libraryBook = libraryBookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Library book not found"));
        return modelMapper.map(libraryBook, LibraryBookDTO.class);
    }

    public LibraryBook createLibraryBook(LibraryBookDTO libraryBookIDDTO) {
        Book book = bookRepository.findById(libraryBookIDDTO.getBookID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found"));
        Library library = libraryRepository.findById(libraryBookIDDTO.getLibraryID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Library not found"));
        LibraryBook libraryBook = new LibraryBook(null, book, library, libraryBookIDDTO.getBorrowDate(), libraryBookIDDTO.getReturnDate());
        return libraryBookRepository.save(libraryBook);
    }

    public LibraryBook updateLibraryBook(Long id, LibraryBookDTO libraryBookIDDTO) {
        LibraryBook libraryBook = libraryBookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Library book not found"));
        Book book = bookRepository.findById(libraryBookIDDTO.getBookID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found"));
        Library library = libraryRepository.findById(libraryBookIDDTO.getLibraryID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Library not found"));
        libraryBook.setBook(book);
        libraryBook.setLibrary(library);
        libraryBook.setBorrowDate(libraryBookIDDTO.getBorrowDate());
        libraryBook.setReturnDate(libraryBookIDDTO.getReturnDate());
        return libraryBookRepository.save(libraryBook);
    }

    public void deleteLibraryBook(Long id) {
        LibraryBook libraryBook = libraryBookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Library book not found"));
        libraryBookRepository.delete(libraryBook);
    }

}
