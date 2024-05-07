package com.pf.www.forum.notice.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pf.www.forum.notice.service.BoardService;

@RestController
public class RestNoticeController {
	private final BoardService boardService;
			
	public RestNoticeController(BoardService boardService) {
		this.boardService = boardService;
	}

	// 좋아요, 싫어요 테이블을 나눠서 관리 하지 않은 경우
	@PostMapping("/forum/notice/{boardSeq}/{boardTypeSeq}/{isLike}/vote.do")
	public int vote(
			@PathVariable Integer boardSeq, 
			@PathVariable Integer boardTypeSeq, 
			@PathVariable String isLike,
			HttpServletRequest request
			) {
		HttpSession session = request.getSession();
		Integer memberSeq = (Integer) session.getAttribute("memberSeq");
		// 회원가입 기능 연결: 1 -> memberSeq		
		return boardService.vote(boardSeq, boardTypeSeq, 1, isLike);
	}
}
