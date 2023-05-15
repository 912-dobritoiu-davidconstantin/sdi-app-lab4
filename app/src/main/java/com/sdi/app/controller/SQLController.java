package com.sdi.app.controller;

import com.sdi.app.config.JwtUtils;
import com.sdi.app.dto.SQLRunResponseDTO;
import com.sdi.app.exception.UserNotAuthorizedException;
import com.sdi.app.model.ERole;
import com.sdi.app.model.User;
import com.sdi.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:8080", "https://lively-mochi-dbc1b6.netlify.app"})
@RestController
@RequestMapping("/api")
@Validated
public class SQLController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final JwtUtils jwtUtils;

    private final UserService userService;

    public SQLController(JwtUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping("/run-delete-authors-script")
    ResponseEntity<?> deleteAllAuthors(@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);

        boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
        );

        if (!isAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        try {
            String currentDir = System.getProperty("user.dir");
            String sql = Files.readString(Paths.get(currentDir + "/../delete_authors.sql"));
            jdbcTemplate.update(sql);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Successfully deleted all authors"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Error: something went wrong"));
        }
    }

    @PostMapping("/run-delete-books-script")
    ResponseEntity<?> deleteAllBooks(@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);

        boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
        );

        if (!isAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        try {
            String currentDir = System.getProperty("user.dir");
            String sql = Files.readString(Paths.get(currentDir + "/../delete_books.sql"));
            jdbcTemplate.update(sql);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Successfully deleted all books"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Error: something went wrong (make sure you deleted librarybooks first)"));
        }
    }

    @PostMapping("/run-delete-libraries-script")
    ResponseEntity<?> deleteAllLibraries(@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);

        boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
        );

        if (!isAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        try {
            String currentDir = System.getProperty("user.dir");
            String sql = Files.readString(Paths.get(currentDir + "/../delete_libraries.sql"));
            jdbcTemplate.update(sql);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Successfully deleted all libraries"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Error: something went wrong (make sure you deleted librarybooks first)"));
        }
    }

    @PostMapping("/run-delete-librarybooks-script")
    ResponseEntity<?> deleteAllLibraryBooks(@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);

        boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
        );

        if (!isAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        try {
            String currentDir = System.getProperty("user.dir");
            String sql = Files.readString(Paths.get(currentDir + "/../delete_librarybooks.sql"));
            jdbcTemplate.update(sql);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Successfully deleted all librarybooks"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Error: something went wrong"));
        }
    }

    @PostMapping("/run-insert-authors-script")
    ResponseEntity<?> insertAllAuthors(@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);

        boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
        );

        if (!isAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }

        try {
            String currentDir = System.getProperty("user.dir");
            String fullPath = currentDir + "/../insert_authors.sql";

            String fileContent = new String(Files.readAllBytes(Paths.get(fullPath)));

            String[] statements = fileContent.split(";");

            for (String statement : statements) {
                statement = statement.trim();
                if (!statement.isEmpty()) {
                    jdbcTemplate.execute(statement);
                }
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Successfully inserted all authors"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Error: something went wrong"));
        }
    }



    @PostMapping("/run-insert-books-script")
    ResponseEntity<?> insertAllBooks(@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);

        boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
        );

        if (!isAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }

        try {
            String currentDir = System.getProperty("user.dir");
            String fullPath = currentDir + "/../insert_books.sql";

            // Read the file content into a String
            String fileContent = new String(Files.readAllBytes(Paths.get(fullPath)));

            // Create the SimpleJdbcCall with the script content and set the script as the SQL statement
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withProcedureName("insert_books")
                    .withoutProcedureColumnMetaDataAccess()
                    .declareParameters(
                            new SqlParameter("script", Types.VARCHAR)
                    )
                    .withReturnValue();

            // Create the script parameter map
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("script", fileContent);

            // Execute the script using SimpleJdbcCall
            Map<String, Object> result = jdbcCall.execute(paramMap);

            // Check the result
            int returnValue = (int) result.get("#update-count-1");
            if (returnValue >= 0) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new SQLRunResponseDTO("Successfully inserted all books"));
            } else {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new SQLRunResponseDTO("Error: something went wrong"));
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Error: something went wrong"));
        }
    }

    @PostMapping("/run-insert-libraries-script")
    ResponseEntity<?> insertAllLibraries(@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);

        boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
        );

        if (!isAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }

        try {
            String currentDir = System.getProperty("user.dir");
            String fullPath = currentDir + "/../insert_libraries.sql";

            String fileContent = new String(Files.readAllBytes(Paths.get(fullPath)));

            String[] statements = fileContent.split(";");

            for (String statement : statements) {
                statement = statement.trim();
                if (!statement.isEmpty()) {
                    jdbcTemplate.execute(statement);
                }
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Successfully inserted all libraries"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Error: something went wrong"));
        }
    }

    @PostMapping("/run-insert-librarybooks-script")
    ResponseEntity<?> insertAllLibraryBooks(@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);

        boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
        );

        if (!isAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }

        try {
            String currentDir = System.getProperty("user.dir");
            String fullPath = currentDir + "/../insert_librarybooks.sql";

            String fileContent = new String(Files.readAllBytes(Paths.get(fullPath)));

            String[] statements = fileContent.split(";");

            for (String statement : statements) {
                statement = statement.trim();
                if (!statement.isEmpty()) {
                    jdbcTemplate.execute(statement);
                }
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Successfully inserted all librarybooks"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Error: something went wrong"));
        }
    }
}