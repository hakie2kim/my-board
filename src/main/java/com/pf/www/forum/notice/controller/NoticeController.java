package com.pf.www.forum.notice.controller;

import java.util.Calendar;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
	
	@RequestMapping("/forum/notice/readPage.do")
	public ModelAndView readPage(@RequestParam HashMap<String, String> params) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("key", Calendar.getInstance().getTimeInMillis());
		mv.addObject("board", boardService.findBoardByBoardSeq(params.get("boardSeq")));
		mv.setViewName("forum/notice/read");
		
		return mv;
	}

}
