package com.sdi.app.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryStatisticsDTO {

    private Long libraryId;

    private String libraryName;

    private int booksCount;
}
