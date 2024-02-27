package com.example.booksearching.spring.entity;

import com.example.booksearching.spring.entity.converter.YearToIntConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Year;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Book {

    @Id
    @Column(length = 13)
    private String isbn;

    @Column(nullable = false, length = 250)
    private String title;

    @Column(length = 200)
    private String author;

    @Column(nullable = false)
    @Min(value = 0, message = "가격은 0 이상이어야 합니다")
    private Integer price;

    private Year publishYear;

    private Book(String isbn, String title, String author, Integer price, Year publishYear) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.price = price;
        this.publishYear = publishYear;
    }

    public static Book of(String isbn, String title, String author, Integer price, Year publishYear) {
        return new Book(
                Optional.of(isbn).get(),
                Optional.of(title).get(),
                author,
                price,
                publishYear
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book that)) return false;
        return this.getIsbn() != null && this.getIsbn().equals(that.getIsbn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getIsbn());
    }

}
