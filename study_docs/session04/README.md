# 정기 세션 04 | 댓글·연관관계, 페이징 & N+1

> 11월 3일 · 댓글·연관관계(`@ManyToOne`), 기본 페이징, N+1 문제

- 세션 자료: [정기 세션 04 | 댓글·연관관계, 페이징 & N+1 (Notion)](https://app.notion.com/p/40db0900718081968211e126bafcf731)
- 작업 브랜치: 본인 브랜치 (`yeonghong` / `mingyu` / `chanhaeng`)

세션 중에는 **Notion 자료를 보면서 코드를 따라 칩니다.** Notion 코드 블록의 **첫 줄 주석이 파일 경로**이고, 파일 안의 `// TODO [세션 04 · 번호]` 자리에 작성합니다. 번호는 Notion 목차 번호와 같습니다. IntelliJ **TODO 탭**(Windows `Alt + 6` · Mac `Cmd + 6`)에서 모든 자리를 한 번에 볼 수 있습니다.

## 시작 상태

`main`에는 **세션 03까지 완성된 코드**와 세션 04용 **댓글 뼈대**가 들어 있습니다.

```
src/main/java/com/gdgoc/board/
├── comment/                                  # 이번 세션에 새로 채우는 패키지
│   ├── domain/Comment.java                   ← TODO 2-4
│   ├── repository/CommentRepository.java    ← TODO 3-2, 3-5, 5-4
│   ├── service/CommentService.java           ← TODO 3-3, 5-3
│   ├── controller/CommentController.java     ← TODO 3-4, 5-3
│   └── dto/                                  # 모두 새 파일 (아래 표 참고)
└── post/
    ├── service/PostService.java              ← TODO 3-5, 4-2 (기존 코드 수정)
    ├── controller/PostController.java        ← TODO 4-2 (기존 코드 수정)
    └── dto/PostPageResponse.java             # 새 파일
```

## STEP 0. 준비

```bash
git switch yeonghong      # 본인 브랜치
git pull origin main      # 세션 04 TODO 가 들어간 코드 받기 → Gradle 새로고침
```

- MySQL 을 켜고 서버가 정상 실행되는지 확인합니다. (`DB_USERNAME` · `DB_PASSWORD` 환경변수는 세션 03과 같음)
- Swagger(http://localhost:8080/swagger-ui/index.html)에서 `POST /posts` 로 게시글을 **3개 이상** 만들어 둡니다.
- 오늘은 **콘솔의 SQL 로그를 보는 것**이 핵심입니다.

---

## STEP 1. 댓글 작성 · 조회 · 삭제 · Notion 2-4, 3-2 ~ 3-4

작성 순서: **2-4 → 3-2 → 3-3 → 3-4**

| Notion | 파일 | 위치 |
| --- | --- | --- |
| 2-4 | `comment/domain/Comment.java` | `// TODO [세션 04 · 2-4]` |
| 3-2 | `comment/repository/CommentRepository.java` | `// TODO [세션 04 · 3-2]` |
| 3-2 | `comment/dto/CommentCreateRequest.java` | **새 파일** (record) |
| 3-2 | `comment/dto/CommentResponse.java` | **새 파일** (record) |
| 3-3 | `comment/service/CommentService.java` | `// TODO [세션 04 · 3-3]` |
| 3-4 | `comment/controller/CommentController.java` | `// TODO [세션 04 · 3-4]` |

**확인**: 서버 재실행 후 Swagger 에서 차례로
- `POST /posts/1/comments` + `{"content": "첫 댓글"}` → **201**, 응답에 `postId: 1`, 콘솔에 `select` 다음 `insert`
- 같은 요청으로 댓글 2개 더 작성 → **201**
- `GET /posts/1/comments` → **200**, 댓글 3개, 콘솔 select 에 `where post_id=?`
- `POST /posts/999/comments` → **500** (세션 05에서 404로 바꿉니다)
- `DELETE /posts/1` → **500**, 콘솔에 `foreign key constraint fails` ← 다음 단계에서 해결

## STEP 2. 댓글이 달린 게시글 삭제 · Notion 3-5

| 파일 | 위치 |
| --- | --- |
| `comment/repository/CommentRepository.java` | `// TODO [세션 04 · 3-5]` |
| `post/service/PostService.java` | `// TODO [세션 04 · 3-5]` ×2 (필드 추가 + 기존 `delete()` 수정) |

**확인**: `DELETE /posts/1` → **204**, 콘솔에 댓글 delete 들 다음 게시글 delete

> 1번 게시글을 지웠다면 STEP 3, 4를 위해 게시글을 하나 더 만들어 두세요.

## STEP 3. 게시글 목록 페이징 · Notion 4-2

| 파일 | 위치 |
| --- | --- |
| `post/dto/PostPageResponse.java` | **새 파일** (record) |
| `post/service/PostService.java` | `// TODO [세션 04 · 4-2]` (기존 `findAll()` 수정) |
| `post/controller/PostController.java` | `// TODO [세션 04 · 4-2]` (기존 `findAll()` 수정) |

**확인** (게시글 3개 이상)
- `GET /posts?page=0&size=2` → 게시글 2개, `hasNext: true`, 콘솔에 `limit` select + `count` select
- `GET /posts?page=1&size=2` → 나머지 게시글
- `GET /posts` → 기본값(크기 10, 최신순)
- 응답이 배열에서 **객체**로 바뀝니다. (정상)

> `size=10` 으로 보내면 게시글이 3개뿐일 때 count 쿼리가 생략됩니다. 확인은 `size=2` 로 하세요.

## STEP 4. N+1 확인과 fetch join 해결 · Notion 5-3, 5-4

**4-1. N+1 확인 (5-3)**

| 파일 | 위치 |
| --- | --- |
| `comment/dto/CommentWithPostResponse.java` | **새 파일** (record) |
| `comment/service/CommentService.java` | `// TODO [세션 04 · 5-3]` |
| `comment/controller/CommentController.java` | `// TODO [세션 04 · 5-3]` |

**확인**: **서로 다른 게시글 3개**에 댓글을 하나씩 달고 `GET /comments` → 콘솔에 comment select 1번 + post select **3번**

**4-2. fetch join 으로 해결 (5-4)**

| 파일 | 위치 |
| --- | --- |
| `comment/repository/CommentRepository.java` | `// TODO [세션 04 · 5-4]` |
| `comment/service/CommentService.java` | 5-3 코드에서 `findAll()` → `findAllWithPost()` 한 곳 수정 |

**확인**: 재실행 후 `GET /comments` → `join` 이 들어간 select **1번**

---

## 과제

**Mission**: `GET /posts/{postId}/comments?page=0&size=5` 가 해당 게시글의 댓글을 **오래된 순**으로 5개씩 돌려주고, 응답에 `page` · `totalPages` · `totalElements` · `hasNext` 가 포함되도록 완성하세요.

**선택**: `POST /posts/{postId}/like` 로 게시글 좋아요 수를 1 증가시키고, 게시글 응답에 `likeCount` 를 포함하세요.

> Hint 는 Notion 6장에 있습니다. Answer 를 펼치기 전에 먼저 시도해 보세요.

## 과제 제출

```bash
git add .
git commit -m "[session04] 댓글 API 및 페이징 구현"
git push origin yeonghong
```

- 커밋 메시지 앞에 `[session04]` 를 붙입니다.
- GitHub Actions 빌드가 초록불(✅)인지 확인해 주세요.
- `NOTES.md` 의 세션 04 칸에 회고와 막힌 부분을 남겨 주세요.
