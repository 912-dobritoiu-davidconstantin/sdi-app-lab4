package com.sdi.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {

    private Long id;

    private String name;

    private String email;

    private String bio;

    private String country;

    private int booksCount;

}