# 게시판

## 💬 소개

### 게시물 목록

### 페이징

## 🔨 기능 요구사항

### 프로젝트 환경 설정하기

- [x] servlet

- [x] spring

- [x] spring jdbc

- [x] logback

### Servlet 구성 및 접속

- [ ] 게시물 목록 페이지 : `/forum//notice/listPage.do`

- [ ] 게시물 읽기 페이지 : `/forum/notice/writePage.do`

- [ ] 게시물 쓰기 페이지 : `/forum/notice/readPage.do`

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
