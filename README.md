# 게시판

## 💬 소개

### 게시물 목록

`NoticeController` ➭ `BoardService` ➭ `BoardDao`
`listPage()` ➭ `findAllBoards()` ➭ `findAll()` (파라미터, 리턴 타입 추후 보완 예정)

---

### 게시물 단건 조회

`NoticeController` ➭ `BoardService` ➭ `BoardDao`
`readPage()` ➭ `findBoardByBoardSeq()` ➭ `findBoardByBoardSeq()` (파라미터, 리턴 타입 추후 보완 예정)

---

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

---

### 게시물 별 좋아요/싫어요 조회

`NoticeController` ➭ `BoardService` ➭ `BoardDao`
`readPage()` ➭ `findIsLikeByBoardSeqAndBoardTypeSeqAndMemberSeq()` ➭ `findIsLikeByBoardSeqAndBoardTypeSeqAndMemberSeq()` (파라미터, 리턴 타입 추후 보완 예정)

`member_seq`는 `session`에서 갖고 온다.

---

### 게시물 별 좋아요/싫어요 반영

좋아요/싫어요를 반영할 때 필수적으로 그리고 선택적으로 필요한 값은 다음과 같다.

- 필수: `board_seq`, `board_type_seq`, `member_seq`
- 선택: `member_id`, `IP` 등

이때, `member_seq`, `member_id`가 둘다 고유하다면 둘 중 어떤 것이든 필수 값으로 사용해도 된다.

`RestNoticeController` ➭ `BoardService` ➭ `BoardDao`

- 좋아요/싫어요를 처음 누르는 경우: `vote()` ➭ `vote()` ➭ `addVote()` (파라미터, 리턴 타입 추후 보완 예정)
- 좋아요 또는 싫어요가 이미 있는 경우: `vote()` ➭ `vote()` ➭ `updateVote()` (파라미터, 리턴 타입 추후 보완 예정)
- 이미 누른 좋아요/싫어요를 다시 한 번 더 누를 경우: `vote()` ➭ `vote()` ➭ `cntVote()`, `deleteVote()` (파라미터, 리턴 타입 추후 보완 예정)

`member_seq`는 `session`에서 갖고 온다.

---

### 게시물 쓰기

`RestNoticeController` ➭ `BoardService` ➭ `BoardDao` / `BoardService`
`write()` ➭ `write()` ➭ `addBoard()` / `uploadFiles()` (파라미터, 리턴 타입 추후 보완 예정)

---

### 게시물 수정

#### 흐름

`NoticeController` ➭ `BoardService` ➭ `BoardDao`
`modify()` ➭ `modify()` ➭ `updateBoard()` (파라미터, 리턴 타입 추후 보완 예정)

`NoticeController` ➭ `BoardService` ➭ `FileUtil` / `BoardAttachDao`
`modify()` ➭ `uploadFiles()` ➭ `saveFile()` / `addBoardAttach()` (파라미터, 리턴 타입 추후 보완 예정)

- `uploadFiles()`

`MultipartFile`의 메서드 중 `getOriginalFilename()`을 이용해 파일의 이름이 비어있는 경우를 확인하고 이는 사용자가 파일을 업로드하지 않은 것이기 때문에 아래 과정을 수행하지 않는다.

1. 물리적 파일 저장
2. DB에 파일 정보 저장

➭ 만약 위 과정에서 에러가 발생하는 경우, 저장된 물리적 파일을 지운다.

#### 문제점

사용자가 수정하지 않은 파일이 또 다시 업로드 되는 문제가 발생한다.

---

### 게시물 수정에서 파일 개별 삭제

#### 흐름

`modify.jsp` ➭ `RestNoticeController` ➭ `BoardService` ➭ `BoardAttachDao`
`<buton> 태그 클릭 시 removeFile()` ➭ `removeFile()` ➭ `findBoardAtt()`, `deleteBoardAtt()` (파라미터, 리턴 타입 추후 보완 예정)

- `BoardService`의 `removeFile()`

