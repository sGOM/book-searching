package com.example.booksearching.elasticsearch.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;

@Setter
@Getter
@ToString
public class Book {
    private String isbnThirteenNo;
    private String titleName;
    private String authorName;
    private int publicationYear;

    private Book(String isbnThirteenNo, String titleName, String authorName, int publicationYear) {
        this.isbnThirteenNo = Optional.of(isbnThirteenNo)
                .filter(s -> s.length() == 13)
                .orElseThrow(() -> new IllegalArgumentException("String length is not 13"));
        this.titleName = Optional.of(titleName).get();
        this.authorName = authorName;
        this.publicationYear = Optional.of(publicationYear).get();
    }

    public static Book of(String isbnThirteenNo, String titleName, String authorName, int publicationYear) {
        return new Book(
                isbnThirteenNo,
                titleName,
                authorName,
                publicationYear
        );
    }
}
