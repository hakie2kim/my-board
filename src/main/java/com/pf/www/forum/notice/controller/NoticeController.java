package com.pf.www.forum.notice.controller;

import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pf.www.forum.notice.dto.BoardDto;
import com.pf.www.forum.notice.service.BoardService;
import com.pf.www.forum.notice.util.Pagination;

@Controller
public class NoticeController {
	@Autowired
	BoardService boardService;
	
	@RequestMapping("/forum//notice/listPage.do")
	public ModelAndView listPage(@RequestParam HashMap<String, String> params,
			@RequestParam(defaultValue="1") Integer page,
			@RequestParam(defaultValue="10") Integer size
			) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("key", Calendar.getInstance().getTimeInMillis());
		mv.addObject("list", boardService.findAllBoards(page, size));
		
		// 추후 게시판 타입에 따라 cntTotalBoards에 인자를 넘길 수 있음
		mv.addObject("pagination", new Pagination(boardService.cntTotalBoards(), page, size));
		
		mv.setViewName("forum/notice/list");
		
		return mv;
	}
	
	@RequestMapping("/forum/notice/writePage.do")
	public ModelAndView writePage(@RequestParam HashMap<String, String> params) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("key", Calendar.getInstance().getTimeInMillis());
		mv.setViewName("forum/notice/write");
		
		return mv;
	}
	
	
	@PostMapping("/forum/notice/write.do")
	public String write(
			// @RequestParam HashMap<String, String> params,
			@ModelAttribute BoardDto boardDto,
			HttpServletRequest request
			) {
		// System.out.println(params);
		
		// 추후 변경 예정
		boardDto.setBoardTypeSeq(1);
		HttpSession session = request.getSession();
		Integer memberSeq = (Integer) session.getAttribute("memberSeq");
		boardDto.setRegMemberSeq(67);
		
		boardService.write(boardDto);
		
		return "redirect:/forum//notice/listPage.do";
	}
	
	@RequestMapping("/forum/notice/readPage.do")
	public ModelAndView readPage(
			@RequestParam HashMap<String, String> params,
			@RequestParam(defaultValue="1") Integer boardSeq,
			@RequestParam(defaultValue="1") Integer boardTypeSeq,
			HttpServletRequest request
			) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("key", Calendar.getInstance().getTimeInMillis());
		mv.addObject("board", boardService.findBoardByBoardSeqAndBoardTypeSeq(boardSeq, boardTypeSeq));
		
		HttpSession session = request.getSession();
		Integer memberSeq = (Integer) session.getAttribute("memberSeq");
		// 회원가입 기능 연결: 1 -> memberSeq
		mv.addObject("isLike", boardService.findIsLikeByBoardSeqAndBoardTypeSeqAndMemberSeq(boardSeq, boardTypeSeq, 1));
		
		mv.setViewName("forum/notice/read");
		
		return mv;
	}

}