1. 지워야 되는 파일의 정보를 찾음
2. 물리적으로 저장되어 있는 파일을 삭제
3. DB에 저장된 파일 정보 삭제
4. 성공한 경우 `'1'`이 ajax의 비동기 처리 결과 값으로 전달되고 `modify.jsp`의 페이지가 새로고침됨

---

### 게시물 파일 업로드

#### ERD 설계

다음은 게시물 파일을 업로드 했을 때 저장해야 하는 값을 위한 논리 테이블이다.

|   논리 이름    |               예시 값                |
| :------------: | :----------------------------------: |
|   일련 번호    |                  1                   |
|  게시판 번호   |                  1                   |
|  게시글 번호   |                 1013                 |
| 원본 파일 이름 |              커피.jpec               |
|  변경 파일명   |         djvanlkwnlkean.jpec          |
|  업로드 일시   |              2405110900              |
|   파일 형식    |              image/jpec              |
|   파일 크기    |               7402849                |
|   저장 경로    | /file/2024/05/11/djvanlkwnlkean.jpec |

파일 형식은 파일을 다운로드 받을 때 인코딩할 방식을 정하는 `content-type`의 값으로 사용된다.

저장 경로는 다음과 같이 `URL`, `URI` 방식으로 저장할 수 있다.

- 다운로드 `URL`: http://localhost:8080/file/djvanlkwnlkean.jpec
- 다운로드 `URI`: /file/2024/05/11/djvanlkwnlkean.jpec

클라우드에 파일을 저장할 경우에는 파일의 일련 번호를 DB에서 찾아 다운로드하면 된다.

일자별로 구분된 폴더에 파일을 저장하는 이유는 전체 파일 리스트를 찾기 위해 `ls -la`와 같은 명령어를 입력했을 때 파일 리스트가 전부 보이지 않을 수도 있기 때문이다.

서로 다른 사용자가 같은 이름의 파일을 업로드 했을 때 파일 이름이 중복되지 않도록 고유한 이름으로 바꿔주도록 한다.

#### Maven dependency 추가

- `Apache Commons IO` `(ver 2.16.1)`: https://mvnrepository.com/artifact/commons-io/commons-io
- `Apache Commons FileUpload` `(ver 1.5)`: https://mvnrepository.com/artifact/commons-fileupload/commons-fileupload

#### Bean 등록

```xml
<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
	<property name="maxUploadSize" value="-1"/>
</bean>
```

#### `<form>` 태그 속성

HTML 폼 전송 방식에는 두 가지가 있다. 해당 값들은 `enctype` 속성을 통해 결정할 수 있다.

- `application/x-www-form-urlencoded` (기본값)
- `multipart/form-data`

`<form>` 태그에 별도 `enctype` 옵션이 없다면 다음과 같은 예로 데이터가 전송된다.

1. 다음과 같은 폼에 값을 입력하고 전송 버튼을 누른다.

```html
<form action="/register" method="post">
  <input type="text" name="username" />
  <input type="text" name="bday" />
  <button type="submit">전송</button>
</form>
```

2. 웹 브라우저는 다음과 같은 HTTP 메시지를 생성한다.

```
POST /register HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded

username=jun&bday=20020202
```

하지만 파일까지 같이 전송할 경우 문자와 바이너리를 같이 보내야 하는데 이를 위해 나온 전송 방식이 바로 `multipart/form-data`이다.

##### 흐름

`write.jsp` ➭ `NoticeController` ➭ `BoardService` ➭ `BoardDao`, `FileUtil`, `BoardAttachDao`
`<input type="file" name="attFile">` ➭ `write()` ➭ `addBoard()`, `saveFile()`, `addBoardAttach()` (파라미터, 리턴 타입 추후 보완 예정)

- `BoardService`의 `write()` (파라미터, 리턴 타입 추후 보완 예정)

1. `board` 테이블에 게시물 관련 정보를 저장한다. ➭ `3.`에서 필요한 값을 위해 `addBoard()` 리턴 값을 `board_seq`로 변경했다.
2. `saveFile()`을 통해 실제 파일 저장한다.
   2-1. 만약 실패한 경우 `saveFile()` 메서드는 `IOException`, `IllegalStateException` 예외를 던지는데 이 경우 실제 저장한 파일을 삭제한다 (`delete()`).
