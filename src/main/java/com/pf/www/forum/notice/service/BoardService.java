package com.pf.www.forum.notice.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.pf.www.forum.notice.dao.BoardDao;
import com.pf.www.forum.notice.dto.BoardDto;

@Service
public class BoardService {
	@Autowired
	BoardDao boardDao;
	
	public List<BoardDto> findAllBoards(Integer currPage, Integer postsPerPage) {
		Integer startBoardSeq = (currPage - 1) * postsPerPage;
		return boardDao.findAll(startBoardSeq, postsPerPage);
	}
	
	public BoardDto findBoardByBoardSeqAndBoardTypeSeq(Integer boardSeq, Integer boardTypeSeq) {
		return boardDao.findBoardByBoardSeqAndBoardTypeSeq(boardSeq, boardTypeSeq);
	}

	public int cntTotalBoards() {
		return boardDao.cntTotalBoards();
	}

	public String findIsLikeByBoardSeqAndBoardTypeSeqAndMemberSeq(Integer boardSeq, Integer boardTypeSeq, Integer memberSeq) {
		try {
			return boardDao.findIsLikeByBoardSeqAndBoardTypeSeqAndMemberSeq(boardSeq, boardTypeSeq, memberSeq);
		} catch (EmptyResultDataAccessException e) {
			return "Empty";
		}
	}
	
	public int addVote(Integer boardSeq, Integer boardTypeSeq, Integer memberSeq, String isLike) {
		return boardDao.addVote(boardSeq, boardTypeSeq, memberSeq, isLike);
	}
}
