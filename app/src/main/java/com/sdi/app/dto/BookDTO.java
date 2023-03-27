package com.sdi.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private Long id;

    private String title;

    private int year;

    private double price;

    private int rating;

    private AuthorDTO author;

    private Set<LibraryBooksDTO> libraries;
}