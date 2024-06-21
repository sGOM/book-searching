package com.example.booksearching.spring.controller.web;

import com.example.booksearching.spring.service.SearchService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/search")
@Controller
public class SearchPageController {

    private final SearchService searchService;

    @GetMapping
    public String getSearchPage(
            String keyword,
            @Positive @RequestParam(defaultValue = "1", required = false) int page,
            @Positive @RequestParam(defaultValue = "24", required = false) int size,
            Model map
    ) {
        map.addAttribute("data", searchService.searchBooks(keyword, page, size));

        return "index";
    }

}
