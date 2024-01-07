package com.example.booksearching.spring.entity;

import com.example.booksearching.spring.entity.converter.YearToIntConverter;
import jakarta.persistence.*;
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
    @Column(length = 100)
    private String id;
    @Column(nullable = false, length = 250)
    private String name;
    @Column(nullable = false, length = 200)
    private String kdcLabel;
    @Column(nullable = false, length = 3)
    private String kdcCode;
//    @Column(nullable = false, length = 600)
//    private String summary;
    @Column(length = 200)
    private String author;
//    @Column(length = 200)
//    private String publisher;
    @Convert(converter = YearToIntConverter.class)
    private Year publishedYear;

    private Book(String id, String name, String kdcLabel, String kdcCode, String author, Year publishedYear) {
        this.id = id;
        this.name = name;
        this.kdcLabel = kdcLabel;
        this.kdcCode = kdcCode;
        this.author = author;
        this.publishedYear = publishedYear;
    }

    public static Book of(String id, String name, String kdcLabel, String kdcCode, String author, Year publishedYear) {
        return new Book(
                Optional.of(id).get(),
                Optional.of(name).get(),
                Optional.of(kdcLabel).get(),
                Optional.of(kdcCode).get(),
                author,
                publishedYear
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

}