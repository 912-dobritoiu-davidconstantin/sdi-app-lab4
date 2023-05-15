package com.sdi.app.service;

import com.sdi.app.exception.RoleNotFoundException;
import com.sdi.app.exception.UserNotAuthorizedException;
import com.sdi.app.exception.UserNotFoundException;
import com.sdi.app.exception.UserProfileNotFoundException;
import com.sdi.app.model.ERole;
import com.sdi.app.model.Role;
import com.sdi.app.model.User;
import com.sdi.app.model.UserProfile;
import com.sdi.app.repository.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    private final LibraryRepository libraryRepository;

    private final LibraryBookRepository libraryBookRepository;

    private final UserProfileRepository userProfileRepository;

    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, AuthorRepository authorRepository, BookRepository bookRepository, LibraryRepository libraryRepository, LibraryBookRepository libraryBookRepository, UserProfileRepository userProfileRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.libraryRepository = libraryRepository;
        this.libraryBookRepository = libraryBookRepository;
        this.userProfileRepository = userProfileRepository;
        this.roleRepository = roleRepository;
    }

    public UserProfile getUserProfileById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return user.getUserProfile();
    }

    public UserProfile getUserProfileByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return user.getUserProfile();
    }

    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    public Integer getUserNumberOfAuthorsById(Long id) {
        return authorRepository.findByUserId(id).size();
    }

    public Integer getUserNumberOfBooksById(Long id) {
        return bookRepository.findByUserId(id).size();
    }

    public Integer getUserNumberOfLibrariesById(Long id) {
        return libraryRepository.findByUserId(id).size();
    }

    public Integer getUserNumberOfLibraryBooksById(Long id) { return libraryBookRepository.findByUserId(id).size();}

    public List<User> searchUsersByUsername(String username) {
        return this.userRepository.findByUsernameContainingIgnoreCaseOrUsernameIsNull(username);
    }

    public UserProfile updateUserProfile(UserProfile newUserProfile, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        boolean isUser = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_USER || role.getName() == ERole.ROLE_ADMIN
        );
        if (!isUser) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }

        return userProfileRepository.findById(user.getUserProfile().getId())
                .map(userProfile -> {
                    userProfile.setBio(newUserProfile.getBio());
                    userProfile.setLocation(newUserProfile.getLocation());
                    userProfile.setGender(newUserProfile.getGender());
                    userProfile.setMaritalStatus(newUserProfile.getMaritalStatus());
                    userProfile.setBirthdate(newUserProfile.getBirthdate());
                    return userProfileRepository.save(userProfile);
                })
                .orElseThrow(() -> new UserProfileNotFoundException(id));
    }

    public User updateRolesUser(HashMap<String, Boolean> roles, Long id, Long userID) {
        User callerUser = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));

        boolean isAdmin = callerUser.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
        );
        if (!isAdmin) {
            throw new UserNotAuthorizedException(String.format(callerUser.getUsername()));
        }

        User user = this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        Set<Role> roleSet = new HashSet<>();
        if (roles.get("isUser")) {
            Role role = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_USER));
            roleSet.add(role);
        }
        if (roles.get("isModerator")){
            Role role = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_MODERATOR));
            roleSet.add(role);
        }
        if (roles.get("isAdmin")){
            Role role = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_ADMIN));
            roleSet.add(role);
        }
        user.setRoles(roleSet);
        return userRepository.save(user);
    }
}
