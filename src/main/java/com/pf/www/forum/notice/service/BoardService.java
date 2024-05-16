package com.pf.www.forum.notice.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.pf.www.forum.notice.dao.BoardAttachDao;
import com.pf.www.forum.notice.dao.BoardDao;
import com.pf.www.forum.notice.dto.BoardAttachDto;
import com.pf.www.forum.notice.dto.BoardDto;
import com.pf.www.util.FileUtil;

@Service
public class BoardService {
	@Autowired
	private BoardDao boardDao;

	@Autowired
	private FileUtil fileUtil;

	@Autowired
	private BoardAttachDao boardAttachDao;

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

	public String findIsLikeByBoardSeqAndBoardTypeSeqAndMemberSeq(Integer boardSeq, Integer boardTypeSeq,
			Integer memberSeq) {
		try {
			return boardDao.findIsLikeByBoardSeqAndBoardTypeSeqAndMemberSeq(boardSeq, boardTypeSeq, memberSeq);
		} catch (EmptyResultDataAccessException e) {
			return "Empty";
		}
	}

	public int vote(Integer boardSeq, Integer boardTypeSeq, Integer memberSeq, String isLike) {
		/*
		 * try { // 처음 좋아요/싫어요를 하는 경우 return boardDao.addVote(boardSeq, boardTypeSeq,
		 * memberSeq, isLike); } catch (DuplicateKeyException dke) { // 좋아요/싫어요가 이미 있는
		 * 경우 // 같은 좋아요 또는 싫어요를 한번 더 눌렀을 경우 if (boardDao.cntVote(boardSeq, boardTypeSeq,
		 * memberSeq, isLike) == 1) { boardDao.deleteVote(boardSeq, boardTypeSeq,
		 * memberSeq); return 2; }
		 * 
		 * return boardDao.updateVote(boardSeq, boardTypeSeq, memberSeq, isLike); }
		 */

		// 처음 좋아요/싫어요를 하는 경우
		if (boardDao.cntVote(boardSeq, boardTypeSeq, memberSeq) == 0) {
			return boardDao.addVote(boardSeq, boardTypeSeq, memberSeq, isLike);
			// 좋아요/싫어요가 이미 있는 경우
		} else { // boardDao.cntVote(boardSeq, boardTypeSeq, memberSeq) == 1
			// 같은 좋아요 또는 싫어요를 한번 더 눌렀을 경우
			if (boardDao.cntVote(boardSeq, boardTypeSeq, memberSeq, isLike) == 1) {
				boardDao.deleteVote(boardSeq, boardTypeSeq, memberSeq);
				return 2;
			}

			// 좋아요 -> 싫어요 OR 싫어요 -> 좋아요
			return boardDao.updateVote(boardSeq, boardTypeSeq, memberSeq, isLike);
		}
	}

	public boolean write(BoardDto boardDto, MultipartFile[] mfs) {
		// 1. DB에 게시글 정보 저장
		int boardSeq = boardDao.addBoard(boardDto);

		return uploadFiles(boardDto, mfs, boardSeq);
	}

	public boolean uploadFiles(BoardDto boardDto, MultipartFile[] mfs, int boardSeq) {
		File file = null;
		
		try {
			for (MultipartFile mf : mfs) {
				// 원본 파일 이름이 빈 경우 = 파일 3개 중 하나라도 업로드 하지 않은 경우 -> 그 다음 MultipartFile로 넘어가기
				if (ObjectUtils.isEmpty(mf.getOriginalFilename()))
					continue;
				
				// 2. 물리적 파일 저장
				file = fileUtil.saveFile(mf);

				BoardAttachDto boardAttachDto = new BoardAttachDto();
				boardAttachDto.setBoardSeq(boardSeq);
				boardAttachDto.setBoardTypeSeq(boardDto.getBoardTypeSeq());
				boardAttachDto.setOrgFileNm(mf.getOriginalFilename());
				boardAttachDto.setChngFileNm(file.getName());
				boardAttachDto.setFileType(mf.getContentType());
				boardAttachDto.setFileSize(mf.getSize());
				boardAttachDto.setSavePath(file.getAbsolutePath());

				// 3. DB에 파일 정보 저장
				boardAttachDao.addBoardAttach(boardAttachDto);
			}

			return true;
		} catch (IllegalStateException | IOException e) {
			// 물리적 파일 지우기
			if (ObjectUtils.isEmpty(file))
				file.delete();

			return false;
		}
	}

	public int modfiy(BoardDto boardDto) {
		return boardDao.updateBoard(boardDto);
	}

	public List<BoardAttachDto> findBoardAttList(Integer boardSeq, Integer boardTypeSeq) {
		return boardAttachDao.findBoardAttList(boardSeq, boardTypeSeq);
	}

	public BoardAttachDto findFileInfo(Integer attSeq) { 
		return boardAttachDao.findBoardAtt(attSeq);
	}

	public int removeFile(Integer attSeq) {
		// 1. 지울 파일 정보를 갖고 옴
		BoardAttachDto boardAttachDto = boardAttachDao.findBoardAtt(attSeq);
		
		// 2. 물리적 파일 삭제
		new File(boardAttachDto.getSavePath()).delete();
		
		// 3. DB에 저장한 파일 정보 삭제
		return boardAttachDao.deleteBoardAtt(attSeq);
	}

}
