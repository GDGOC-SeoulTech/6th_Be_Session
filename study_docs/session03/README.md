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

IntelliJ 하단의 **TODO 탭**(Windows: `Alt + 6`, macOS: `Command + 6`)을 열면 코드를 쓸 자리가 모두 모여 보입니다.

### 코드를 작성할 때 공통으로 확인할 것

1. Notion 코드 블록 첫 줄의 파일 경로를 보고, 해당 파일의 TODO 자리에 **메서드를 클래스 안에** 작성합니다. 기존 `create()`와 `findAll()`은 함께 사용합니다.
2. 새 파일은 IntelliJ에서 `com.gdgoc.board.post.dto` 패키지를 우클릭해 만듭니다. `PostUpdateRequest.java` 첫 줄은 `package com.gdgoc.board.post.dto;`입니다.
3. 타입이나 어노테이션에 빨간 밑줄이 생기면 `Alt + Enter`(macOS: `Option + Enter`)로 **Import class**를 선택합니다. 주요 import는 아래와 같습니다.

   | 작성 위치 | 필요한 import |
   | --- | --- |
   | Controller의 조회·수정·삭제·검색 | `org.springframework.web.bind.annotation.PathVariable`, `PatchMapping`, `DeleteMapping`, `RequestParam` — 각각 별도 import |
   | Service·Controller의 수정 | `com.gdgoc.board.post.dto.PostUpdateRequest` |
   | Repository의 검색 | `java.util.List` |
   | Service의 트랜잭션 | `org.springframework.transaction.annotation.Transactional` (`readOnly`를 지원하는 Spring 어노테이션) |

4. 각 단계의 파일을 모두 작성한 뒤 실행 중인 서버를 중지하고 `BoardApplication`을 다시 실행합니다. 빨간 밑줄이나 빌드 오류가 남아 있으면 해당 단계의 파일·package·import부터 확인합니다.
5. 코드 변경 후에는 **서버 재실행 → 요청 → 재조회** 순서로 확인합니다. 임시 실험은 **원래 코드로 복구 → 서버 재실행 → 재조회**까지 마칩니다.

> Notion의 `...`가 들어간 코드는 개념을 설명하는 일부 예시입니다. 실제 구현은 3-1·3-2·3-3·7의 TODO용 코드 블록을 따라 작성하세요. `build.gradle`의 Swagger 한 줄은 이미 있는 `dependencies { ... }` 안에 넣습니다.

---

## STEP 0. 준비 (세션 시작 전)

**기준 환경은 JDK 21 · Spring Boot 4.1.1 · Gradle Wrapper 8.14.3입니다.** MySQL 8.4 LTS는 로컬에서 실행합니다. 설치하지 않았다면 [MySQL 설치·접속 가이드](../setup/MYSQL.md)를 먼저 완료하세요. Java 버전을 바꾸느라 실습 시간이 소모되지 않도록 미리 확인하세요.

처음 받는 경우:

```bash
git clone https://github.com/GDGOC-SeoulTech/6th_Be_Session.git
cd 6th_Be_Session
git switch yeonghong        # 본인 브랜치: yeonghong / mingyu / chanhaeng
git merge origin/main
```

이미 받은 경우 (작성하던 변경을 먼저 커밋한 뒤):

```bash
git switch yeonghong        # 본인 브랜치
git fetch origin
git merge origin/main
```

충돌이 나면 강제로 덮어쓰지 말고 진행자에게 보여 주세요. `main`으로 바꾸고 실습하면 본인 브랜치에 작성한 코드가 사라진 것처럼 보일 수 있으니, 현재 브랜치를 먼저 확인합니다.

1. IntelliJ에서 폴더를 Open하고 Gradle 로딩을 기다립니다.
   - **Project SDK: 21**, **Gradle JVM: 21**로 맞춥니다. JDK가 없으면 Download JDK로 21을 설치합니다.
   - Gradle JVM은 Settings → Build, Execution, Deployment → Build Tools → Gradle에서, 실행 JRE는 Edit Configurations → BoardApplication에서 확인합니다.
   - 터미널로 실행한다면 `java -version`과 `./gradlew --version`(Windows: `.\gradlew.bat --version`)에서도 Java 21인지 확인합니다. 다르면 `JAVA_HOME`과 PATH의 Java 경로를 JDK 21로 맞춘 뒤 터미널을 다시 엽니다.
   - Lombok의 "Enable annotation processing" 알림은 **Enable**을 누릅니다.
2. 터미널에서 사전 점검을 합니다. MySQL 없이도 실행할 수 있습니다.
   ```bash
   ./gradlew prepareSession
   # Windows PowerShell: .\gradlew.bat prepareSession
   ```
   `BUILD SUCCESSFUL`을 확인합니다. 빌드·H2 테스트와 Swagger 라이브러리 다운로드를 미리 끝내는 작업입니다. **이것만 통과했다고 MySQL 연결까지 확인된 것은 아닙니다.** Swagger는 STEP 6의 의존성을 실제로 추가해야 켜집니다.
