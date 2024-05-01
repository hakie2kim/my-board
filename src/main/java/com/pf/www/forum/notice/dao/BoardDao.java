package com.pf.www.forum.notice.dao;

import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.pf.www.forum.notice.dto.BoardDto;

@Repository
public class BoardDao extends JdbcTemplate {
	@Autowired
	BoardDao (DataSource dataSource) {
		super(dataSource);
	};
	
	public List<BoardDto> findAll(HashMap<String, String> params) {
		String sql = "SELECT b.board_seq, b.board_type_seq, b.title, b.content, b.hit, b.del_yn, b.reg_dtm, b.reg_member_seq, b.update_dtm, b.update_member_seq, m.member_id "
				+ "FROM forum.`board` b "
				+ "JOIN forum.`member` m "
				+ "ON b.reg_member_seq = m.member_seq "
				+ "LIMIT ?, ?; ";
		Object[] args = {Integer.parseInt(params.get("currPage")) - 1, Integer.parseInt(params.get("postsPerPage"))};
		return query(sql, boardRowMapper(), args);
	}
	
	public BoardDto findBoardByBoardSeq(String boardSeq) {
		String sql = "SELECT b.board_seq, b.board_type_seq, b.title, b.content, b.hit, b.del_yn, b.reg_dtm, b.reg_member_seq, b.update_dtm, b.update_member_seq, m.member_id "
				+ "FROM forum.`board` b "
				+ "JOIN forum.`member` m "
				+ "ON b.reg_member_seq = m.member_seq "
				+ "WHERE b.board_seq = ?; ";
		Object[] args = {boardSeq};
		return queryForObject(sql, boardRowMapper(), args);
	}
	
	private RowMapper<BoardDto> boardRowMapper() {
		return (rs, rowNum) -> {
			BoardDto boardDto = new BoardDto();
			boardDto.setBoardSeq(rs.getInt("board_seq"));
			boardDto.setBoardTypeSeq(rs.getInt("board_type_seq"));
			boardDto.setTitle(rs.getString("title"));
			boardDto.setContent(rs.getString("content"));
			boardDto.setHit(rs.getInt("hit"));
			boardDto.setDelYn(rs.getString("del_yn"));
			boardDto.setRegDtm(rs.getString("reg_dtm"));
			boardDto.setRegMemberSeq(rs.getInt("reg_member_seq"));
			boardDto.setUpdateDtm(rs.getString("update_dtm"));
			boardDto.setUpdateMemberSeq(rs.getInt("update_member_seq"));

			boardDto.setMemberId(rs.getString("member_id"));
			
			return boardDto;
		};
	}
}