3. `board_attach` 테이블에 파일 관련 정보를 저장한다.

- `saveFile()` (파라미터, 리턴 타입 추후 보완 예정)

1. `/file/연도/월/일` 폴더가 존재 하지 않으면 만든다.
2. 파일 이름을 UUID를 이용해 변경한다.
3. `MultipartFile`의 메서드 `transferTo()`를 이용해 파일을 저장한다. `transferTo()`는 `IOException`, `IllegalStateException` 예외를 던진다.

---

### 게시물 별 파일 조회

`NoticeController` ➭ `BoardService` ➭ `BoardAttachDao` ➭ `read.jsp`
`readPage()` ➭ `findBoardAttList()` ➭ `findBoardAttList()` (파라미터, 리턴 타입 추후 보완 예정)

---

### 게시물 별 파일 개별 다운로드

#### `FileDownloadView`

추상 클래스 `AbstractView`를 구현하고 `renderMergedOutputModel()`를 오버라이드 한다. 해당 뷰로 다운로드 받을 파일(`file`)과 원본 파일 이름(`orgFileNm`)을 보내주면 된다.

#### `BeanNameViewResolver` 뷰 리졸버 등록

```xml
<bean class="org.springframework.web.servlet.view.BeanNameViewResolver">
	<property name="order" value="0" />
</bean>
```

`BeanNameViewResolver`는 빈 이름으로 뷰를 찾아서 반환하는 뷰 리졸버이다. 해당 뷰 리졸버의 순위를 우선으로 변경하여 `readPage()`에서 `"fileViewResolver"`를 리턴했을 때 직접 등록한 `fileViewResolver` 뷰를 찾도록 한다.

#### 흐름

`NoticeController` ➭ `BoardService` ➭ `BoardAttachDao` ➭ `FileDownloadView`
`downloadFile()` ➭ `findFileInfo()` ➭ `findBoardAtt()` (파라미터, 리턴 타입 추후 보완 예정)

---

### 게시물 별 모든 파일 압축 다운로드

#### 흐름

`read.jsp` ➭ `NoticeController` ➭ `BoardService` ➭ `BoardAttachDao` ➭ `FileDownloadView`
`<form> 태그` ➭ `downloadMultipleFiles()` ➭ `findFileInfo()`, `makeZipFile()` (파라미터, 리턴 타입 추후 보완 예정)

- `read.jsp`

```jsp
<c:if test="${fn:length(attFiles) ne 0}">
	<form action="<%=ctx%>/forum/notice/downloadMultipleFiles.do" method="post">
		<c:forEach var="attFile" items="${attFiles}">
			<input type="hidden" name="attSeq" value="${attFile.attachSeq}">
		</c:forEach>
		<button type="submit">파일 한번에 다운 받기</button>
	</form>
</c:if>
```

- `makeZipFile()`

`FileUtil.java`의 `createZipFile()` 메서드를 사용한다.

1. 파일 객체 생성 및 리스트에 추가
   ➭ 파일이 저장될 때 `UUID`를 사용해 파일 이름이 바뀌기 때문에 `.zip` 파일을 만들 때는 `FileUtils`의 `copyFile()`을 이용해 원본 이름을 갖고 있는 파일로 복제한다.
2. `.zip` 파일 생성
3. 원래 이름으로 복원한 파일들 삭제

