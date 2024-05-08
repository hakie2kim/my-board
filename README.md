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

```
(1) `전체 게시물 개수`가 `1`, `2`, `3`, `4`, `5`인 경우에는 페이지가 `1`개가 필요
(2) `전체 게시물 개수`가 `6`, `7`, `8`, `9`, `10`인 경우에는 페이지가 `2`개가 필요
(3) `전체 게시물 개수`가 `11`, `12`, `13`, `14`, `15`인 경우에는 페이지가 `3`개가 필요
...
```

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

### 게시물 별 좋아요/싫어요 조회

`NoticeController` ➭ `BoardService` ➭ `BoardDao`
`readPage()` ➭ `findIsLikeByBoardSeqAndBoardTypeSeqAndMemberSeq()` ➭ `findIsLikeByBoardSeqAndBoardTypeSeqAndMemberSeq()` (파라미터, 리턴 타입 추후 보완 예정)

`member_seq`는 `session`에서 갖고 온다.

### 게시물 별 좋아요/싫어요 반영

좋아요/싫어요를 반영할 때 필수적으로 그리고 선택적으로 필요한 값은 다음과 같다.

- 필수: `board_seq`, `board_type_seq`, `member_seq`
- 선택: `member_id`, `IP` 등

이때, `member_seq`, `member_id`가 둘다 고유하다면 둘 중 어떤 것이든 필수 값으로 사용해도 된다.

`RestNoticeController` ➭ `BoardService` ➭ `BoardDao`

- 좋아요/싫어요를 처음 누르는 경우: `vote()` ➭ `addVote()` ➭ `addVote()` (파라미터, 리턴 타입 추후 보완 예정)
- 좋아요/싫어요가 이미 있는 경우: `vote()` ➭ `updateVote()` ➭ `addVote()` (파라미터, 리턴 타입 추후 보완 예정)

`member_seq`는 `session`에서 갖고 온다.

### 게시물 쓰기

`RestNoticeController` ➭ `BoardService` ➭ `BoardDao`
`write()` ➭ `write()` ➭ `addBoard()` (파라미터, 리턴 타입 추후 보완 예정)

## 🔨 기능 요구사항

### 프로젝트 환경 설정하기

- [x] servlet

- [x] spring

- [x] spring jdbc

- [x] logback

### Servlet 구성 및 접속

- [x] 게시물 목록 페이지 : `/forum//notice/listPage.do`

- [x] 게시물 단건 조회 페이지 : `/forum/notice/readPage.do`

- [x] 게시물 쓰기 페이지 : `/forum/notice/writePage.do`

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

### 게시물 목록에서 필요한 파라미터가 없는 경우

`/forum//notice/listPage.do?page=1&size=10`의 `query string`에서 만약 값을 넘겨주지 않는다면 다음과 같은 에러가 발생한다.

### 문제 상황

```
Request processing failed; nested exception is java.lang.NumberFormatException: Cannot parse null string
com.pf.www.forum.notice.controller.NoticeController.listPage(NoticeController.java:25)
```

`startBoardSeq` 시작할 게시물 번호를 구하기 위해 `String`에서 `Integer`로 변환을 시도한다. 이때 `query string`의 값이 없는 경우 `null` 값을 `Integer` 값으로 변환하려 시도하기 때문에 위와 같은 에러가 발생한 것이다.

### 해결 방법

`page`의 값과 `size`의 값이 주어지지 않아도 기본값을 갖고 있게 `Spring`의 `@RequestParam` 기능을 사용했다.

```java
public ModelAndView listPage(
  @RequestParam(defaultValue="1") Integer page,
  @RequestParam(defaultValue="10") Integer size
) {
```

### `JSP`의 `EL` 비교

#### 문제 상황

`list.jsp`의 `navbar`에서 표시하고 있는 페이지가 현재 페이지인가 아닌가를 구분하기 위해 다음과 같은 EL의 비교 표현을 사용한다.

```
<a class="page-numbers <c:if test="${params.page eq pageNum}">current</c:if>" href="<c:url value='/forum/notice/listPage.do?page=${pageNum}&size=${pagination.postsPerPage}'/>">${pageNum}</a>
```

