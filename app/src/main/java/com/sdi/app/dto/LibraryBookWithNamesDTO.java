package com.sdi.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryBookWithNamesDTO {

    private Long id;

    private String bookTitle;

    private String libraryName;

    private Date borrowDate;

    private Date returnDate;
}
