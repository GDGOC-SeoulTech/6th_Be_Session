# 정기 세션 05 | 유효성 검사, 예외 처리 & 테스트

> 유효성 검사(`@Valid`), 전역 예외 처리(`@RestControllerAdvice`), JUnit · Mockito · MockMvc 테스트

- 세션 자료: [정기 세션 05 | 유효성 검사, 예외 처리 & 테스트 (Notion)](NOTION_LINK)
- 작업 브랜치: 본인 브랜치 (`yeonghong` / `mingyu` / `chanhaeng`)

세션 중에는 Notion 자료로 흐름을 함께 읽고, **실습 ① · ② 는 수업 후** 진행합니다. Notion 코드 블록의 **첫 줄 주석이 파일 경로**이고, 파일 안의 `// TODO [세션 05 · 번호]` 자리에 작성합니다. 번호는 Notion 목차 번호와 같습니다. IntelliJ **TODO 탭**(Windows `Alt + 6` · Mac `Cmd + 6`)에서 모든 자리를 한 번에 볼 수 있습니다.

## 시작 상태

`main`에는 **세션 04 까지 완성된 코드**(과제 · 선택 과제 포함)와 세션 05 용 **TODO** 가 들어 있습니다.

```
build.gradle                                  ← TODO 3-1, 5-2
src/main/java/com/gdgoc/board/
├── global/
│   ├── common/ApiResponse.java               # 새 파일 (4-4)
│   └── exception/                            # 새 패키지
│       ├── ErrorCode.java                    # 새 파일 (4-2)
│       ├── CustomException.java              # 새 파일 (4-3)
│       └── GlobalExceptionHandler.java       # 새 파일 (4-5)
├── post/
│   ├── dto/PostCreateRequest.java            ← TODO 3-2
│   ├── dto/PostUpdateRequest.java            ← TODO 3-2
│   ├── service/PostService.java              ← TODO 4-3
│   └── controller/PostController.java        ← TODO 3-3, 4-4
└── comment/
    ├── dto/CommentCreateRequest.java         ← TODO 3-2
    ├── service/CommentService.java           ← TODO 4-3 ×2
    └── controller/CommentController.java     ← TODO 3-3, 4-4
src/test/java/com/gdgoc/board/
├── post/service/PostServiceTest.java         # 새 파일 (5-3)
├── post/controller/PostControllerTest.java   # 새 파일 (5-4)
└── comment/service/CommentServiceTest.java   # 새 파일 (과제)
```

## STEP 0. 준비

```bash
git switch yeonghong      # 본인 브랜치
git pull origin main      # 세션 05 TODO 가 들어간 코드 받기 → Gradle 새로고침
```

