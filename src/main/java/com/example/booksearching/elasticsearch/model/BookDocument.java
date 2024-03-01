package com.example.booksearching.elasticsearch.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class BookDocument {
    // Elasticsearch 필드 네이밍 규칙 : https://www.elastic.co/guide/en/ecs/current/ecs-guidelines.html
    private String isbn_thirteen_no;
    private String title;
    private String title_chosung;
    private String title_jamo;
    private String title_engtokor;
    private String author;
    private int published_year;
    private int price;

    private BookDocument(String isbnThirteenNo, String title, String titleChosung, String titleJamo, String titleEngToKor, String author, int publishedYear, int price) {
        this.isbn_thirteen_no = isbnThirteenNo;
        this.title = title;
        this.title_chosung = titleChosung;
        this.title_jamo = titleJamo;
        this.title_engtokor = titleEngToKor;
        this.author = author;
        this.published_year = publishedYear;
        this.price = price;
    }

    public static BookDocument of(String isbnThirteenNo, String title, String titleChosung, String titleJamo, String titleEngToKor, String author, int publishedYear, int price) {
        return new BookDocument(
                isbnThirteenNo,
                title,
                titleChosung,
                titleJamo,
                titleEngToKor,
                author,
                publishedYear,
                price
        );
    }
}
