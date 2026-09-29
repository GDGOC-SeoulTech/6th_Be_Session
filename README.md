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
| Spring Boot | 3.5.3 |
| Gradle | 8.14.3 (Wrapper 포함) |
| DB | H2 (in-memory) |
| 기본 패키지 | `com.gdgoc.board` |

## 시작하기

```bash
git clone <레포 주소>
cd GDGoC_BE
git switch yeonghong      # 본인 브랜치 (mingyu / chanhaeng)
```

1. IntelliJ에서 레포 폴더를 Open 합니다. (Gradle 프로젝트로 자동 인식됩니다)
2. `BoardApplication`을 실행하고 http://localhost:8080/h2-console 이 열리면 준비 완료입니다.
   - JDBC URL: `jdbc:h2:mem:board` / User: `sa` / Password: (비워 두기)

터미널에서 빌드·실행하려면:

```bash
./gradlew build      # Windows: .\gradlew.bat build
./gradlew bootRun
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
git fetch origin

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
