package com.pf.www.forum.notice.controller.rest;

import java.io.File;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pf.www.forum.notice.dto.BoardAttachDto;
import com.pf.www.forum.notice.dto.BoardDto;
import com.pf.www.forum.notice.service.BoardService;

@RestController
public class RestNoticeController {
	private final BoardService boardService;
			
	public RestNoticeController(BoardService boardService) {
		this.boardService = boardService;
	}

	// 좋아요, 싫어요 테이블을 나눠서 관리 하지 않은 경우
	@GetMapping("/forum/notice/{boardSeq}/{boardTypeSeq}/vote.do")
	public int vote(
			@PathVariable Integer boardSeq, 
			@PathVariable Integer boardTypeSeq, 
			@RequestParam String isLike,
			HttpServletRequest request
			) {
		HttpSession session = request.getSession();
		Integer memberSeq = (Integer) session.getAttribute("memberSeq");
		// 회원가입 기능 연결: 1 -> memberSeq		
		return boardService.vote(boardSeq, boardTypeSeq, 1, isLike);
	}
	
	
	@DeleteMapping("/forum/notice/{attSeq}/removeFile.do")
	public int removeFile(@PathVariable Integer attSeq) {
		return boardService.removeFile(attSeq);
	}
}