- MySQL 을 켜고 서버가 정상 실행되는지 확인합니다. (`DB_USERNAME` · `DB_PASSWORD` 환경변수는 이전과 같음)
- 게시글 1개 이상, 댓글 1개 이상을 준비합니다.
- Swagger(http://localhost:8080/swagger-ui/index.html)에서 `GET /posts/9999` → **500** 확인. 오늘 이것을 404 로 바꿉니다.

> 세션 04 를 본인 방식으로 다르게 구현했다면 `git pull` 에서 충돌이 날 수 있습니다. 충돌 난 파일은 `main` 쪽 코드를 기준으로 맞춘 뒤 진행하세요.

---

## STEP 1. 유효성 검사 · Notion 3-1 ~ 3-3

작성 순서: **3-1 → 3-2 → 3-3**

| Notion | 파일 | 위치 |
| --- | --- | --- |
| 3-1 | `build.gradle` | `// TODO [세션 05 · 3-1]` → 작성 후 Gradle 새로고침 |
| 3-2 | `post/dto/PostCreateRequest.java` | `// TODO [세션 05 · 3-2]` |
| 3-2 | `post/dto/PostUpdateRequest.java` | `// TODO [세션 05 · 3-2]` |
| 3-2 | `comment/dto/CommentCreateRequest.java` | `// TODO [세션 05 · 3-2]` |
| 3-3 | `post/controller/PostController.java` | `// TODO [세션 05 · 3-3]` (`create()` · `update()`) |
| 3-3 | `comment/controller/CommentController.java` | `// TODO [세션 05 · 3-3]` (`create()`) |

**확인**: 서버 재실행 후 `POST /posts` 에 `{"title": "", "content": "내용"}` → **400** (응답 본문은 아직 Spring 기본 형식. STEP 2 에서 바뀝니다)

## STEP 2. 전역 예외 처리 · Notion 4-2 ~ 4-5

작성 순서: **4-2 → 4-3 → 4-4 → 4-5**

| Notion | 파일 | 위치 |
| --- | --- | --- |
| 4-2 | `global/exception/ErrorCode.java` | **새 파일** (enum) |
| 4-3 | `global/exception/CustomException.java` | **새 파일** |
| 4-3 | `post/service/PostService.java` | `// TODO [세션 05 · 4-3]` (`getPost()` 한 곳) |
| 4-3 | `comment/service/CommentService.java` | `// TODO [세션 05 · 4-3]` ×2 (`create()` · `delete()`) |
| 4-4 | `global/common/ApiResponse.java` | **새 파일** (record) |
| 4-4 | `post/controller/PostController.java` | `// TODO [세션 05 · 4-4]` (`delete()` 제외 전부) |
| 4-4 | `comment/controller/CommentController.java` | `// TODO [세션 05 · 4-4]` (`delete()` 제외 전부) |
| 4-5 | `global/exception/GlobalExceptionHandler.java` | **새 파일** |

**확인**: 서버 재실행 후 Swagger 에서 차례로

- `GET /posts` → **200**, `success: true`, `data` 안에 `posts`
- `GET /posts/9999` → **404**, "게시글을 찾을 수 없습니다."
- `POST /posts` + `{"title": "", "content": "내용"}` → **400**, "제목은 필수입니다."
- `POST /posts` + `{"title": "제목" "content": "내용"}` (쉼표 빠짐) → **400**, "잘못된 입력입니다."
- `POST /posts/999/comments` + `{"content": "댓글"}` → **404** (세션 04 에서 500)
- `POST /posts/{있는 번호}/comments` + `{"content": ""}` → **400**, "댓글 내용은 필수입니다."
- `DELETE /comments/9999` → **404**, "댓글을 찾을 수 없습니다."
- `GET /posts/abc` → **500** (정상. 선택 과제에서 400 으로 바꿉니다)

## STEP 3. 테스트 코드 · Notion 5-2 ~ 5-4

작성 순서: **5-2 → 5-3 → 5-4**

| Notion | 파일 | 위치 |
| --- | --- | --- |
| 5-2 | `build.gradle` | `// TODO [세션 05 · 5-2]` → 작성 후 Gradle 새로고침 |
| 5-3 | `src/test/.../post/service/PostServiceTest.java` | **새 파일** |
| 5-4 | `src/test/.../post/controller/PostControllerTest.java` | **새 파일** |

**확인**

- 테스트 클래스 왼쪽 ▶ 실행 → `PostServiceTest` 1개, `PostControllerTest` 2개 통과
- 터미널 `./gradlew test` → `BoardApplicationTests` 포함 전체 통과 (MySQL 꺼져 있어도 통과)

> 실행 중 `Mockito is currently self-attaching ...` 경고는 JDK 21 의 경고이며 테스트 결과와 무관합니다.

---

## 과제

**Mission**: 없는 게시글에 댓글을 작성하면 `POST_NOT_FOUND` 예외가 발생하는지 확인하는 `CommentService` 단위 테스트를 `src/test/java/com/gdgoc/board/comment/service/CommentServiceTest.java` 에 작성하세요.

**선택**: `GET /posts/abc` 는 **400**, 없는 주소 `GET /nope` 는 **404** 로 응답하도록 `GlobalExceptionHandler` 를 보완하세요.

> Hint 는 Notion 6장에 있습니다. Answer 를 펼치기 전에 먼저 시도해 보세요.

## 과제 제출

```bash
git add .
git commit -m "[session05] 유효성 검사, 예외 처리, 테스트 구현"
git push origin yeonghong
```

- 커밋 메시지 앞에 `[session05]` 를 붙입니다.
- GitHub Actions 빌드가 초록불(✅)인지 확인해 주세요. 이번 세션부터 **직접 작성한 테스트도 Actions 에서 함께 실행**됩니다.
- `NOTES.md` 의 세션 05 칸에 회고와 막힌 부분을 남겨 주세요.
