package com.sdi.app.controller;

import com.sdi.app.config.JwtUtils;
import com.sdi.app.model.User;
import com.sdi.app.model.UserProfile;
import com.sdi.app.service.UserService;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:8080", "https://lively-mochi-dbc1b6.netlify.app"})
@RestController
@RequestMapping("/api")
@Validated
public class UserController {

    private final UserService userService;

    private final JwtUtils jwtUtils;

    UserController(UserService userService, JwtUtils jwtUtils) {this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/user-profile-id/{id}")
    UserProfile getProfileById(@PathVariable Long id) {
        return userService.getUserProfileById(id);
    }

    @GetMapping("/user-profile-username/{username}")
    UserProfile getProfileByUsername(@PathVariable String username) {
        return userService.getUserProfileByUsername(username);
    }

    @GetMapping("/user/{username}")
    User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/user-number-authors/{id}")
    Integer getUserNumberOfAuthorsById(@PathVariable Long id) {
        return userService.getUserNumberOfAuthorsById(id);
    }

    @GetMapping("/user-number-books/{id}")
    Integer getUserNumberOfBooksById(@PathVariable Long id) {
        return userService.getUserNumberOfBooksById(id);
    }

    @GetMapping("/user-number-libraries/{id}")
    Integer getUserNumberOfLibrariesById(@PathVariable Long id) {
        return userService.getUserNumberOfLibrariesById(id);
    }

    @GetMapping("/user-number-librarybooks/{id}")
    Integer getUserNumberOfLibraryBooksById(@PathVariable Long id) {
        return userService.getUserNumberOfLibraryBooksById(id);
    }


    @GetMapping("/user-search")
    List<User> getUsersByName(@RequestParam(required = false) String username) {
        return this.userService.searchUsersByUsername(username);
    }

    @PutMapping("/user-profile/{id}")
    UserProfile updateUserProfile(@Valid @RequestBody UserProfile newUserProfile,
                                  @PathVariable Long id) {

        return userService.updateUserProfile(newUserProfile, id);
    }

    @PutMapping("/user-roles/{id}")
    User updateUser(@Valid @RequestBody HashMap<String, Boolean> roles,
                    @PathVariable Long id,
                    @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);

        return userService.updateRolesUser(roles, id, user.getId());
    }

}
