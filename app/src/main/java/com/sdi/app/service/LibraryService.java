package com.sdi.app.service;

import com.sdi.app.dto.*;
import com.sdi.app.exception.UserNotAuthorizedException;
import com.sdi.app.exception.UserNotFoundException;
import com.sdi.app.model.ERole;
import com.sdi.app.model.Library;
import com.sdi.app.model.LibraryBook;
import com.sdi.app.model.User;
import com.sdi.app.repository.LibraryRepository;
import com.sdi.app.repository.UserRepository;
import javax.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
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

    private final LibraryRepository libraryRepository;

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    public LibraryService(LibraryRepository libraryRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.libraryRepository = libraryRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public Page<LibraryAllDTO> getAllLibraries(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Library> libraries = libraryRepository.findAll(pageRequest);
        List<LibraryAllDTO> libraryDTOs = libraries.stream()
                .map(library -> {
                    LibraryAllDTO libraryDTO = modelMapper.map(library, LibraryAllDTO.class);
                    int numBooks = library.getBooks().size();
                    libraryDTO.setBooksCount(numBooks);
                    return libraryDTO;
                })
                .collect(Collectors.toList());
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

    public Library createLibrary(LibraryDTO libraryDTO, Long userID) {
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));
        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        if (!userOrModOrAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        Library library = new Library(null, libraryDTO.getName(), libraryDTO.getDescription(), libraryDTO.getLocation(), libraryDTO.getRating(), libraryDTO.getOwner(), new HashSet<>(), user);
        return libraryRepository.save(library);
    }

    public Library updateLibrary(Long id, LibraryDTO libraryDTO, Long userID) {
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));
        Library library = libraryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Library not found"));
        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        if (!userOrModOrAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        library.setName(libraryDTO.getName());
        library.setDescription(libraryDTO.getDescription());
        library.setLocation(libraryDTO.getLocation());
        library.setRating(libraryDTO.getRating());
        library.setOwner(libraryDTO.getOwner());
        return libraryRepository.save(library);
    }

    public void deleteLibrary(Long id, Long userID) {
        Library library = libraryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Library not found"));
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));
        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        if (!userOrModOrAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        libraryRepository.delete(library);
    }

    public Set<LibraryBook> findLibraryBooksByLibrary(Long libraryId) {
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new EntityNotFoundException("Library with id " + libraryId + " not found"));
        assert false;
        return library.getBooks();
    }

    public Page<LibraryStatisticsDTO> getLibrariesWithBookCount(int pageNumber, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        Page<Library> libraryPage = libraryRepository.findAll(pageable);
        List<Library> libraries = libraryPage.getContent();
        List<LibraryStatisticsDTO> libraryDTOs = new ArrayList<>();
        for (Library library : libraries) {
            int bookCount = library.getBooks().size();
            LibraryStatisticsDTO libraryDTO = new LibraryStatisticsDTO(library.getId(), library.getName(), bookCount);
            libraryDTOs.add(libraryDTO);
        }

        libraryDTOs.sort((dto1, dto2) -> Integer.compare(dto2.getBooksCount(), dto1.getBooksCount()));

        return new PageImpl<>(libraryDTOs, pageable, libraryPage.getTotalElements());
    }

    public long countLibraries() {
        return libraryRepository.count();
    }
}
