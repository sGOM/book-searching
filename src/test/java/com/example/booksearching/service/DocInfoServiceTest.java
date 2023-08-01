package com.example.booksearching.service;

import com.example.booksearching.StubUtils;
import com.example.booksearching.entity.DocInfo;
import com.example.booksearching.repository.DocInfoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@DisplayName("DocInfo 로직")
@ExtendWith(MockitoExtension.class)
class DocInfoServiceTest {
    @InjectMocks
    private DocInfoService sut;
    @Mock
    private DocInfoRepository docInfoRepository;

    @DisplayName("DocInfo를 건네면, 저장하고 저장한 DocInfo를 반환한다")
    @Test
    void givenDocInfo_whenSaving_thenSaveAndReturnDocInfo() {
        // Given
        DocInfo docInfo = StubUtils.createDocInfo();
        given(docInfoRepository.save(docInfo)).willReturn(docInfo);

        // When
        DocInfo res = sut.saveDocInfo(docInfo);

        // Then
        assertEquals(res, docInfo);
        then(docInfoRepository).should().save(docInfo);
    }
}