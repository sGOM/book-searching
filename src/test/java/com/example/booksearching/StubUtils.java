package com.example.booksearching;

import com.example.booksearching.entity.DocInfo;
import com.example.booksearching.entity.constant.DocType;

import java.time.Year;

public class StubUtils {
    public static DocInfo createDocInfo() {
        return DocInfo.of(
                "PCY_20171122038330711",
                DocType.BOOK,
                "아임상 및 경증 우울증 자기관리법 효용성 연구",
                "보건의료",
                "500",
                "이완요법은 이완을 통해 육체적, 정신적 긴장을 줄이는 것이다. 예로 명상과 MBSR과 같은 임상 연구가 진행 중이다. 이러한 프로그램은 경제성을 지니며 요가도 몸과 마음의 조절로 삶의 질을 향상시킨다. 무작위 대조연구에서도 우울증 해소에 요가의 효과성을 입증했지만 우울증상의 차이 등을 고려해 추가 연구가 필요하다.",
                "오강섭",
                "한국보건의료연구원",
                Year.of(2010)
        );
    }
}
