package com.example.booksearching.spring.service;

import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import com.example.booksearching.spring.dto.PageInfoResponse;
import com.example.booksearching.spring.exception.ElasticsearchCommunicationException;
import com.example.booksearching.spring.exception.ElasticsearchCommunicationExceptionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Transactional
@Service
public class PaginationService {

    private static final int BAR_LENGTH = 10;

    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages){
        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2), 1);
        int endNumber = Math.min(startNumber + BAR_LENGTH - 1, totalPages);

        return IntStream.rangeClosed(startNumber, endNumber).boxed().toList();
    }

    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int size, int totalElements){
        int totalPages = totalElements / size + (totalElements % size == 0 ? 0 : 1);

        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2), 1);
        int endNumber = Math.min(startNumber + BAR_LENGTH - 1, totalPages);

        return IntStream.rangeClosed(startNumber, endNumber).boxed().toList();
    }

    public <T> PageInfoResponse getPageInfo(HitsMetadata<T> hitsMetadata, int page, int size) {
        Optional.ofNullable(hitsMetadata.total()).orElseThrow(()-> new ElasticsearchCommunicationException(ElasticsearchCommunicationExceptionType.ELASTICSEARCH_SEARCH_FAIL));

        int totalElements = (int)hitsMetadata.total().value();
        boolean totalHitsRelationIsEq = hitsMetadata.total().relation() == TotalHitsRelation.Eq;
        int totalPages = totalElements / size + (totalElements % size == 0 ? 0 : 1);
        List<Integer> barNumberList = this.getPaginationBarNumbers(page, size, totalElements);

        return PageInfoResponse.of(
                page,
                size,
                totalPages,
                totalElements,
                totalHitsRelationIsEq,
                barNumberList
        );
    }

    public int currentBarLength(){ return BAR_LENGTH; }

}