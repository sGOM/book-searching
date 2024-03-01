package com.example.booksearching.spring.service;

import com.example.booksearching.StubUtils;
import com.example.booksearching.spring.entity.Book;
import com.example.booksearching.spring.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("DocInfo 로직")
@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @InjectMocks
    private SearchService sut;
    @Mock
    private BookRepository bookRepository;

//    @DisplayName("DocInfo를 건네면, 저장하고 저장한 DocInfo를 반환한다")
//    @Test
//    void givenDocInfo_whenSaving_thenSaveAndReturnDocInfo() {
//        // Given
//        Book book = StubUtils.createDocInfo();
//        given(bookRepository.save(book)).willReturn(book);
//
//        // When
//        Book res = sut.saveDocInfo(book);
//
//        // Then
//        assertEquals(res, book);
//        then(bookRepository).should().save(book);
//    }
}