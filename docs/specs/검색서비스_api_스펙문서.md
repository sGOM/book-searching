# 검색 서비스 REST API 스펙 문서

## 1. 자동 완성

### Request

```http request
GET /search/auto-complete?keyword={keyword}
```

Query Parameters:

| 필드       | 타입     | 설명     | 제한                                                          |
|----------|--------|--------|-------------------------------------------------------------|
| username | String | 유저 아이디 | ^[a-zA-Z0-9]{6,12}$                                         |
| password | String | 비밀번호   | ^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\S+$).{8,16}$ |
| nickname | String | 닉네임    | 3~20자                                                       |
| role     | String | 유저 권한  | ROLE_USER, ROLE_ADMIN                                       |

### Response

#### 정상

```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "data": [
    {
        "isbn": "9788954655224",
        "title": "해리엇",
        "highlightTitle": "<strong>해리</strong>엇",
        "author": "한윤섭 원작·각색",
        "publishedYear": "2019",
        "price": 43000
    },
    {
        "isbn": "9788949110813",
        "title": "안녕, 해리!",
        "highlightTitle": "안녕, <strong>해리</strong>!",
        "author": "마틴 워델 글 ; 바바라 퍼스 그림 ; 노은정 옮김",
        "publishedYear": "2002",
        "price": 21000
    },
    {
        "isbn": "9788901234557",
        "title": "뚝딱뚝딱 해리",
        "highlightTitle": "뚝딱뚝딱 <strong>해리</strong>",
        "author": "라스르 클린팅 글·그림;황덕령 옮김",
        "publishedYear": "2019",
        "price": 92000
    },
    {
        "isbn": "9787543474987",
        "title": "海狸的？？",
        "highlightTitle": "<strong>海狸</strong>的？？",
        "author": "[美] 伊丽莎白·乔治·斯皮尔 著 ; 徐匡 译",
        "publishedYear": "2015",
        "price": 30000
    },
    {
        "isbn": "9788993925374",
        "title": "해리포터",
        "highlightTitle": "<strong>해리</strong>포터",
        "author": "조앤 K. 롤링 지음;김혜원;최인자 [공]옮김",
        "publishedYear": "2017",
        "price": 6000
    },
    {
        "isbn": "9788975484322",
        "title": "해리장애",
        "highlightTitle": "<strong>해리</strong>장애",
        "author": "도상금 지음",
        "publishedYear": "2000",
        "price": 26000
    },
    {
        "isbn": "8809462432792",
        "title": "해리포터",
        "highlightTitle": "<strong>해리</strong>포터",
        "author": "크리스 콜롬버스 감독",
        "publishedYear": "2021",
        "price": 39000
    },
    {
        "isbn": "8809323147834",
        "title": "해리포터",
        "highlightTitle": "<strong>해리</strong>포터",
        "author": "크리스 콜럼버스 외 감독",
        "publishedYear": "2012",
        "price": 66000
    },
    {
        "isbn": "9788983925649",
        "title": "해리포터",
        "highlightTitle": "<strong>해리</strong>포터",
        "author": "J.K. 롤링 지음 ;최인자 옮김",
        "publishedYear": "2014",
        "price": 54000
    },
    {
        "isbn": "9788983927844",
        "title": "해리포터",
        "highlightTitle": "<strong>해리</strong>포터",
        "author": "J. K. 롤링 지음;강동혁 옮김",
        "publishedYear": "2021",
        "price": 14000
    }
  ]
}
```

#### 요청 형식이 잘못되었거나 필수 필드가 누락된 경우.

```http
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "error": {
    "message": "요청 형식이 잘못되었습니다.",
    "code": 2000
  }
}
```
