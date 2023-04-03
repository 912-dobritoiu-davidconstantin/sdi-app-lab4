package com.sdi.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO {
    @NotBlank(message = "Title is mandatory")
    private String title;

    private int year;

    private double price;

    private int rating;
}
