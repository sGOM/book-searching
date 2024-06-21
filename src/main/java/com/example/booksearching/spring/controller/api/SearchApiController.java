package com.example.booksearching.spring.controller.api;

import com.example.booksearching.spring.dto.BookInfoResponse;
import com.example.booksearching.spring.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/search")
@RestController
public class SearchApiController {

    private final SearchService searchService;

    @GetMapping("/auto-complete")
    public ResponseEntity<List<BookInfoResponse>> getAutocompleteSuggestions(String keyword) {
        return new ResponseEntity<>(searchService.searchAutocompleteSuggestions(keyword), HttpStatus.OK);
    }

}
