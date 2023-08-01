package com.example.booksearching.repository;

import com.example.booksearching.entity.DocInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocInfoRepository extends JpaRepository<DocInfo, String> {
}
