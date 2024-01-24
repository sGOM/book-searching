package com.example.booksearching.spring.controller;

import com.example.booksearching.spring.dto.BookSearchResponse;
import com.example.booksearching.spring.service.BookService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@RequestMapping("/books")
@Controller
public class BookController {

    private final BookService bookService;
    
    @GetMapping("/search")
    @ResponseBody
    public BookSearchResponse getSearch(
            String keyword,
            @Positive @RequestParam(defaultValue = "1", required = false) int page,
            @Positive @RequestParam(defaultValue = "24", required = false) int size
    ) {
        return bookService.searchBookTitles(keyword, page, size);
    }

}
