package com.sdi.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryAllDTO {

    private Long id;

    private String name;

    private String description;

    private String location;

    private int rating;

    private String owner;
}