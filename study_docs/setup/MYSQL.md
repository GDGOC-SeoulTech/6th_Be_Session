# MySQL 8.4 LTS 설치·접속

세션 03 전에 **MySQL 서버 실행 → 접속 → 실습 DB 준비 → Spring 연결 → POST·GET 확인**을 완료합니다. Java는 JDK 21이며, IntelliJ의 Project SDK와 Gradle JVM을 모두 21로 맞춥니다.

## 1. 핵심 용어

- **MySQL Server**: 데이터를 저장하고 SQL 요청을 처리하는 DB 서버 프로그램.
- **mysql 클라이언트**: 실행 중인 MySQL 서버에 접속해 SQL을 보내는 명령줄 프로그램. 터미널에서 `mysql`을 실행하는 것과 서버를 시작하는 것은 다른 작업입니다.
- **Database**: 이 프로젝트의 테이블들을 담는 DB 이름. 여기서는 `gdg_board`를 사용합니다.
- **환경변수**: 실행하는 프로그램에 외부에서 전달하는 설정값. DB 계정과 비밀번호는 코드 대신 실행 설정으로 전달합니다.

근거: [MySQL 서버](https://dev.mysql.com/doc/refman/8.4/en/mysqld.html), [mysql 클라이언트](https://dev.mysql.com/doc/refman/8.4/en/mysql.html).

## 2. Windows 설치

1. [MySQL Community Server 다운로드](https://dev.mysql.com/downloads/mysql/)에서 **8.4.x LTS**, Windows, MSI 패키지를 선택합니다.
2. MSI를 실행하고 설치 후 **MySQL Configurator**를 실행합니다. 설치만 끝내고 설정을 생략하면 서버가 시작되지 않습니다.
3. 로컬 개발용 설정으로 진행하고 포트 **3306**, root 비밀번호, Windows 서비스 설정을 확인한 뒤 적용합니다. 비밀번호는 IntelliJ에서도 같은 값을 사용합니다.
4. 시작 메뉴의 MySQL Command Line Client를 열어 root 비밀번호를 입력합니다.

PowerShell에서 직접 접속하려면:

```powershell
& "C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe" -u root -p
```

설치 위치를 바꿨다면 경로도 바꿉니다. 실행 시 Visual C++ 런타임 오류가 발생하면 공식 설치 안내에 연결된 재배포 패키지를 설치합니다.

근거: [MySQL 8.4 Windows 공식 설치 안내](https://dev.mysql.com/doc/refman/8.4/en/windows-installation.html).

## 3. macOS 설치

Homebrew가 설치된 터미널에서 실행합니다. 다른 버전의 MySQL 서비스를 이미 실행 중이라면 먼저 확인하고, 서버 두 개가 3306 포트를 함께 사용하지 않도록 합니다.

```bash
brew install mysql@8.4
brew services start mysql@8.4
"$(brew --prefix mysql@8.4)/bin/mysql" -u root
```

새 Homebrew 설치는 root 비밀번호 없이 초기화됩니다. 접속한 뒤 비밀번호를 지정합니다. 기존 설치에서 이미 비밀번호를 설정했다면 마지막 접속 명령에 `-p`를 붙입니다.

```sql
ALTER USER 'root'@'localhost' IDENTIFIED BY '본인비밀번호';
```

이후 접속:

```bash
"$(brew --prefix mysql@8.4)/bin/mysql" -u root -p
```

위 방식은 PATH 설정 없이 실행할 수 있습니다. 명령 이름만 쓰고 싶다면 `brew info mysql@8.4`의 PATH 안내를 따릅니다.

근거: [Homebrew mysql@8.4 공식 Formula](https://formulae.brew.sh/formula/mysql@8.4).

## 4. 접속 확인 · 실습 DB 초기화

`mysql>` 프롬프트가 나타나면 SQL을 입력합니다. 세미콜론까지 입력해야 실행됩니다.

```sql
SELECT VERSION();
```

**아래 DROP은 로컬 실습용 `gdg_board`의 기존 데이터를 삭제합니다.** 보관할 데이터는 먼저 백업하고, 다른 프로젝트나 공용 DB에는 실행하지 않습니다. 노션의 게시글 번호 1·2·3과 맞추기 위해 세션 전에 한 번 초기화합니다.

```sql
DROP DATABASE IF EXISTS gdg_board;
CREATE DATABASE gdg_board CHARACTER SET utf8mb4;
SHOW DATABASES;
```

테이블은 직접 만들지 않습니다. 현재 프로젝트의 `ddl-auto: update` 설정에 따라 서버 기동 시 Hibernate가 엔티티 매핑을 바탕으로 테이블을 준비합니다.

## 5. IntelliJ에서 Spring 연결

1. **Edit Configurations → BoardApplication → Environment variables**를 엽니다. 항목이 안 보이면 **Modify options → Environment variables**를 켭니다.
2. `DB_USERNAME`은 `root`, `DB_PASSWORD`는 설정한 비밀번호로 추가합니다. 비밀번호에 구분 문자가 있으면 변수 편집 표에서 항목별로 입력합니다.
3. `BoardApplication`을 실행하고 8080 포트에서 서버가 시작되는지 확인합니다.
4. [세션 03 STEP 0](../session03/README.md#step-0-준비-세션-시작-전)의 게시글 세 개를 순서대로 POST합니다. id **1·2·3**과 `GET /posts`의 **200**을 확인합니다.

`./gradlew prepareSession`의 H2 테스트 통과와 실제 MySQL 연결 성공은 별도로 확인합니다.

## 6. 막혔을 때

| 증상 | 확인할 것 |
| --- | --- |
| `mysql`을 찾을 수 없음 | Windows는 전체 경로, Mac은 `brew --prefix` 경로로 실행 |
| `Access denied for user` | root 비밀번호로 클라이언트 접속을 먼저 확인한 뒤 IntelliJ 변수 확인 |
| `Communications link failure` / 연결 거부 | Windows 서비스 또는 `brew services list`에서 서버 실행 확인, 포트 3306 확인 |
| `Unknown database 'gdg_board'` | 같은 서버에서 CREATE DATABASE 실행 여부 확인 |
| `DB_USERNAME` / `DB_PASSWORD` 설정 오류 | 실제 실행하는 BoardApplication 구성에 변수가 들어갔는지 확인 |
| H2 테스트가 다른 DB에 접속하거나 PostgreSQL 드라이버를 요청함 | 다른 프로젝트에서 설정한 `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `SPRING_PROFILES_ACTIVE` 환경변수가 수업 설정을 덮어쓰는지 확인 |
| Java 버전 오류 / `UnsupportedClassVersionError` | Project SDK·Gradle JVM·실행 JRE를 21로 맞추고, 터미널은 `java -version`과 JAVA_HOME 확인 |
| 8080 포트 사용 중 | 이미 실행한 BoardApplication을 종료한 뒤 재실행 |
| 게시글 id가 1·2·3이 아님 | POST를 중복 실행했는지 확인하고, 필요한 데이터 보관 후 실습 DB 초기화부터 다시 진행 |

에러가 나면 콘솔의 `Caused by` 원인 메시지와 실패한 단계를 진행자에게 보여 주세요. 공유하는 화면에 비밀번호는 포함하지 않습니다.
