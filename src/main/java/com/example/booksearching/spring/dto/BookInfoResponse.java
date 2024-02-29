package com.example.booksearching.spring.dto;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.booksearching.elasticsearch.model.BookDocument;
import com.example.booksearching.spring.exception.ElasticsearchCommunicationException;
import com.example.booksearching.spring.exception.ElasticsearchCommunicationExceptionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.Year;
import java.util.Optional;

public record BookInfoResponse(
        @NotNull
        @Size(min = 13, max = 13)
        String isbn,
        @NotNull
        @Size(max = 250)
        String title,
        String highlightTitle,
        @Size(max = 200)
        String author,
        Year publishedYear,
        @NotNull
        @PositiveOrZero
        Integer price
) {

    public static BookInfoResponse of(String isbn, String title, String highlightTitle, String author, Year publishedYear, Integer price) {
        return new BookInfoResponse(isbn, title, highlightTitle, author, publishedYear, price);
    }

    public static BookInfoResponse from(Hit<BookDocument> hit) {
        Optional.ofNullable(hit.source()).orElseThrow(()-> new ElasticsearchCommunicationException(ElasticsearchCommunicationExceptionType.ELASTICSEARCH_SEARCH_FAIL));

        BookDocument doc = hit.source();
        String firstHighlight = hit.highlight()
                .entrySet()
                .stream()
                .findFirst()
                .map(entry -> entry.getValue().stream().findFirst().orElse(doc.getTitle()))
                .orElse(doc.getTitle());

        return BookInfoResponse.of(
                doc.getIsbn_thirteen_no(),
                doc.getTitle(),
                firstHighlight,
                doc.getAuthor(),
                Year.of(doc.getPublished_year()),
                doc.getPrice()
        );
    }

}
