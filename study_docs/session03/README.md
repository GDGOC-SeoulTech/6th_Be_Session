# 정기 세션 03 | 트랜잭션 & 게시글 CRUD

> 10월 6일 · 게시글 CRUD, 영속성 컨텍스트·트랜잭션, Swagger

- 세션 자료: [정기 세션 03 | 트랜잭션 & 게시글 CRUD (Notion)](https://app.notion.com/p/3e2b0900718081ceb5bbcf77492dd441)
- 과제 마감: **10월 26일 23:59** (중간고사 휴회 후 세션 04 전날)
- 작업 브랜치: 본인 브랜치 (`yeonghong` / `mingyu` / `chanhaeng`)

세션 중에는 **Notion 자료를 보면서 코드를 따라 칩니다.** 이 문서는 각 단계에서 **어느 파일의 어느 `TODO` 자리에 코드를 쓰고, 어떻게 확인하는지**를 정리한 체크포인트입니다.

## 시작 상태

`main`에는 **세션 02 Practice까지 끝난 코드**가 들어 있습니다. 모두 같은 코드에서 출발합니다.

```
src/main/java/com/gdgoc/board/
├── BoardApplication.java
├── global/
│   ├── common/BaseEntity.java          # createdAt, updatedAt
│   └── config/JpaConfig.java           # @EnableJpaAuditing
└── post/
    ├── controller/PostController.java  # POST /posts, GET /posts         ← TODO 3-1, 3-2, 3-3, 7
    ├── service/PostService.java        # create(), findAll()             ← TODO 3-1, 3-2, 3-3, 7
    ├── repository/PostRepository.java                                    ← TODO 7
    ├── domain/Post.java                                                  ← TODO 3-2
    └── dto/
        ├── PostCreateRequest.java
        └── PostResponse.java
build.gradle                                                              ← TODO 6
```

IntelliJ 하단의 **TODO 탭**(`Alt + 6`)을 열면 코드를 쓸 자리가 모두 모여 보입니다.

---

## STEP 0. 준비 (세션 시작 전)

```bash
git clone https://github.com/GDGOC-SeoulTech/6th_Be_Session.git
cd 6th_Be_Session
git switch yeonghong        # 본인 브랜치
```

1. IntelliJ에서 폴더를 Open → Gradle 로딩이 끝날 때까지 기다립니다.
   - "Enable annotation processing" 알림이 뜨면 **Enable**을 누릅니다. (Lombok)
2. MySQL에 `gdg_board` 데이터베이스가 있는지 확인합니다. (세션 02에서 만든 DB, 없으면 `CREATE DATABASE gdg_board;`)
3. 실행 설정에 환경변수를 넣습니다. (세션 02와 동일)
   - 오른쪽 위 실행 설정 → **Edit Configurations** → `BoardApplication`
   - **Environment variables**: `DB_USERNAME=root;DB_PASSWORD=본인비밀번호`
4. `BoardApplication`을 실행하고 Postman으로 확인합니다.
   - `POST http://localhost:8080/posts` + Body(raw, JSON) `{"title": "첫 번째 게시글", "content": "안녕하세요!"}` → **201**
   - `GET http://localhost:8080/posts` → 방금 쓴 글이 배열로 나오면 준비 완료 ✅

> 이후 단계에서 테스트할 게시글이 필요하니 글을 2~3개 더 저장해 두세요. 제목 하나에는 "스프링"을 넣어 두면 7단계 검색 과제에서 씁니다.

---

## STEP 1. 게시글 단건 조회 · Notion 3-1

| 파일 | 위치 |
| --- | --- |
| `post/service/PostService.java` | `// TODO [세션 03 · 3-1]` |
| `post/controller/PostController.java` | `// TODO [세션 03 · 3-1]` |

**확인**: 서버 재실행 후
- `GET /posts/1` → **200** + 게시글 JSON
- `GET /posts/999` → **500** (없는 글. 예외 처리는 세션 05에서 404로 바꿉니다)

## STEP 2. 게시글 수정 · Notion 3-2

| 파일 | 위치 |
| --- | --- |
| `post/dto/PostUpdateRequest.java` | **새 파일** (`dto` 패키지 우클릭 → New → Java Class → Record) |
| `post/domain/Post.java` | `// TODO [세션 03 · 3-2]` |
| `post/service/PostService.java` | `// TODO [세션 03 · 3-2]` |
| `post/controller/PostController.java` | `// TODO [세션 03 · 3-2]` |

**확인**
- `PATCH /posts/1` + `{"title": "수정된 제목", "content": "수정된 내용"}` → **200** + 바뀐 JSON
- `GET /posts/1` 로 다시 조회해도 바뀐 값이 나오는지 확인

## STEP 3. 게시글 삭제 · Notion 3-3

| 파일 | 위치 |
| --- | --- |
| `post/service/PostService.java` | `// TODO [세션 03 · 3-3]` |
| `post/controller/PostController.java` | `// TODO [세션 03 · 3-3]` |

**확인**
- `DELETE /posts/2` → **204** (응답 본문 없음)
- `GET /posts` 목록에서 2번 글이 사라졌는지 확인

## STEP 4. 더티 체킹 눈으로 확인하기 · Notion 4장

코드를 새로 쓰지 않고, **STEP 2에서 만든 수정 API의 콘솔 로그**를 봅니다.

1. IntelliJ 콘솔을 비웁니다. (콘솔 왼쪽 휴지통 아이콘)
2. `PATCH /posts/1` 을 다시 보냅니다.
3. 콘솔에 `select` 다음 **`update`** 가 찍히는지 확인합니다. `save()`를 호출하지 않았는데도 `update`가 나갑니다.

## STEP 5. `@Transactional` 지워 보기 · Notion 5장

1. `PostService.update()` 위의 `@Transactional` 한 줄을 **주석 처리**하고 서버를 재실행합니다.
2. `PATCH /posts/1` 을 다른 제목으로 보내고, **응답 JSON**과 **콘솔 SQL**을 봅니다.
3. `GET /posts/1` 로 다시 조회합니다. 응답에서 본 값과 같나요?
4. 결과를 `NOTES.md`에 적은 뒤 **`@Transactional` 주석을 다시 풀어 둡니다.**

> 힌트: 클래스에 붙은 `@Transactional(readOnly = true)` 가 이 메서드에 어떤 영향을 줬을까요?

## STEP 6. Swagger 붙이기 · Notion 6장

| 파일 | 위치 |
| --- | --- |
| `build.gradle` | `// TODO [세션 03 · 6]` → 추가 후 🐘 Gradle 새로고침 |

**확인**: 서버 재실행 → http://localhost:8080/swagger-ui/index.html
- `post-controller` 아래 API **5개**가 보이면 성공
- `PATCH /posts/{postId}` → **Try it out** → **Execute** 로 수정 요청 보내 보기

## STEP 7. 게시글 검색 API (Practice · 과제) · Notion 7장

| 파일 | 위치 |
| --- | --- |
| `post/repository/PostRepository.java` | `// TODO [세션 03 · 7]` |
| `post/service/PostService.java` | `// TODO [세션 03 · 7]` |
| `post/controller/PostController.java` | `// TODO [세션 03 · 7]` |

**확인**
- `GET /posts/search?keyword=스프링` → 제목에 "스프링"이 들어간 글만 나오는지
- 콘솔에 `where ... like ?` 가 들어간 `select` 가 찍히는지
- Swagger에 API가 **6개**가 되었는지

> Answer를 펼치기 전에 Hint만 보고 먼저 시도해 보세요.

### (도전) 더 해 보기
- [ ] 제목 **또는** 내용에 keyword가 포함된 게시글 검색
- [ ] keyword 없이 `GET /posts/search` 를 보내면 어떻게 되나요? 어떻게 처리하면 좋을지 `NOTES.md`에 적기

---

## 과제 제출

1. STEP 1 ~ 7을 모두 완성합니다. (세션 중에 못 끝낸 부분 포함)
2. `NOTES.md`의 세션 03 칸에 아래 질문의 답을 적습니다.
3. 커밋하고 본인 브랜치에 push 합니다.

```bash
git add .
git commit -m "[session03] 게시글 CRUD 및 검색 API 구현"
git push origin yeonghong
```

- GitHub Actions 빌드가 초록불(✅)인지 확인해 주세요.
- 중간중간 단계별로 커밋해도 좋습니다. 예: `[session03] 게시글 단건 조회 API`

### 생각해 볼 질문 (`NOTES.md`)

1. 수정 API에서 `save()`를 호출하지 않았는데 `UPDATE` 쿼리가 나가는 이유는 무엇인가요?
2. STEP 5에서 `@Transactional`을 지웠을 때 응답과 DB 값은 각각 어땠나요? 왜 그렇게 되었을까요?
3. 같은 트랜잭션 안에서 `findById(1L)`을 두 번 호출하면 `SELECT` 쿼리는 몇 번 나갈까요? 그 이유는?
4. 같은 클래스 안에서 `@Transactional` 메서드를 호출하면 트랜잭션이 적용되지 않는 이유는 무엇인가요?
