package com.sdi.app.dto;

import com.sdi.app.model.Author;
import com.sdi.app.model.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDTO {
    private Long id;
    private String name;
    private List<Book> books;

    public AuthorResponseDTO(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.books = author.getBooks();
    }
}
