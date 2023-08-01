package com.example.booksearching.entity;

import com.example.booksearching.entity.constant.DocType;
import com.example.booksearching.entity.converter.YearToIntConverter;
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
public class DocInfo {
    @Id
    @Column(length = 100)
    private String id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocType type;
    @Column(nullable = false, length = 250)
    private String name;
    @Column(nullable = false, length = 200)
    private String kdcLabel;
    @Column(nullable = false, length = 3)
    private String kdcCode;
    @Column(nullable = false, length = 600)
    private String summary;
    @Column(length = 200)
    private String author;
    @Column(length = 200)
    private String publisher;
    @Convert(converter = YearToIntConverter.class)
    private Year publishedYear;

    private DocInfo(String id, DocType type, String name, String kdcLabel, String kdcCode, String summary, String author, String publisher, Year publishedYear) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.kdcLabel = kdcLabel;
        this.kdcCode = kdcCode;
        this.summary = summary;
        this.author = author;
        this.publisher = publisher;
        this.publishedYear = publishedYear;
    }

    public static DocInfo of(String id, DocType type, String name, String kdcLabel, String kdcCode, String summary, String author, String publisher, Year publishedYear) {
        return new DocInfo(
                Optional.of(id).get(),
                Optional.of(type).get(),
                Optional.of(name).get(),
                Optional.of(kdcLabel).get(),
                Optional.of(kdcCode).get(),
                Optional.of(summary).get(),
                author,
                publisher,
                publishedYear
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DocInfo that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

}
