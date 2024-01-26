package com.example.booksearching.spring.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.DisMaxQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhraseQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import com.example.booksearching.elasticsearch.model.BookDocument;
import com.example.booksearching.spring.dto.BookInfoResponse;
import com.example.booksearching.spring.dto.BookSearchResponse;
import com.example.booksearching.spring.dto.PageInfoResponse;
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

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final PaginationService paginationService;
    private final ElasticsearchClient esClient;

    public Book saveDocInfo(Book book) {
        return bookRepository.save(book);
    }

    public BookSearchResponse searchBookTitles(String keyword, Integer page, Integer size) {
        final String BOOK_INDEX = "books";
        final String FIELD_NAME = "title";
        final Float KEYWORD_BOOST_VALUE = 2f;
        final Float PHRASE_BOOST_VALUE = 1.5f;
        final Float LANGUAGE_BOOST_VALUE = 1.2f;
        final Float DEFAULT_BOOST_VALUE = 1f;
        final Float PARTIAL_BOOST_VALUE = 0.5f;

        String[] fieldSuffixes = containsKorean(keyword) ? new String[]{"", "_chosung", "_jamo"} : new String[]{"", "_engtokor"};
        Map<String, Float> boostValueByMultiFieldMap = Map.of(
                "", KEYWORD_BOOST_VALUE,
                ".edge", DEFAULT_BOOST_VALUE,
                ".partial", PARTIAL_BOOST_VALUE
        );

        List<Query> queryList = new ArrayList<>(createMatchQueryList(FIELD_NAME, fieldSuffixes, boostValueByMultiFieldMap, keyword).stream().map(MatchQuery::_toQuery).toList());
        String languageField = FIELD_NAME + (containsKorean(keyword) ? ".kor" : ".en");
        queryList.add(createMatchQuery(keyword, languageField, LANGUAGE_BOOST_VALUE)._toQuery());
        queryList.add(createMatchPhraseQuery(keyword, languageField, PHRASE_BOOST_VALUE)._toQuery());

        DisMaxQuery disMaxQuery = new DisMaxQuery.Builder()
                .queries(queryList)
                .build();

        // Highlight 생성
        Highlight highlight = createHighlightFieldMap(List.of(FIELD_NAME, FIELD_NAME + ".en", FIELD_NAME + ".kor", FIELD_NAME + ".edge", FIELD_NAME + ".partial"));

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index(BOOK_INDEX)
                .size(size)
                .from(size * (page - 1))        // TODO: 높은 수가 들어 가면 어떻게 되는지 테스트
                .query(queryBuilder -> queryBuilder.disMax(disMaxQuery))
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

        HitsMetadata<BookDocument> hitsMetadata = response.hits();
        List<BookInfoResponse> booksInfo = hitsMetadata.hits().stream().map(BookInfoResponse::from).toList();
        PageInfoResponse pageInfo = paginationService.getPageInfo(hitsMetadata, page, size);
        BookSearchResponse res = BookSearchResponse.of(booksInfo, pageInfo);

        Optional.ofNullable(hitsMetadata.total()).orElseThrow(()-> new ElasticsearchCommunicationException(ElasticsearchCommunicationExceptionType.ELASTICSEARCH_SEARCH_FAIL));
        log.info("There are " + (hitsMetadata.total().relation() == TotalHitsRelation.Eq ? "" : "more than ") + hitsMetadata.total().value() + " results");
        log.info("Search result: {{}}", booksInfo.stream().map(BookInfoResponse::toString).collect(Collectors.joining(",\n")));

        return res;
    }

    private boolean containsKorean(String text) {
        return (text != null) && text.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
    }

    private List<MatchQuery> createMatchQueryList(String fieldName, String[] fieldSuffixes, Map<String, Float> boostValueByMultiFieldMap, String keyword) {
        return Arrays.stream(fieldSuffixes)
                .flatMap(fieldSuffix -> boostValueByMultiFieldMap.entrySet().stream()
                        .map(boostValueByMultiFieldEnt -> {
                            String multiField = boostValueByMultiFieldEnt.getKey();
                            Float boostValue = boostValueByMultiFieldEnt.getValue();
                            return createMatchQuery(keyword, fieldName + fieldSuffix + multiField, boostValue);
                        }))
                .collect(Collectors.toList());
    }

    private MatchQuery createMatchQuery(String keyword, String fieldName, Float boostValue) {
        return new MatchQuery.Builder()
                .query(keyword)
                .field(fieldName)
                .boost(boostValue)
                .build();
    }

    private MatchPhraseQuery createMatchPhraseQuery(String keyword, String fieldName, Float boostValue) {
        return new MatchPhraseQuery.Builder()
                .query(keyword)
                .field(fieldName)
                .boost(boostValue)
                .build();
    }

    // https://stackoverflow.com/questions/71351777/how-to-explicitly-order-highlighted-fields-using-elasticsearch-java-api-client
    private Highlight createHighlightFieldMap(List<String> fieldNames) {
        Map<String, HighlightField> highlightFieldMap = new HashMap<>();
        for (String fieldName : fieldNames) {
            highlightFieldMap.put(
                    fieldName,
                    new HighlightField.Builder().postTags("</strong>").preTags("<strong>").build()
            );
        }
        return new Highlight.Builder().fields(highlightFieldMap).build();
    }

}
