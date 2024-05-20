package com.pf.www.forum.notice.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pf.www.forum.notice.dto.BoardCommentDto;
import com.pf.www.forum.notice.service.BoardCommentService;

@RestController
public class RestReplyController {
	@Autowired
	private BoardCommentService boardCommentService;
	
	@PostMapping("/forum/notice/reply.do")
	@ResponseBody
	public int reply(@RequestBody BoardCommentDto boardCommentDto) {
		boardCommentDto.setMemberSeq(67);
		return boardCommentService.reply(boardCommentDto);
	}
	
	@PatchMapping("/forum/notice/{commentSeq}/reply.do")
	public int deleteReply(@PathVariable Integer commentSeq) {
		return boardCommentService.deleteReply(commentSeq);
	}
}
