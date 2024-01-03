package com.example.booksearching.spring.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.*;
import com.example.booksearching.elasticsearch.model.BookDocument;
import com.example.booksearching.spring.dto.BookSearchResponse;
import com.example.booksearching.spring.entity.Book;
import com.example.booksearching.spring.exception.ElasticsearchCommunicationException;
import com.example.booksearching.spring.exception.ElasticsearchCommunicationExceptionType;
import com.example.booksearching.spring.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final ElasticsearchClient esClient;

    public Book saveDocInfo(Book book) {
        return bookRepository.save(book);
    }

    public List<BookSearchResponse> searchBookTitles(String keyword) {
        final String BOOK_INDEX = "books";
        final String FIELD_NAME = "title";
        final Integer SIZE = 10;
        final Float DEFAULT_BOOST_VALUE = 1f;
        final Float PARTIAL_BOOST_VALUE = 0.3f;

        // field = {"", "_engtokor", "_chosung", "_jamo"}
        Map<String, Float> fieldBoostMap = Map.of(
                "", DEFAULT_BOOST_VALUE,
                "_engtokor", DEFAULT_BOOST_VALUE
        );
        Map<String, Float> koreanPhonemefieldBoostMap = Map.of(
                "_chosung", DEFAULT_BOOST_VALUE,
                "_jamo", DEFAULT_BOOST_VALUE
        );

        // option = {".exact", ".edge", ".partial"}
        Map<String, Float> optionBoostMap = Map.of(
                ".exact", DEFAULT_BOOST_VALUE,
                ".edge", DEFAULT_BOOST_VALUE,
                ".partial", PARTIAL_BOOST_VALUE
        );

        // MultiMatchQuery 생성
        MultiMatchQuery multiMatchQuery = createMultiMatchQuery(keyword, FIELD_NAME, fieldBoostMap, optionBoostMap, TextQueryType.BestFields);
        MultiMatchQuery koreanPhonemeMultiMatchQuery = createMultiMatchQuery(keyword, FIELD_NAME, koreanPhonemefieldBoostMap, optionBoostMap, TextQueryType.Phrase);

        // Highlight 생성
        Highlight highlight = createHighlightFieldMap(
                Stream.of(multiMatchQuery.fields(), koreanPhonemeMultiMatchQuery.fields())
                        .flatMap(Collection::stream)
                        .toList()
        );

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index(BOOK_INDEX)
                .size(SIZE)
                .query(queryBuilder ->
                        queryBuilder.bool(boolBuilder ->
                                boolBuilder.should(
                                        List.of(
                                                multiMatchQuery._toQuery(),
                                                koreanPhonemeMultiMatchQuery._toQuery()
                                        )
                                )
                        )
                )
                .highlight(highlight)
                .build();

        SearchResponse<BookDocument> response = null;
        try {
            response = esClient.search(searchRequest, BookDocument.class);
        } catch (ElasticsearchException e) {
            throw new ElasticsearchCommunicationException(ElasticsearchCommunicationExceptionType.ELASTICSEARCH_SEARCH_FAIL);
        } catch (IOException e) {
            throw new ElasticsearchCommunicationException(ElasticsearchCommunicationExceptionType.ELASTICSEARCH_IO_FAIL);
        }

        TotalHits total = response.hits().total();
        assert total != null;
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;
        log.info("There are " + (isExactResult ? "" : "more than ") + total.value() + " results");

        List<Hit<BookDocument>> hits = response.hits().hits();
        List<BookSearchResponse> res = hits.stream().map(BookSearchResponse::from).toList();

        log.info("Search result: {{}}", res.stream().map(BookSearchResponse::toString).collect(Collectors.joining(",\n")));

        return res;
    }

    private List<String> createMultiMatchFieldList(String fieldName, Map<String, Float> fieldBoostMap, Map<String, Float> optionBoostMap) {
        return fieldBoostMap.entrySet().stream()
                .flatMap(fieldBoostEntry ->
                    optionBoostMap.entrySet().stream()
                            .map(optionBoostEntry -> fieldName + fieldBoostEntry.getKey() + optionBoostEntry.getKey() + "^" + fieldBoostEntry.getValue() * optionBoostEntry.getValue())
                ).toList();
    }

    private MultiMatchQuery createMultiMatchQuery(String keyword, String fieldName, Map<String, Float> fieldBoostMap, Map<String, Float> optionBoostMap, TextQueryType queryType) {
        return new MultiMatchQuery.Builder()
                .query(keyword)
                .type(queryType)
                .fields(createMultiMatchFieldList(fieldName, fieldBoostMap, optionBoostMap))
                .build();
    }

    // https://stackoverflow.com/questions/71351777/how-to-explicitly-order-highlighted-fields-using-elasticsearch-java-api-client
    private Highlight createHighlightFieldMap(List<String> fieldNames) {
        Map<String, HighlightField> highlightFieldMap = new HashMap<>();
        for (String fieldName : fieldNames) {
            highlightFieldMap.put(
                    fieldName.substring(0, fieldName.lastIndexOf('^')),
                    new HighlightField.Builder().postTags("</strong>").preTags("<strong>").build()
            );
        }
        return new Highlight.Builder().fields(highlightFieldMap).build();
    }

}