하지만 `params.page`와 `현재 페이지`가 일치함에도 계속해서 `css`에서는 반영이 안되었다. `params.page`는 문자열 값 `pageNum`은 숫자 값이기 때문에 의도했던 비교가 되지 않고 있었던 것이다.

#### 해결 방법

```
<a class="page-numbers <c:if test="${pagination.currentPage eq pageNum}">current</c:if>" href="<c:url value='/forum/notice/listPage.do?page=${pageNum}&size=${pagination.postsPerPage}'/>">${pageNum}</a>
```

여기서 `pagination.currentPage`는 숫자이다.

### `JSP`의 `EL` 값 조회

#### 문제 상황

```
org.apache.jasper.JasperException: 행 [93]에서 [/WEB-INF/views/forum/notice/list.jsp]을(를) 처리하는 중 예외 발생

90: </c:if>
91:   <c:forEach var="pageNum" begin="${pagination.startPage}" end="${pagination.endPage}">
92:     <c:if test="${pagination.currentPage eq pageNum}">
93:       <a class="page-numbers current" href="<c:url value='/forum/notice/listPage.do?page=${pageNum}&size=${pagination.postsPerPage}'/>">${pageNum}</a>
94:     </c:if>
95:     <c:if test="${pagination.currentPage ne pageNum}">
96:       <a class="page-numbers" href="<c:url value='/forum/notice/listPage.do?page=${pageNum}&size=${pagination.postsPerPage}'/>">${pageNum}</a>

javax.el.PropertyNotFoundException: [postsPerPage] 특성이 [com.pf.www.forum.notice.util.Pagination] 유형에 없습니다.
```

#### 해결 방법

`EL`은 객체의 값을 `${객체주소.필드}`와 같이 조회할 때 해당 클래스에 `getter`가 있는지 확인한다. 없는 경우 위와 같은 에러가 발생한다. 따라서 `Pagination`에 `getPostsPerPage()` 메서드를 추가해주었다.

### 게시물 별 좋아요/싫어요 반영

#### 문제 상황

`INSERT` 쿼리만을 통해 게시물 별 좋아요/싫어요를 반영할 때 사용자가 처음으로 좋아요 또는 싫어요를 누를 때는 문제가 없었지만 그 다음 좋아요 또는 싫어요를 누를 때는 같은 문제가 발생했다.

```
org.springframework.dao.DuplicateKeyException
SQL [INSERT INTO forum.board_vote (board_seq, board_type_seq, member_seq, is_like, reg_dtm) VALUES(?, ?, ?, ?, DATE_FORMAT(NOW(), '%Y%m%d%H%i%s')); ];
Duplicate entry '1-1-1' for key 'board_vote.PRIMARY';
```

`board_vote`의 `pk`는 `(board_seq, board_type_seq, member_seq)`와 같다. 사용자가 좋아요/싫어요를 처음이 아닌 경우 클릭할 때 이미 존재하는 `pk`로 또 다시 `INSERT`를 하려 하기 때문에 위와 같은 에러가 발생하는 것이다.

#### 해결 방법

위와 같은 에러가 발생하는 경우를 service 계층에서 try-catch문으로 잡아 INSERT 대신 UPDATE로 쿼리를 실행했다.

```java
public int vote(Integer boardSeq, Integer boardTypeSeq, Integer memberSeq, String isLike) {
	try { // 처음 좋아요/싫어요를 하는 경우
		return boardDao.addVote(boardSeq, boardTypeSeq, memberSeq, isLike);
	} catch (DuplicateKeyException de) { // 좋아요/싫어요가 이미 있는 경우
		return boardDao.updateVote(boardSeq, boardTypeSeq, memberSeq, isLike);
	}
}
```

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

### 식별 vs. 비식별 관계

- 식별: 부모 테이블에 데이터가 존재해야 자식 테이블에 데이터를 입력할 수 있음

  예) `자동차 테이블`에서 `자동차_아이디`가 `pk`일 때 `바퀴 테이블`에서 `자동차_아이디`를 `pk`로 갖는 경우

- 비식별: 자식 데이터는 부모 데이터가 없어도 독립적으로 생성될 수 있음

  예) `자동차 테이블`에서 `자동차_아이디`가 `pk`일 때 `자동차_아이디`가 `pk`, `바퀴 테이블`에서 `자동차_아이디`를 `pk`로 갖지 않는 경우
