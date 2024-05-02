package com.pf.www.forum.notice.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	public BoardDto findBoardByBoardSeq(String boardSeq) {
		return boardDao.findBoardByBoardSeq(boardSeq);
	}

	public int cntTotalBoards() {
		return boardDao.cntTotalBoards();
	}
}
