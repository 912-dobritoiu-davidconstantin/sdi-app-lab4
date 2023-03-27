package com.sdi.app.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorStatisticsDTO {

    private Long authorId;

    private String authorName;

    private int booksCount;
}
