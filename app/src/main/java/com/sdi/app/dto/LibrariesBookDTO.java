package com.sdi.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibrariesBookDTO {
    private Long id;

    private Date borrowDate;

    private Date returnDate;
}