```java
public File createZipFile(List<BoardAttachDto> filesInfo) throws IOException {
	List<File> files = new ArrayList<>();

	// 파일 객체 생성 및 리스트에 추가
	for (BoardAttachDto fileInfo : filesInfo) {
		String renamedFileSavePath = fileInfo.getSavePath();
			File renamedFile = new File(renamedFileSavePath);
			File orgFile = new File(renamedFileSavePath.substring(0, renamedFileSavePath.lastIndexOf("\\") + 1) + fileInfo.getOrgFileNm());
			FileUtils.copyFile(renamedFile, orgFile);
			files.add(orgFile);
	}

	// 임시 파일로 생성된 ZIP 파일을 저장할 위치와 이름 설정)
	File zipFile = File.createTempFile("compressed_files", ".zip");

	// ZIP 파일 생성
	try (FileOutputStream fos = new FileOutputStream(zipFile);
				ZipOutputStream zos = new ZipOutputStream(fos)) {

			// 각 파일을 ZIP 파일에 추가
			for (File file : files) {
					// 파일이 존재하고 파일일 경우에만 압축 진행
					if (file.exists() && file.isFile()) {
							try (FileInputStream fis = new FileInputStream(file)) {
									// ZIP 엔트리 생성
									ZipEntry zipEntry = new ZipEntry(file.getName());
									zos.putNextEntry(zipEntry);

									// 파일 데이터를 버퍼를 사용하여 읽고, ZIP 파일에 작성
									byte[] buffer = new byte[1024];
									int length;
									while ((length = fis.read(buffer)) >= 0) {
											zos.write(buffer, 0, length);
									}
							}
					} else {
							System.err.println("파일을 찾을 수 없습니다: " + file.getAbsolutePath());
					}
			}
	}

	// 원래 이름으로 복원한 파일들 삭제
	for (File orgFile : files) {
		orgFile.delete();
	}

	// 생성된 ZIP 파일을 리턴
	return zipFile;
}
```

---

### 게시글 목록 댓글 개수, 파일 유무 표시

`BoardListDto`에 필드 `attachCount`, `commentCount` 추가

---

### 댓글

#### ERD 설계

|   논리 이름    |     예시 값      |
| :------------: | :--------------: |
|   일련 번호    |        1         |
|  작성자 번호   |       1563       |
|  게시판 번호   |        1         |
|  게시글 번호   |       1013       |
|   작성 일시    |    2405110900    |
|   수정 일시    |    2405211908    |
|      내용      | 댓글 내용입니다. |
| 상위 일련 번호 |        30        |

상위 일련 번호가 존재하면 대댓글이다.

#### Mock 데이터 넣기

```sql
ALTER TABLE forum.board_comment AUTO_INCREMENT = 1;

INSERT INTO forum.board_comment (lvl,content,board_seq,board_type_seq,member_seq,parent_comment_seq,reg_dtm,update_dtm,delete_dtm) VALUES
	 (0,'1번 댓글',1000,1,67,NULL,'20240506124700',NULL,NULL);
INSERT INTO forum.board_comment (lvl,content,board_seq,board_type_seq,member_seq,parent_comment_seq,reg_dtm,update_dtm,delete_dtm) VALUES
	 (0,'2번 댓글',1000,1,67,NULL,'20240506124700',NULL,NULL);
INSERT INTO forum.board_comment (lvl,content,board_seq,board_type_seq,member_seq,parent_comment_seq,reg_dtm,update_dtm,delete_dtm) VALUES
	 (1,'1번 대댓글',1000,1,67,1,'20240506124700',NULL,NULL);
INSERT INTO forum.board_comment (lvl,content,board_seq,board_type_seq,member_seq,parent_comment_seq,reg_dtm,update_dtm,delete_dtm) VALUES
	 (2,'1번 대대댓글',1000,1,67,3,'20240506124700',NULL,NULL);
```

#### 게시글 별 댓글 조회

##### 흐름

`NoticeController` ➭ `BoardService` ➭ `BoardCommentDao`
`readPage()` ➭ `findComments()` ➭ `findComments()` (파라미터, 리턴 타입 추후 보완 예정)

- `BoardCommentDao`의 `findComments()`

사용된 쿼리는 다음과 같다.

```sql
SELECT a.*, m.member_id
FROM (
	SELECT
		p.comment_seq,
		p.lvl,
		p.content,
		p.board_seq,
		p.board_type_seq,
		p.member_seq,
		IFNULL(p.parent_comment_seq, s.parent_comment_seq) as parent_comment_seq,
		p.reg_dtm
	FROM board_comment p
	LEFT JOIN board_comment s ON s.parent_comment_seq = p.comment_seq
) a
JOIN member m ON a.member_seq = m.member_seq
WHERE board_seq = ? AND board_type_seq = ?
ORDER BY IFNULL(parent_comment_seq, 9999999), a.reg_dtm, a.comment_seq;
```