3. MySQL이 실행 중인지 확인하고 **세션 실습용 로컬 DB**를 새로 만듭니다. 아래 명령은 `gdg_board`의 기존 데이터를 삭제합니다. 보관할 실습 데이터가 있다면 먼저 백업한 뒤 실행하세요. 다른 프로젝트나 공용 DB에는 실행하지 않습니다.
   ```sql
   DROP DATABASE IF EXISTS gdg_board;
   CREATE DATABASE gdg_board CHARACTER SET utf8mb4;
   ```
4. 실행 설정 → **Edit Configurations** → `BoardApplication`의 **Environment variables**에 `DB_USERNAME=root;DB_PASSWORD=본인비밀번호`를 넣습니다.
5. `BoardApplication`을 실행합니다. 서버가 8080 포트로 시작되고, 아래 POST와 GET이 모두 성공해야 준비 완료입니다.

### 실습 데이터 세 개 준비

Postman에서 Body → raw → **JSON**으로 아래 요청을 각각 보냅니다. 주소는 모두 `POST http://localhost:8080/posts`이고 **201**이 나와야 합니다.

| 역할 | 요청 본문 | 기록할 값 |
| --- | --- | --- |
| 조회·수정용 | `{"title":"첫 번째 게시글","content":"안녕하세요!"}` | id **1** → `postId` |
| 검색용 | `{"title":"스프링 공부","content":"JPA"}` | id **2** → `searchId`, 삭제하지 않음 |
| 삭제용 | `{"title":"세 번째","content":"삭제용"}` | id **3** → `deleteId` |

`GET http://localhost:8080/posts`에서 세 글을 확인하세요. **노션과 같은 번호로 진행하도록 id 1·2·3인지 확인합니다.** 번호가 다르면 POST를 더 보내지 말고 DB 초기화와 요청 순서를 점검하세요. 번호만 다르게 실습하기로 했다면 아래 예시의 1과 3을 실제 응답 번호로 바꿉니다.

Postman에서는 [요청 모음](./session03.postman_collection.json)을 Import하면 번호를 자동으로 기록할 수 있습니다. DB 초기화는 요청 모음이 수행하지 않습니다. `00 사전 준비` 폴더를 순서대로 실행하세요. 세션 시작 코드에서는 이 폴더만 실행합니다. `01 CRUD 확인`은 STEP 1~3과 Swagger(STEP 6)를 완성한 후, `02 검색 과제 확인`은 STEP 7을 완성한 후 실행합니다. 전체를 한꺼번에 실행하지 마세요.

> 세션 당일에는 노트북·충전기, Notion, Postman을 준비합니다. 에러가 나면 가장 위의 원인 메시지와 실행 설정을 캡처해 진행자에게 보여 주세요.

---

## 용어를 읽는 기준 · Notion 1-1

**핵심 용어**

- **Domain** = 소프트웨어가 적용되는 주제 영역. 이 예제의 게시글 작성·조회·관리가 해당 영역입니다.
- **JPA Entity** = 영속성을 갖는 도메인 객체. Entity 인스턴스가 항상 JPA의 관리 상태인 것은 아닙니다.
- **DTO** = 데이터를 묶어 전달하는 객체. DTO가 반드시 record이거나 불변이어야 하는 것은 아닙니다.

