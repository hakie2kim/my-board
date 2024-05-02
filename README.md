# 게시판

## 💬 소개

### 게시물 목록

`NoticeController` ➭ `BoardService` ➭ `BoardDao`
`listPage()` ➭ `findAllBoards()` ➭ `findAll()` (파라미터, 리턴 타입 추후 보완 예정)

### 게시물 단건 조회

`NoticeController` ➭ `BoardService` ➭ `BoardDao`
`readPage()` ➭ `findBoardByBoardSeq()` ➭ `findBoardByBoardSeq()` (파라미터, 리턴 타입 추후 보완 예정)

### 페이징

생성자를 통해 `전체 게시물 개수`, `현재 페이지 번호`, `한 페이지 당 게시물 개수`가 주어지면 `전체 페이지 개수`, `시작 페이지 번호`, `끝 페이지 번호`, `이전 & 다음 화살표 표시 여부`가 차례로 정해진다.

#### 전체 페이지 개수

- 필요한 값: `전체 게시물 개수`, `한 페이지 당 게시물 개수`

`전체 게시물 개수` / `한 페이지 당 게시물 개수`가 나머지 없이 나눠 떨어진다면 전체 페이지 개수를 구할 수 있다. 하지만 나눠 떨어지지 않는 경우는 어떨까? 단순히 1을 더하면 될 거 같지만 이때는 나머지 없이 나눠 떨어지는 경우에 부합하지 않는다.

예) `한 페이지 당 게시물 개수`가 `5`인 경우

(1) `전체 게시물 개수`가 `1`, `2`, `3`, `4`, `5`인 경우에는 페이지가 `1`개가 필요
(2) `전체 게시물 개수`가 `6`, `7`, `8`, `9`, `10`인 경우에는 페이지가 `2`개가 필요
(3) `전체 게시물 개수`가 `11`, `12`, `13`, `14`, `15`인 경우에는 페이지가 `3`개가 필요
...

`전체 게시물 개수`가 `한 페이지 당 게시물 개수`의 배수가 되는 시점에 필요한 페이지의 개수가 올라가는 것을 확인할 수 있다. 따라서 `한 페이지 당 게시물 개수`로 나눈 몫이 일정해야 한다. 하지만 `1`, `2`, `3`, `4`의 경우는 `0`이지만 `5` 혼자만 `1`이다. `0`부터 `4`까지는 몫이 `0`이기 때문에 `전체 게시물 개수`에서 `1`을 빼준다.

```
((전체 게시물 개수) - 1) / (한 페이지 당 게시물 개수) + 1
```

#### 끝 페이지 번호

- 필요한 값: `현재 페이지 번호`, `한번에 표시할 페이지 개수`, `전체 페이지 개수`

예) `현재 페이지 번호`가 `17`이고 `한번에 표시할 페이지 개수`가 `5`인 경우

```
(1, 2, 3, 4, 5), (6, 7, 8, 9, 10), (11, 12, 13, 14, 15), (16, 17, 18, 19, 20), ...
```

`전체 페이지 개수`를 구하는 식과 비슷하게 `한번에 표시할 페이지 개수`로 묶었을 때 끝 번호가 구하고자 하는 `끝 페이지 번호가` 된다. 나누어 떨어지는 경우만 몫이 증가하는 형태로 규칙이 깨지기 때문에 마찬가지로 `1`을 빼준다.

```
(((현재 페이지 번호) - 1) / (한번에 표시할 페이지 개수) + 1) * 한번에 표시할 페이지 개수
```

하지만 `전체 페이지 개수`가 `끝 페이지 번호` 보다 작은 경우 `끝 페이지 번호`는 `전체 페이지 개수`가 된다.

```java
if (this.totalPages < this.endPage)
  this.endPage = this.totalPages;
```

#### 시작 페이지 번호

`끝 페이지 번호`와 비슷한 방법으로 구할 수 있다.

```
((현재 페이지 번호) - 1) / (한번에 표시할 페이지 개수) + 1
```

#### 이전 화살표 표시 여부

`시작 페이지 번호`가 `1`인 경우에만 표시하지 않고 나머지는 모두 표시한다.

#### 다음 화살표 표시 여부

`끝 페이지 번호`가 `전체 페이지 개수`인 경우에만 표시하지 않고 나머지는 모두 표시한다.

```java
package com.pf.www.forum.notice.util;

public class Pagination {
	private int totalPosts; // 전체 게시물 개수
	private int currentPage; // 현재 페이지 번호
	private int postsPerPage; // 한 페이지 당 게시물 개수
	public static int DISPLAY_PAGE_NUM = 10; // 한번에 표시할 페이지 개수

	private int totalPages; // 전체 페이지 개수
	private int startPage; // 시작 페이지 번호
	private int endPage; // 끝 페이지 번호
	private boolean prev; // 이전 화살표 표시 여부
	private boolean next; // 다음 화살표 표시 여부

	public Pagination(int totalPosts, int currentPage, int postsPerPage) {
		this.totalPosts = totalPosts;
		this.currentPage = currentPage;
		this.postsPerPage = postsPerPage;

		setTotalPages();
		setStartPage();
		setEndPage();
		setPrev();
		setNext();
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages() {
		this.totalPages = ((this.totalPosts - 1) / this.postsPerPage) + 1;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage() {
		this.startPage = ((this.currentPage - 1) / DISPLAY_PAGE_NUM) * DISPLAY_PAGE_NUM + 1;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage() {
		this.endPage = (((this.currentPage - 1) / DISPLAY_PAGE_NUM) + 1) * DISPLAY_PAGE_NUM;

		if (this.totalPages < this.endPage)
			this.endPage = this.totalPages;
	}

	public boolean getPrev() {
		return prev;
	}

	public void setPrev() {
		this.prev = (this.startPage == 1) ? false : true;
	}

	public boolean getNext() {
		return next;
	}

	public void setNext() {
		this.next = (this.endPage == this.totalPages) ? false : true;
	}
}
```

## 🔨 기능 요구사항

### 프로젝트 환경 설정하기

- [x] servlet

- [x] spring

- [x] spring jdbc

- [x] logback

### Servlet 구성 및 접속

- [x] 게시물 목록 페이지 : `/forum//notice/listPage.do`

- [x] 게시물 단건 조회 페이지 : `/forum/notice/readPage.do`

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