Mock 데이터가 삽입된 후 정렬했을 때는 다음과 같은 결과가 나와야 한다.

```
(lvl, content, board_seq, board_type_seq, member_seq, parent_comment_seq, reg_dtm, update_dtm, delete_dtm)
(0,'1번 댓글', 1000, 1, 67, NULL, '20240506124700', NULL, NULL)
(1,'1번 대댓글', 1000, 1, 67, 1, '20240506124700', NULL, NULL)
(2,'1번 대대댓글', 1000, 1, 67, 3, '20240506124700', NULL, NULL)
(0,'2번 댓글', 1000, 1, 67, NULL, '20240506124700', NULL, NULL)
```

안 쪽 쿼리에서 `p.parent_comment_seq`가 `null`인 경우는 대댓글이 아닌 댓글인 경우이다. 이때 대댓글이 달려 있는 경우를 생각해보자. `null`은 값이 있는 경우보다 오름차순 정렬 시 우선 순위에서 밀리기 때문에 오름차순 정렬을 위해 임의로 `s.parent_comment_seq`로 변경해준다.

정렬 기준을 차례대로 보자.

1. `parent_comment_seq`: 이 값이 `null`인 경우는 정수 최댓값으로 값을 대체하는데 해당 행을 대댓글과 엮이지 않도록 가장 마지막으로 보내고 다음 기준을 이용해 정렬하게 한다. 대댓글이 없는 경우 해당 기준을 무효화한다고 생각하면 되겠다.
2. `reg_dtm`: 대댓글이 있는 경우에는 `lvl`을 사용해도 되지만 대댓글이 없는 경우를 위해 해당 정렬 기준을 사용한다.
3. `comment_seq`: 앞의 두 기준이 같은 경우 구별하기 위해 해당 기준을 사용한다.

#### 게시글 별 댓글 작성

##### 흐름

`read.jsp` ➭ `RestReplyController` ➭ `BoardCommentService` ➭ `BoardCommentDao`
`leaveReplyOrComment()` ➭ `reply()` ➭ `addBoardComment()` (파라미터, 리턴 타입 추후 보완 예정)

- `read.jsp`의 `leaveReplyOrComment()`

  - 파라미터 중 `contentId`는 `'trumbowyg-demo'`로 대댓글이 아닌 댓글 전용 `trumbowyg` 에디터(`.comment-form-area`)의 값을 갖고 온다.
  - 속성 `data-parent-comment-seq`의 값은 `0`이다.

#### 게시글 별 대댓글 작성

대댓글의 경우 각 댓글 별 `대댓글 달기` 버튼을 눌렀을 떄 해당 댓글에 `trumbowyg` 에디터가 나타나야 한다. 대댓글 전용 `trumbowyg` 에디터(`.comment-form-area.edit`)을 사용한다.

##### 흐름

`read.jsp` ➭ `RestReplyController` ➭ `BoardCommentService` ➭ `BoardCommentDao`
`showCommentFormAreaReply()`, `leaveReplyOrComment()` ➭ `reply()` ➭ `addBoardComment()` ➭ `addBoardComment()` (파라미터, 리턴 타입 추후 보완 예정)

- `read.jsp`의 `leaveReplyOrComment()`

  - 파라미터 중 `contentId`는 `'trumbowyg-reply'`로 대댓글 전용 `trumbowyg` 에디터(`.comment-form-area.reply`)의 값을 갖고 온다.
  - 속성 `data-lvl`의 값은 대댓글을 달고 싶은 댓글로부터 전달되는 값이다. `showCommentFormAreaReply()`를 통해 `대댓글 달기` 버튼을 눌렀을 때 `trumbowyg` 에디터(`.comment-form-area.reply`)`<button>`에 `data-lvl`의 값이 전달된다.
  - `data-parent-comment-seq` 또한 전달된다.

#### 게시글 별 댓글 삭제

##### 흐름

