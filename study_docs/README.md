# study_docs

정기 세션별 과제 명세를 모아 둡니다. 각 세션 폴더의 `README.md`에 해당 주차 구현 범위와 체크리스트가 있습니다.

## 커리큘럼

| 주차 | 날짜 | 세션 | 과제 |
| --- | --- | --- | --- |
| 1주차 | 9/22 | 정기 세션 01: Backend & Spring Boot 이해, 개발 환경 세팅과 첫 API 만들기 | 실습: `/introduce` API |
| 2주차 | 9/29 | 정기 세션 02: IoC·DI·Spring Bean, 계층형 아키텍처, DB·JPA 핵심 원리 | |
| 3주차 | 10/6 | [정기 세션 03](./session03): 게시글 CRUD, 영속성 컨텍스트·트랜잭션, Swagger | 게시글 CRUD 직접 구현, 게시글 검색 API |
| 4주차 | 10/13 | _중간고사 휴회_ | |
| 5주차 | 10/20 | _중간고사 휴회_ | |
| 6주차 | 10/27 | 정기 세션 04: 댓글·연관관계, 기본 페이징, N+1 문제 | 댓글 조회 API (선택: 단순 좋아요 수 증가) |
| 7주차 | 11/3 | 정기 세션 05: 유효성 검사, 예외 처리, JUnit 테스트 | |
| 8주차 | 11/10 | 정기 세션 06: 회원가입·로그인, Spring Security·JWT 인증 및 필터 | 선택: 본인 글만 수정·삭제 |
| 9주차 | 11/17 | 정기 세션 07: Docker·EC2 배포, CORS, 회고 | 마지막 세션! |

## 사전 준비

- [MySQL 8.4 LTS 설치·접속](./setup/MYSQL.md)
- [세션 03 체크포인트](./session03/README.md) · JDK 21, 게시글 1·2·3 준비

## 세션 폴더 규칙

```
study_docs/
└── sessionXX/
    └── README.md    # Notion 자료 링크, 단계별 체크포인트(파일·TODO 위치·확인 방법), 과제, 제출 방법
```

- 세션 03부터 멤버들은 본인 브랜치(`yeonghong`, `mingyu`, `chanhaeng`)의 프로젝트를 **계속 이어서** 구현합니다.
- 코드 원문은 Notion 세션 자료에만 두고, 세션 README는 "어느 파일의 어느 TODO에 쓰고 어떻게 확인하는지"만 안내합니다.
- 세션 시작 지점마다 `sessionXX-start` 태그를 답니다. → `git diff session03-start origin/본인브랜치 -- src`
