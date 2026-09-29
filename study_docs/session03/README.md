# 정기 세션 03 | 트랜잭션 & 게시글 CRUD

> 10월 6일 · 게시글 CRUD, 영속성 컨텍스트·트랜잭션, Swagger

- 세션 자료: _(링크 추가 예정)_
- 마감: **10월 26일 23:59** (중간고사 휴회 후 세션 04 전날)
- 작업 브랜치: 본인 브랜치 (`yeonghong` / `mingyu` / `chanhaeng`)

## 과제

1. **세션 자료의 코드를 본인 브랜치에서 직접 구현하기**: 게시글 CRUD API
2. **게시글 검색 API 추가하기**: 자료에 없는 기능을 스스로 구현
3. **`NOTES.md`에 회고 작성하기**: 아래 [생각해 볼 질문](#생각해-볼-질문)에 답하기

## 패키지 구조 (권장)

```
src/main/java/com/gdgoc/board/
├── BoardApplication.java
└── post/
    ├── Post.java                # Entity
    ├── PostRepository.java      # JpaRepository
    ├── PostService.java         # 비즈니스 로직 + 트랜잭션
    ├── PostController.java      # API
    └── dto/
        ├── PostCreateRequest.java
        ├── PostUpdateRequest.java
        └── PostResponse.java
```

세션 04 댓글(`comment/`), 세션 06 회원(`member/`)도 같은 방식으로 기능별 패키지를 추가해 나갑니다.

## API 명세

멤버끼리 구현을 비교할 수 있도록 **URL과 요청·응답 형태는 아래 명세를 따릅니다.** 내부 구현 방식은 자유입니다.

| 기능 | Method | URL | 요청 Body | 응답 |
| --- | --- | --- | --- | --- |
| 게시글 작성 | `POST` | `/posts` | `{ "title", "content", "author" }` | `201` + `PostResponse` |
| 게시글 목록 조회 | `GET` | `/posts` | | `200` + `PostResponse[]` |
| 게시글 단건 조회 | `GET` | `/posts/{id}` | | `200` + `PostResponse` |
| 게시글 수정 | `PUT` | `/posts/{id}` | `{ "title", "content" }` | `200` + `PostResponse` |
| 게시글 삭제 | `DELETE` | `/posts/{id}` | | `204` |
| **게시글 검색 (과제)** | `GET` | `/posts/search?keyword={keyword}` | | `200` + `PostResponse[]` |

`PostResponse` 예시:

```json
{
  "id": 1,
  "title": "첫 글",
  "content": "안녕하세요",
  "author": "yeonghong"
}
```

> 존재하지 않는 `id`로 요청할 때의 처리는 지금은 `IllegalArgumentException`을 던지는 정도로 둡니다. 제대로 된 예외 처리는 세션 05에서 다룹니다.

## 구현 체크리스트

### 게시글 CRUD (세션 자료)
- [ ] `Post` 엔티티: `@Entity`, `@Id`, `@GeneratedValue`, `protected` 기본 생성자
- [ ] `PostRepository`: `JpaRepository<Post, Long>` 상속
- [ ] `PostService`: 클래스에 `@Transactional(readOnly = true)`, 쓰기 메서드에 `@Transactional`
- [ ] 수정 로직은 `save()`를 호출하지 않고 **변경 감지(Dirty Checking)** 로 처리
- [ ] Controller는 엔티티를 그대로 반환하지 않고 **DTO로 응답**
- [ ] Swagger UI(`/swagger-ui/index.html`)에서 모든 API 호출해 보기

### 게시글 검색 (과제)
- [ ] `GET /posts/search?keyword=spring` 요청 시 **제목에 keyword가 포함된** 게시글 목록 반환
- [ ] (도전) 제목 **또는** 내용에 keyword가 포함된 게시글 검색
- [ ] (도전) keyword가 비어 있으면 어떻게 처리할지 정하고 `NOTES.md`에 이유 적기

> 힌트: Spring Data JPA의 **쿼리 메서드** 이름 규칙을 찾아보세요. (`findBy...`)

## 생각해 볼 질문

`NOTES.md`의 세션 03 칸에 짧게 답을 적어 주세요. 정답보다 **직접 확인해 본 과정**이 중요합니다.

1. 수정 API에서 `save()`를 호출하지 않았는데 콘솔에 `UPDATE` 쿼리가 찍히는 이유는 무엇인가요?
2. `PostService`의 수정 메서드에서 `@Transactional`을 지우면 어떻게 되나요? 직접 지워 보고 결과를 적어 주세요.
3. 같은 트랜잭션 안에서 `findById(1L)`을 두 번 호출하면 `SELECT` 쿼리는 몇 번 나가나요? 그 이유는?
4. `@Transactional(readOnly = true)`는 무엇을 해 주나요?

## 제출

```bash
git switch yeonghong
git pull origin main           # 이 과제 명세 받아오기
# ... 구현 ...
git add .
git commit -m "[session03] 게시글 CRUD 구현"
git push origin yeonghong
```

- GitHub Actions 빌드가 초록불(✅)인지 확인해 주세요.
- 마감 후 `example` 브랜치에 예시 코드가 올라오면 본인 코드와 비교해 보세요. (루트 README의 [코드 비교하기](../../README.md#코드-비교하기) 참고)