정의 근거: [Eric Evans · DDD Reference](https://www.domainlanguage.com/wp-content/uploads/2016/05/DDD_Reference_2015-03.pdf), [Jakarta Persistence 명세](https://jakarta.ee/specifications/persistence/3.2/jakarta-persistence-spec-3.2), [Martin Fowler · DTO](https://martinfowler.com/eaaCatalog/dataTransferObject.html).

**부차 설명**: `domain`과 `dto`는 이 프로젝트에서 정한 패키지 이름입니다. `Post`는 게시글의 값·동작과 JPA 매핑을 담고, 요청·응답 DTO는 record로 구현합니다. 전체 용어 정의와 예제 코드는 노션에서 확인합니다.

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
- `PATCH /posts/1` + `{"title":"제목만 수정"}` → 제목만 변경되고 기존 content가 유지되는지 확인
- 제목만 수정한 뒤 `GET /posts/1`로도 기존 content가 유지되는지 확인

## STEP 3. 게시글 삭제 · Notion 3-3

| 파일 | 위치 |
| --- | --- |
| `post/service/PostService.java` | `// TODO [세션 03 · 3-3]` |
| `post/controller/PostController.java` | `// TODO [세션 03 · 3-3]` |

**확인**
- `DELETE /posts/3` → **204** (응답 본문 없음)
- `GET /posts` 목록에서 삭제용 글이 사라졌는지 확인

## STEP 4. 더티 체킹 눈으로 확인하기 · Notion 4장

코드를 새로 쓰지 않고, **STEP 2에서 만든 수정 API의 콘솔 로그**를 봅니다.

1. IntelliJ 콘솔을 비웁니다. (콘솔 왼쪽 휴지통 아이콘)
2. `PATCH /posts/1` + `{"title":"변경 감지 확인"}`처럼 **현재 값과 다른 제목**을 보냅니다. 같은 값을 반복해서 보내면 변경이 없어 UPDATE가 안 나올 수 있습니다.
3. 콘솔에 `select` 다음 **`update`** 가 찍히는지 확인합니다. `save()`를 호출하지 않았는데도 `update`가 나갑니다.

Notion 4-4의 1차 캐시 실험을 했다면 `findById()`를 원래 코드로 복구하고 서버를 재실행한 뒤 다음 단계로 넘어갑니다. `updatedAt` 비교는 선택 실험입니다.

## STEP 5. `@Transactional` 지워 보기 · Notion 5장

1. **클래스 위의 `@Transactional(readOnly = true)`는 그대로 두고**, `PostService.update()` 위의 `@Transactional` 한 줄만 **주석 처리**합니다. 서버를 재실행합니다.
2. `PATCH /posts/1` 을 다른 제목으로 보내고, **응답 JSON**과 **콘솔 SQL**을 봅니다.
3. `GET /posts/1` 로 다시 조회합니다. 응답에서 본 값과 같나요?
4. 결과를 `NOTES.md`에 적은 뒤 **`@Transactional` 주석을 다시 풀고 서버를 재실행합니다.** 다른 제목으로 PATCH 후 GET해 이번에는 DB에 반영되는지 확인하세요.

> 이 실험에서는 PATCH 응답은 바뀐 값이지만, UPDATE SQL은 없고 다시 GET하면 원래 값이어야 합니다.
>
> 힌트: 클래스에 붙은 `@Transactional(readOnly = true)` 가 이 메서드에 어떤 영향을 줬을까요?

### 롤백 확인 · Notion 5-4

1. `update()`의 `@Transactional`을 복원한 상태에서 Notion의 **RuntimeException 롤백 실험 코드**를 잠깐 작성하고 서버를 재실행합니다.
2. `PATCH /posts/1` + `{"title":"롤백 확인"}`을 보내면 **500**이 나옵니다. 콘솔에 UPDATE가 없는지 보고, `GET /posts/1`에서 실험 전 제목이 유지되는지 확인합니다.
3. 실험용 `if (true) { throw ...; }`를 제거하고 서버를 재실행합니다. 새 제목으로 PATCH 후 GET해 정상 수정으로 돌아왔는지 확인합니다.

체크 예외 비교는 선택 실험입니다. 시도했다면 Service·Controller에 추가한 `throws Exception`도 복구합니다. 500 응답만으로 롤백됐다고 판단하지 말고 GET 결과까지 확인하세요.

## STEP 6. Swagger 붙이기 · Notion 6장

| 파일 | 위치 |
| --- | --- |
| `build.gradle` | `// TODO [세션 03 · 6]` → 추가 후 🐘 Gradle 새로고침 |

의존성은 `implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:3.1.1'`입니다. `session03Preparation`은 미리 다운로드만 하는 설정이므로, **implementation 줄도 별도로 추가(주석 해제)**해야 합니다. 시간이 걸리는 다운로드는 STEP 0에서 미리 마쳐 주세요.

**확인**: Gradle 새로고침 후 서버 재실행 → http://localhost:8080/swagger-ui/index.html
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
4. 같은 객체 안에서 메서드를 직접 호출하면 그 메서드의 `@Transactional` 설정이 따로 적용되지 않는 이유는 무엇인가요? 호출한 메서드에서 이미 시작한 트랜잭션이 있다면 어떻게 될까요?


## 진행자 마지막 점검

- [ ] 멤버 모두 본인 브랜치에 최신 main을 가져왔다.
- [ ] 사전 prepareSession이 성공했고, MySQL에서 POST 201·GET 200을 확인했다.
- [ ] 게시글 1(조회·수정)·2(검색)·3(삭제)을 확인했다. 검색용 글은 삭제하지 않는다.
- [ ] STEP 4와 5는 매번 현재 값과 다른 제목을 보낸다.
- [ ] STEP 5 후 update의 @Transactional을 복원하고 재실행·재조회했다.
- [ ] STEP 6에서는 implementation 의존성을 추가하고 Gradle 새로고침했다.
- [ ] 같은 객체 조회 실험 등 임시 코드는 원래대로 복원했다.

없는 글 요청의 500은 현재 예제의 예외 처리 단계에 따른 결과입니다. 정상 서비스의 권장 응답이라는 뜻은 아닙니다. 세션 05에서 404로 바꿉니다.
