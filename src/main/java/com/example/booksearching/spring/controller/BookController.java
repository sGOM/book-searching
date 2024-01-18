package com.example.booksearching.spring.controller;

import com.example.booksearching.spring.dto.BookSearchResponse;
import com.example.booksearching.spring.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/books")
@Controller
public class BookController {

    private final BookService bookService;
    
    @GetMapping("/search")
    @ResponseBody
    public List<BookSearchResponse> getSearch(@RequestParam(required = false) String keyword, @RequestParam(required = false) Integer size) {
        return bookService.searchBookTitles(keyword, size);
    }

}
