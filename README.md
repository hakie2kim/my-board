# 게시판

## 💬 소개

### 게시물 목록

`NoticeController` ➭ `BoardService` ➭ `BoardDao`
`listPage()` ➭ `findAllBoards()` ➭ `findAll()` (파라미터, 리턴 타입 추후 보완 예정)

### 게시물 단건 조회

`NoticeController` ➭ `BoardService` ➭ `BoardDao`
`readPage()` ➭ `findBoardByBoardSeq()` ➭ `findBoardByBoardSeq()` (파라미터, 리턴 타입 추후 보완 예정)

### 페이징

## 🔨 기능 요구사항

### 프로젝트 환경 설정하기

- [x] servlet

- [x] spring

- [x] spring jdbc

- [x] logback

### Servlet 구성 및 접속

- [ ] 게시물 목록 페이지 : `/forum//notice/listPage.do`

- [ ] 게시물 단건 조회 페이지 : `/forum/notice/readPage.do`

- [ ] 게시물 쓰기 페이지 : `/forum/notice/writePage.do`

### 예외 처리

### 기타

## 🐿️ Docker DB

```
# for Windows
docker run --name mysql-lecture -p 53306:3306 -v c:/dev/docker/mysql:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=admin_123 -d mysql:8.3.0

# for Mac
docker run --name mysql-lecture -p 53306:3306 -v ~/dev/docker/mysql:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=admin_123 -d mysql:8.3.0
```

## 🚨 트러블 슈팅

## 📝 메모

### `SQL LIMIT x OFFSET y`

```sql
SELECT b.board_seq, b.board_type_seq, b.title, b.content, b.hit, b.del_yn, b.reg_dtm, b.reg_member_seq, b.update_dtm, b.update_member_seq, m.member_id
FROM forum.`board` b
JOIN forum.`member` m
ON b.reg_member_seq = m.member_seq
LIMIT 20, OFFSET 10;
```

위와 같은 쿼리를 사용하게 되면 페이지에 따라 보여지는 게시물 목록을 다르게 설정할 수 있다. 위 쿼리는 처음 20개 행을 건너 뛰고 10개의 행을 갖고 온다. LIMIT을 사용해 페이지마다 원하는 게시물을 갖고 오게하는 sql은 다음과 같다.

```sql
SELECT b.board_seq, b.board_type_seq, b.title, b.content, b.hit, b.del_yn, b.reg_dtm, b.reg_member_seq, b.update_dtm, b.update_member_seq, m.member_id
FROM forum.`board` b
JOIN forum.`member` m
ON b.reg_member_seq = m.member_seq
LIMIT ((현재 페이지) - 1) * (페이지 당 게시물 수), OFFSET (페이지 당 게시물 수);
```

참고로 `OFFSET`은 생략 가능하다.
