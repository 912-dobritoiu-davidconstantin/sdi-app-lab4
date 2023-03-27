package com.sdi.app.dto;

import com.sdi.app.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorWithBookDTO {

    private Long id;

    private String name;

    private String email;

    private String bio;

    private String country;

    private List<BookForAuthorDTO> books;
}