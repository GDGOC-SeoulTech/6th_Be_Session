# 6th_Be_Session

GDGoC SeoulTech 6기 BE Session. 백엔드 파트 정기 세션 자료와 멤버별 실습 코드를 모아 두는 레포입니다.

- 세션 자료 · 과제 명세: [`study_docs/`](./study_docs)
- 멤버 실습 코드: 멤버별 브랜치

## 브랜치 구조

| 브랜치 | 용도 | 누가 수정하나 |
| --- | --- | --- |
| `main` | 원본 코드(스켈레톤) + 세션 자료·과제 명세 | 코어 멤버 |
| `example` | 세션별 예시 코드 (과제 마감 후 생성·공개) | 코어 멤버 |
| `yeonghong` | 영홍 실습 코드 | 영홍 |
| `mingyu` | 민규 실습 코드 | 민규 |
| `chanhaeng` | 찬행 실습 코드 | 찬행 |

- 멤버는 **본인 브랜치 하나를 9주 내내** 사용합니다. 세션 03의 게시글 CRUD 위에 세션 04 댓글, 세션 06 회원·인증을 계속 쌓아 올려 **하나의 게시판 프로젝트**를 완성합니다.
- 멤버 브랜치는 `main`에 머지하지 않습니다.
- 예시 코드는 `main`이 아니라 `example` 브랜치에 올립니다. 그래야 멤버가 `main`을 머지해서 새 세션 자료를 받아도 본인 코드와 충돌하지 않습니다.

## 개발 환경

| 항목 | 버전 |
| --- | --- |
| Java | 17 |
| Spring Boot | 4.1.1 |
| Gradle | 8.14.3 (Wrapper 포함) |
| DB | MySQL (`gdg_board`), 테스트는 H2 |
| 라이브러리 | Spring Web MVC, Spring Data JPA, Lombok |
| 기본 패키지 | `com.gdgoc.board` |

모든 멤버가 같은 코드에서 출발하도록 `main`에는 **직전 세션까지 완성된 코드**와 이번 세션에서 작성할 자리를 표시한 `// TODO [세션 XX · 번호]` 주석이 들어 있습니다. 번호는 Notion 세션 자료의 목차 번호와 같습니다.

## 시작하기

```bash
git clone <레포 주소>
cd GDGoC_BE
git switch yeonghong      # 본인 브랜치 (mingyu / chanhaeng)
```

1. IntelliJ에서 레포 폴더를 Open 합니다. (Gradle 프로젝트로 자동 인식됩니다)
   - "Enable annotation processing" 알림이 뜨면 **Enable** (Lombok)
2. MySQL에 `gdg_board` 데이터베이스를 준비합니다. → `CREATE DATABASE gdg_board;`
3. **Edit Configurations** → `BoardApplication` → **Environment variables**에 `DB_USERNAME=root;DB_PASSWORD=본인비밀번호`
4. `BoardApplication`을 실행하고 `GET http://localhost:8080/posts` 가 `[]`를 돌려주면 준비 완료입니다.

> DB 비밀번호는 절대 `application.yml`에 직접 적지 마세요. 환경변수로만 넣습니다.

터미널에서 빌드·테스트하려면 (테스트는 H2를 쓰므로 MySQL 없이도 돌아갑니다):

```bash
./gradlew build      # Windows: .\gradlew.bat build
```

## 과제 진행 방법

1. 세션이 끝나면 `main`에 새 과제 명세가 올라옵니다. 본인 브랜치로 가져옵니다.
   ```bash
   git switch yeonghong
   git pull origin main
   ```
2. [`study_docs/sessionXX/README.md`](./study_docs)의 과제 명세를 보고 구현합니다.
3. 커밋 메시지 앞에 세션 번호를 붙입니다. → `[session03] 게시글 수정 API 구현`
4. 본인 브랜치에 push 합니다. push 할 때마다 GitHub Actions가 빌드·테스트를 돌립니다.
   ```bash
   git push origin yeonghong
   ```
5. 브랜치 루트의 [`NOTES.md`](./NOTES.md)에 세션별 회고·질문을 남깁니다.

### 규칙

- **본인 브랜치에만 push 합니다.** `main`, `example`, 다른 멤버 브랜치에는 push 하지 않습니다.
- `study_docs/` 폴더는 수정하지 않습니다. (`main`에서 받아올 때 충돌이 납니다)
- 자료의 코드를 복사·붙여넣기 하지 말고 **직접 타이핑하면서** 구현합니다.
- 막힌 부분은 `NOTES.md`의 질문 칸에 적어 주세요. 세션 때 같이 봅니다.

## 코드 비교하기

과제 마감 후에는 다른 멤버의 구현이나 예시 코드와 비교해 보세요.

```bash
git fetch origin --tags

# 이번 세션에서 내가 작성한 코드만 보기 (세션 시작 지점 태그와 비교)
git diff session03-start origin/yeonghong -- src

# 다른 멤버와 비교
git diff origin/yeonghong origin/mingyu -- src

# 예시 코드와 비교
git diff origin/example origin/yeonghong -- src
```

GitHub에서는 `https://github.com/<org>/<repo>/compare/mingyu...yeonghong` 형태의 주소로 비교 화면을 볼 수 있습니다.

## 멤버 추가 방법 (코어 멤버용)

```bash
git switch main
git switch -c <english-name>
git push -u origin <english-name>
```

이 레포의 README 브랜치 표에 새 멤버를 추가합니다.
