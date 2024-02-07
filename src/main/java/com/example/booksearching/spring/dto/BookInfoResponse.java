package com.example.booksearching.spring.dto;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.booksearching.elasticsearch.model.BookDocument;
import com.example.booksearching.spring.exception.ElasticsearchCommunicationException;
import com.example.booksearching.spring.exception.ElasticsearchCommunicationExceptionType;

import java.time.Year;
import java.util.Optional;

public record BookInfoResponse(
        String isbn,
        String title,
        String highlightTitle,
        String author,
        Year publishedYear,
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
