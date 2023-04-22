package com.sdi.app.service;

import com.sdi.app.dto.*;
import com.sdi.app.model.Author;
import com.sdi.app.model.Library;
import com.sdi.app.model.LibraryBook;
import com.sdi.app.repository.LibraryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LibraryService {

    @Autowired
    private final LibraryRepository libraryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public LibraryService(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    public Page<LibraryAllDTO> getAllLibraries(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Library> libraries = libraryRepository.findAll(pageRequest);
        List<LibraryAllDTO> libraryDTOs = new ArrayList<>(libraries.stream()
                .map(library -> modelMapper.map(library, LibraryAllDTO.class)).toList());
        Collections.shuffle(libraryDTOs);
        return new PageImpl<>(libraryDTOs, pageRequest, libraries.getTotalElements());
    }

    public LibraryDTO getLibraryById(Long id) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Library not found"));

        Set<LibraryBook> libraryBooks = findLibraryBooksByLibrary(id);

        LibraryDTO libraryDTO = modelMapper.map(library, LibraryDTO.class);
        Set<LibrariesBookDTO> libraryBookDTOs = libraryBooks.stream()
                .map(libraryBook -> modelMapper.map(libraryBook, LibrariesBookDTO.class))
                .collect(Collectors.toSet());

        libraryDTO.setBooks(libraryBookDTOs);
        return libraryDTO;
    }

    public Library createLibrary(LibraryDTO libraryDTO) {
        Library library = new Library(null, libraryDTO.getName(), libraryDTO.getDescription(), libraryDTO.getLocation(), libraryDTO.getRating(), libraryDTO.getOwner(), new HashSet<>());
        return libraryRepository.save(library);
    }

    public Library updateLibrary(Long id, LibraryDTO libraryDTO) {
        Library library = libraryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Library not found"));
        library.setName(libraryDTO.getName());
        library.setDescription(libraryDTO.getDescription());
        library.setLocation(libraryDTO.getLocation());
        library.setRating(libraryDTO.getRating());
        library.setOwner(libraryDTO.getOwner());
        return libraryRepository.save(library);
    }

    public void deleteLibrary(Long id) {
        Library library = libraryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Library not found"));
        libraryRepository.delete(library);
    }

    public Set<LibraryBook> findLibraryBooksByLibrary(Long libraryId) {
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new EntityNotFoundException("Library with id " + libraryId + " not found"));
        assert false;
        return library.getBooks();
    }

    public List<LibraryStatisticsDTO> getLibrariesWithBookCount() {
        List<Library> libraries = libraryRepository.findAll();
        List<LibraryStatisticsDTO> libraryDTOs = new ArrayList<>();
        for (Library library : libraries) {
            int bookCount = library.getBooks().size();
            LibraryStatisticsDTO libraryDTO = new LibraryStatisticsDTO(library.getId(), bookCount);
            libraryDTOs.add(libraryDTO);
        }

        libraryDTOs.sort((dto1, dto2) -> Integer.compare(dto2.getBooksCount(), dto1.getBooksCount()));

        return libraryDTOs;
    }
}
