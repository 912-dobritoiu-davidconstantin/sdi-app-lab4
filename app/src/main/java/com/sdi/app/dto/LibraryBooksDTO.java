package com.sdi.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryBooksDTO {
    private Long id;

    private Long libraryID;

    private Date borrowDate;

    private Date returnDate;
}
