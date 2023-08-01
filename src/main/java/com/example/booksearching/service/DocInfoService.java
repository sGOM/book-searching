package com.example.booksearching.service;

import com.example.booksearching.entity.DocInfo;
import com.example.booksearching.repository.DocInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class DocInfoService {
    private final DocInfoRepository docInfoRepository;

    public DocInfo saveDocInfo(DocInfo docInfo) {
        return docInfoRepository.save(docInfo);
    }

    // TODO : repository랑 연결하고 검색해서 실제 DB에 검색해서 결과 10개 정도 반환
    public List<String> searchDocInfos(String keyword) {
        return List.of("Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Virgin Islands (US)", "Yemen", "Zambia", "Zimbabwe");
    }
}