`read.jsp` ➭ `RestReplyController` ➭ `BoardCommentService` ➭ `BoardCommentDao`
`deleteReply()` ➭ `deleteReply()` ➭ `deleteReply()` ➭ `deleteReply()` (파라미터, 리턴 타입 추후 보완 예정)

- `deleteReply()`: 성공한 경우 `'1'`이 ajax의 비동기 처리 결과 값으로 전달되고 `read.jsp`의 페이지가 새로고침됨

#### 게시글 별 댓글 수정

##### 흐름

`read.jsp` ➭ `RestReplyController` ➭ `BoardCommentService` ➭ `BoardCommentDao`
`showCommentFormAreaEdit()`, `editReplyOrComment()` ➭ `editReply()` ➭ `editReply()` ➭ `updateReply()` (파라미터, 리턴 타입 추후 보완 예정)

- `read.jsp`의 `editReplyOrComment()`

  - 속성 `data-comment-seq`의 값은 수정하고 싶은 댓글로부터 전달되는 값이다. `showCommentFormAreaEdit()`를 통해 `수정` 버튼을 눌렀을 때 `trumbowyg` 에디터(`.comment-form-area.edit`)`<button>`에 `data-comment-seq`의 값이 전달된다.

---

## 🔨 기능 요구사항

### 프로젝트 환경 설정하기

- [x] servlet

- [x] spring

- [x] spring jdbc

- [x] logback

### Servlet 구성 및 접속

#### `NoticeController`

- [x] 게시물 목록 페이지 : `/forum//notice/listPage.do`

- [x] 게시물 단건 조회 페이지 : `/forum/notice/readPage.do`

- [x] 게시물 쓰기 페이지 : `/forum/notice/writePage.do`

- [x] 게시물 쓰기 : `/forum/notice/write.do`

- [x] 게시물 수정 페이지 : `/forum/notice/modifyPage.do`

- [x] 게시물 수정 : `/forum/notice/modify.do`

#### `RestNoticeController`

