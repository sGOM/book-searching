package com.example.booksearching.elasticsearch.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Book {
    private String isbnThirteenNo;
    private String titleName;
    private String authorName;
    private Integer publicationYear;
    private Integer price;

    private Book(String isbnThirteenNo, String titleName, String authorName, Integer publicationYear, Integer price) {
        if (isbnThirteenNo.length() != 13) throw new IllegalArgumentException("isbnThirteenNo length must be 13");
        if (titleName == null) throw new NullPointerException("titleName must be notnull");
        if (publicationYear == null) throw new NullPointerException("publicationYear must be notnull");
        if (price < 0) throw new IllegalArgumentException("price must be positive number");

        this.isbnThirteenNo = isbnThirteenNo;
        this.titleName = titleName;
        this.authorName = authorName;
        this.publicationYear = publicationYear;
        this.price = price;
    }

    public static Book of(String isbnThirteenNo, String titleName, String authorName, Integer publicationYear, Integer price) {
        return new Book(
                isbnThirteenNo,
                titleName,
                authorName,
                publicationYear,
                price
        );
    }
}
