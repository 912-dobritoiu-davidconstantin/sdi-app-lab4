package com.sdi.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryBookDTO {

    private Long id;

    private Long bookID;

    private Long libraryID;

    private Date borrowDate;

    private Date returnDate;
}
