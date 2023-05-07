package com.sdi.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookWithAuthorIDDTO {
    private Long id;

    private String title;

    private int year;

    private double price;

    private int rating;

    private Long authorId;

    private String username;
}
