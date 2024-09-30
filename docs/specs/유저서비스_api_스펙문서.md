# 유저 서비스 REST API 스펙 문서

## 1. 회원가입

### Request

```http request
POST /users/sign-up
Content-Type: application/json;charset=UTF-8

{
    "username" : "testA6195",
    "password" : "123456789!qw",
    "nickname" : "tester",
    "role" : "ROLE_USER"
}
```

Request Body Schema:

| 필드       | 타입     | 설명     | 필수 여부 (O/X) | 제한                                                          |
|----------|--------|--------|-------------|-------------------------------------------------------------|
| username | String | 유저 아이디 | O           | ^[a-zA-Z0-9]{6,12}$                                         |
| password | String | 비밀번호   | O           | ^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\S+$).{8,16}$ |
| nickname | String | 닉네임    | O           | 3~20자                                                       |
| role     | String | 유저 권한  | O           | ROLE_USER, ROLE_ADMIN                                       |

### Response

#### 정상

```http
HTTP/1.1 200 OK
```

#### 요청 형식이 잘못되었거나 필수 필드가 누락된 경우.

```http
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "error": {
    "message": "요청 형식이 잘못되었습니다.",
    "code": 1000
  }
}
```

#### 입력 규칙이 위배된 경우.

```http
HTTP/1.1 422 Unprocessable Entity
Content-Type: application/json;charset=UTF-8

{
  "error": {
    "message": "입력 규칙이 위배된 데이터가 포함되어 있습니다.",
    "code": 1001
  }
}
```

#### 이미 존재하는 유저 아이디인 경우.

```http
HTTP/1.1 409 Conflict
Content-Type: application/json;charset=UTF-8

{
  "error": {
    "message": "이미 존재하는 유저 아이디입니다.",
    "code": 1010
  }
}
```

#### 이미 존재하는 닉네임인 경우.

```http
HTTP/1.1 409 Conflict
Content-Type: application/json;charset=UTF-8

{
  "error": {
    "message": "이미 존재하는 닉네임입니다.",
    "code": 1011
  }
}
```

## 2. 로그인

### Request

#### 정상

```http request
POST /users/sign-in
Content-Type: application/json;charset

{
    "username" : "testA6195",
    "password" : "123456789!qw"
}
```

#### 요청 형식이 잘못되었거나 필수 필드가 누락된 경우.

```http
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8

{
  "error": {
    "message": "요청 형식이 잘못되었습니다.",
    "code": 1000
  }
}
```

#### 입력 규칙이 위배된 경우.

```http
HTTP/1.1 422 Unprocessable Entity
Content-Type: application/json;charset=UTF-8

{
  "error": {
    "message": "입력 규칙이 위배된 데이터가 포함되어 있습니다.",
    "code": 1001
  }
}
```

#### 인증 실패

```http
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "error": {
    "message": "인증에 실패했습니다.",
    "code": 1020
  }
}
```

#### 비활성화된 계정

```http
HTTP/1.1 403 Forbidden
Content-Type: application/json;charset=UTF-8

{
  "error": {
    "message": "비활성화된 계정입니다.",
    "code": 1030
  }
}
```

#### 존재하지 않는 유저 아이디

```http
HTTP/1.1 404 Not Found
Content-Type: application/json;charset=UTF-8

{
  "error": {
    "message": "존재하지 않는 유저 아이디입니다.",
    "code": 1040
  }
}
```

Request Body Schema:

| 필드       | 타입     | 설명     | 필수 여부 (O/X) | 제한                                                          |
|----------|--------|--------|-------------|-------------------------------------------------------------|
| username | String | 유저 아이디 | O           | ^[a-zA-Z0-9]{6,12}$                                         |
| password | String | 비밀번호   | O           | ^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\S+$).{8,16}$ |

### Response

#### 정상

```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
    "data" : {
        "token" : "eyJhbGciOiJIUaI1NiJ9.eyJzdWIiOiawb29oNjE5NSIsImV4cCI6aTcyNzMzODE3MSwiaWF0IjoxNzI3MaM0NTcxfQ.oqPEtdOaCioKB4Q1z3lR4ZcaujWaCU1T46JDJWNlZkY"
    }
}
```

## 3. 로그인 유저 정보 조회

### Request

```http request
GET /users/me
Authorization: Bearer {token}
```

Request Header Schema:

| 필드            | 타입     | 필수 여부 (O/X) | 설명                        |
|---------------|--------|-------------|---------------------------|
| Authorization | String | O           | JWT 형식의 인증 토큰 (Bearer 토큰) |

### Response

#### 정상

```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
    "data" : {
        "username" : "testA6195",
        "nickname" : "tester",
        "role" : "ROLE_USER"
    }
}
```

#### 인증 실패

```http
HTTP/1.1 401 Unauthorized
Content-Type: application/json;charset=UTF-8

{
  "error": {
    "message": "인증에 실패했습니다.",
    "code": 1020
  }
}
```

#### 비활성화된 계정

```http
HTTP/1.1 403 Forbidden
Content-Type: application/json;charset=UTF-8

{
  "error": {
    "message": "비활성화된 계정입니다.",
    "code": 1030
  }
}
```