- [x] 게시물 별 좋아요/싫어요 반영: `/forum/notice/{boardSeq}/{boardTypeSeq}/vote.do`

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
...
```

---

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

---

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

---

### 게시물 별 좋아요/싫어요 조회

#### 문제 상황

게시물 단건 조회 시 좋아요/싫어요 반영 여부를 확인해야 한다. `boardDao`의 `findIsLikeByBoardSeqAndBoardTypeSeqAndMemberSeq()`를 통해 다음과 같은 `sql` 쿼리를 실행하게 된다.

```sql
SELECT is_like
FROM forum.board_vote
WHERE board_seq = ? AND board_type_seq = ? AND member_seq = ?;
```

만약 해당 레코드가 없는 경우 다음과 같은 예외가 발생한다.

```
Request processing failed; nested exception is org.springframework.dao.EmptyResultDataAccessException: Incorrect result size: expected 1, actual 0
com.pf.www.forum.notice.dao.BoardDao.findIsLikeByBoardSeqAndBoardTypeSeqAndMemberSeq(BoardDao.java:72)
```

#### 해결 방법

```java
public String findIsLikeByBoardSeqAndBoardTypeSeqAndMemberSeq(Integer boardSeq, Integer boardTypeSeq, Integer memberSeq) {
	try {
		return boardDao.findIsLikeByBoardSeqAndBoardTypeSeqAndMemberSeq(boardSeq, boardTypeSeq, memberSeq);
	} catch (EmptyResultDataAccessException e) {
		return "Empty";
	}
}
```

위와 같이 `try-catch`문을 통해 예외 처리를 해주었다. 이후 해당 `"Empty"`는 `model`로 `read.jsp`로 전달되는데 `"Empty"`인 경우에는 좋아요 또는 싫어요 어떤 것도 표시되지 않는다.

---

### 게시물 별 좋아요/싫어요 반영 (1)

#### 문제 상황

`INSERT` 쿼리만을 통해 게시물 별 좋아요/싫어요를 반영할 때 사용자가 처음으로 좋아요 또는 싫어요를 누를 때는 문제가 없었지만 그 다음 좋아요 또는 싫어요를 누를 때는 같은 문제가 발생했다.

```
org.springframework.dao.DuplicateKeyException
SQL [INSERT INTO forum.board_vote (board_seq, board_type_seq, member_seq, is_like, reg_dtm) VALUES(?, ?, ?, ?, DATE_FORMAT(NOW(), '%Y%m%d%H%i%s')); ];
Duplicate entry '1-1-1' for key 'board_vote.PRIMARY';
```

`board_vote`의 `pk`는 `(board_seq, board_type_seq, member_seq)`와 같다. 사용자가 좋아요/싫어요를 처음이 아닌 경우 클릭할 때 이미 존재하는 `pk`로 또 다시 `INSERT`를 하려 하기 때문에 위와 같은 에러가 발생하는 것이다.

#### 해결 방법

위와 같은 에러가 발생하는 경우를 `service` 계층에서 `try-catch`문으로 잡아 `INSERT` 대신 `UPDATE`로 쿼리를 실행했다.

```java
public int vote(Integer boardSeq, Integer boardTypeSeq, Integer memberSeq, String isLike) {
	try { // 처음 좋아요/싫어요를 하는 경우
		return boardDao.addVote(boardSeq, boardTypeSeq, memberSeq, isLike);
	} catch (DuplicateKeyException dke) { // 좋아요/싫어요가 이미 있는 경우
		// 같은 좋아요 또는 싫어요를 한번 더 눌렀을 경우
		if (boardDao.cntVote(boardSeq, boardTypeSeq, memberSeq, isLike) == 1) {
			boardDao.deleteVote(boardSeq, boardTypeSeq, memberSeq);
			return 2;
		}

		// 좋아요 -> 싫어요 OR 싫어요 -> 좋아요
		return boardDao.updateVote(boardSeq, boardTypeSeq, memberSeq, isLike);
	}
}
```

---

### 게시물 별 좋아요/싫어요 반영 (2)

#### 문제 상황

하지만 (1)과 같은 해결 방법은 좋아요/싫어요를 누를 때마다 예외 객체를 생성하는 문제가 발생한다. 이와 같은 예외 객체는 `Stack`과 `Heap`에 쌓이게 되는데 다량의 좋아요/싫어요 클릭이 발생할 경우 `StackOverflow`가 발생할 수도 있기 떄문에 다음과 같이 변경했다.

#### 해결 방법

```java
public int vote(Integer boardSeq, Integer boardTypeSeq, Integer memberSeq, String isLike) {
		// 처음 좋아요/싫어요를 하는 경우
		if (boardDao.cntVote(boardSeq, boardTypeSeq, memberSeq) == 0) {
			return boardDao.addVote(boardSeq, boardTypeSeq, memberSeq, isLike);
		// 좋아요/싫어요가 이미 있는 경우
		} else { // boardDao.cntVote(boardSeq, boardTypeSeq, memberSeq) == 1
			// 같은 좋아요 또는 싫어요를 한번 더 눌렀을 경우
			if (boardDao.cntVote(boardSeq, boardTypeSeq, memberSeq, isLike) == 1) {
				boardDao.deleteVote(boardSeq, boardTypeSeq, memberSeq);
				return 2;
			}

			// 좋아요 -> 싫어요 OR 싫어요 -> 좋아요
			return boardDao.updateVote(boardSeq, boardTypeSeq, memberSeq, isLike);
		}
}
```

---

### jQuery 또는 $ is not defined

#### 문제 상황

`write.jsp` 페이지 조회 후 브라우저 콘솔 창에 다음과 같은 오류를 확인했다. `Uncaught ReferenceError: jQuery is not defined`, `Uncaught ReferenceError: $ is not defined`

`html`은 순차적으로 문서를 해석한다. `write.jsp`의 `<head>` 태그에는 다음과 같은 `<script>` 태그를 포함하는데 44, 45번째 줄의 `.js` 파일, 그리고 53번째 줄의 `$` 모두 `jQuery`를 필요로 한다. 하지만 `jQuery`를 불러오는 `<script>` 태그는 `<body>` 태그 안에 존재하기 때문에 이와 같은 에러가 발생한 것이다.

```html
44
<script src="<%=ctx%>/assest/template/js/vendor/trumbowyg.min.js"></script>
45
<script src="<%=ctx%>/assest/template/js/vendor/trumbowyg/ko.js"></script>
46
<script type="text/javascript">
  47	$('#trumbowyg-demo')
  48	.trumbowyg({
  49			lang: 'kr'
  50	})
  51
  52	window.onload = function() {
  53		$('#trumbowyg-demo').on('tbwchange', function(){
  54			// console.log($('#content').value = $(this).text());
  55			$('#content').val($(this).text());
  56		});
  57	}
  58
