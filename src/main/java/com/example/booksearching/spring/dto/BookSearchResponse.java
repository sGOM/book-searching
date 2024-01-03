package com.example.booksearching.spring.dto;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.booksearching.elasticsearch.model.BookDocument;

import java.time.Year;
import java.util.List;
import java.util.Map;

public record BookSearchResponse(
        String isbn,
        String title,
        String highlightTitle,
        String author,
        Year publishedYear
) {

    public static BookSearchResponse of(String isbn, String title, String highlightTitle, String author, Year publishedYear) {
        return new BookSearchResponse(isbn, title, highlightTitle, author, publishedYear);
    }

    public static BookSearchResponse from(Hit<BookDocument> hit) {
        assert hit.source() != null;
        BookDocument doc = hit.source();
        Map<String, List<String>> resultHighlights = hit.highlight();

        return BookSearchResponse.of(
                doc.getIsbn_thirteen_no(),
                doc.getTitle(),
                resultHighlights.entrySet().stream()
                        .findFirst()
                        .filter(entry -> !entry.getValue().isEmpty())
                        .map(entry -> entry.getValue().get(0))
                        .orElse(doc.getTitle()),
                doc.getAuthor(),
                Year.of(doc.getPublished_year())
        );
    }

}
