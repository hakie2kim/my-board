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
	
	public List<BoardDto> findAllBoards(HashMap<String, String> params) {
		return boardDao.findAll(params);
	}
	
	public BoardDto findBoardByBoardSeq(String boardSeq) {
		return boardDao.findBoardByBoardSeq(boardSeq);
	}
}