</script>
```

#### 해결 방법

43번째 줄에 `<script src="http://code.jquery.com/jquery-latest.js"></script>`를 추가해주었다.

---

### `@ResponseBody`가 붙은 경우 리턴 타입에 상관없이 모두 문자열로 해석한다.

#### 문제 상황

다음은 `read.jsp`에서 좋아요 또는 싫어요 버튼을 누른 후 실행되는 성공 콜백 함수이다.

```javascript
// 결과 성공 콜백함수
success : function(result) {
	// console.log(typeof result);
	if (result === 1 && isLike === 'Y') {
		$('a#cThumbsUpAnchor').addClass('active');
		$('a#cThumbsDownAnchor').removeClass('active');
	} else if (result === 1 && isLike === 'N') {
		$('a#cThumbsUpAnchor').removeClass('active');
		$('a#cThumbsDownAnchor').addClass('active');
	} else if (result === 2) {
		$('a#cThumbsUpAnchor').removeClass('active');
		$('a#cThumbsDownAnchor').removeClass('active');
	}
},
```

위의 `if-else-if`문 중 어떤 블록도 수행되지 않음을 확인할 수 있었다. `RestNoticeController`의 `vote()`의 리턴 타입은 `int`지만 HTTP 메시지 컨버터를 통해 `String`으로 변환된다. 값 뿐만 아니라 타입까지 확인하는 `===` 연산의 결과가 `false`였기 때문에 어떤 블록도 수행되지 않은 것이다.

#### 해결 방법

```javascript
// 결과 성공 콜백함수
success : function(result) {
	// console.log(typeof result);
	if (result === '1' && isLike === 'Y') {
		$('a#cThumbsUpAnchor').addClass('active');
		$('a#cThumbsDownAnchor').removeClass('active');
	} else if (result === '1' && isLike === 'N') {
		$('a#cThumbsUpAnchor').removeClass('active');
		$('a#cThumbsDownAnchor').addClass('active');
	} else if (result === '2') {
		$('a#cThumbsUpAnchor').removeClass('active');
		$('a#cThumbsDownAnchor').removeClass('active');
	}
},
```

위와 같이 문자열 타입을 비교하도록 변경해주었다.

---

### `Calendar`의 `JANUARY` 상수는 `0`이다.

#### 문제 상황

```java
Calendar calendar = Calendar.getInstance();
int year = calendar.get(Calendar.YEAR);
int month = calendar.get(Calendar.MONTH);
```

오늘이 만약 2024년 5월 15일인 경우 `/file/2024/4/15`라는 폴더가 생성된다.

#### 해결 방법

`Calendar`의 `JANUARY` 상수는 `0`이기 때문에 `month`의 값을 구할 때 `+1`을 해주었다.

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

---

### 식별 vs. 비식별 관계

- 식별: 부모 테이블에 데이터가 존재해야 자식 테이블에 데이터를 입력할 수 있음

  예) `자동차 테이블`에서 `자동차_아이디`가 `pk`일 때 `바퀴 테이블`에서 `자동차_아이디`를 `pk`로 갖는 경우

- 비식별: 자식 데이터는 부모 데이터가 없어도 독립적으로 생성될 수 있음

  예) `자동차 테이블`에서 `자동차_아이디`가 `pk`일 때 `자동차_아이디`가 `pk`, `바퀴 테이블`에서 `자동차_아이디`를 `pk`로 갖지 않는 경우
