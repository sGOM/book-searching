package com.example.booksearching.controller;

import com.example.booksearching.entity.DocInfo;
import com.example.booksearching.service.DocInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/docinfos")
@Controller
public class DocInfoController {

    private final DocInfoService docInfoService;

    // TODO : keyword에 따라 검색 결과를 DocInfo List 형태로 반환하기
    @GetMapping("/search")
    @ResponseBody
    public List<String> getSearch(@RequestParam(required = false) String keyword) {
        System.out.println(keyword);
        List<String> docinfos = docInfoService.searchDocInfos(keyword);

        return docinfos;
    }
}
