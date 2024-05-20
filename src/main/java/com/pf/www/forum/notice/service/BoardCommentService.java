package com.pf.www.forum.notice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pf.www.forum.notice.dao.BoardCommentDao;
import com.pf.www.forum.notice.dto.BoardCommentDto;

@Service
public class BoardCommentService {
	@Autowired
	private BoardCommentDao boardCommentDao;
	
	public int reply(BoardCommentDto boardCommentDto) {
		return boardCommentDao.addBoardComment(boardCommentDto);
	}

	public List<BoardCommentDto> findComments(Integer boardSeq, Integer boardTypeSeq) {
		return boardCommentDao.findComments(boardSeq, boardTypeSeq);
	}

	public int deleteReply(Integer commentSeq) {
		return boardCommentDao.deleteReply(commentSeq);
	}

	public int editReply(BoardCommentDto boardCommentDto) {
		return boardCommentDao.updateReply(boardCommentDto);
	}

}
