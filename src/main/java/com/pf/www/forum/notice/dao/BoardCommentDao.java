package com.pf.www.forum.notice.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.pf.www.forum.notice.dto.BoardCommentDto;
import com.pf.www.forum.notice.dto.BoardListDto;

@Repository
public class BoardCommentDao extends JdbcTemplate {
	private DataSource dataSource;
	
	@Autowired
	BoardCommentDao(DataSource dataSource) {
		super(dataSource);
	}
	
	public int addBoardComment(BoardCommentDto boardCommentDto) {
		String sql = "INSERT INTO forum.board_comment "
				+ "(lvl, content, board_seq, board_type_seq, member_seq, parent_comment_seq, reg_dtm) "
				+ "VALUES(?, ?, ?, ?, ?, ?, DATE_FORMAT(NOW(),'%Y%m%d%H%i%s')); ";
		
		Object[] args = {
				boardCommentDto.getLvl(), boardCommentDto.getContent(), boardCommentDto.getBoardSeq(),
				boardCommentDto.getBoardTypeSeq(), boardCommentDto.getMemberSeq(), 
				boardCommentDto.getParentCommentSeq() == 0 ? null : boardCommentDto.getParentCommentSeq()
		};
		
		return update(sql, args);
	}

	public List<BoardCommentDto> findComments(Integer boardSeq, Integer boardTypeSeq) {
		String sql = "SELECT a.*, m.member_nm "
				+ "FROM ( "
				+ "	SELECT "
				+ "		p.comment_seq, "
				+ "		p.lvl, "
				+ "		p.content, "
				+ "		p.board_seq, "
				+ "		p.board_type_seq, "
				+ "		p.member_seq, "
				+ "		IFNULL(p.parent_comment_seq, s.parent_comment_seq) as parent_comment_seq, "
				+ "		p.reg_dtm, "
				+ "		p.update_dtm, "
				+ "		p.delete_dtm "
				+ "	FROM board_comment p "
				+ "	LEFT JOIN board_comment s ON s.parent_comment_seq = p.comment_seq "
				+ ") a "
				+ "JOIN member m ON a.member_seq = m.member_seq "
				+ "WHERE a.board_seq = ? AND a.board_type_seq = ? "
				+ "ORDER BY IFNULL(parent_comment_seq, 9999999), a.reg_dtm, a.comment_seq";
		Object[] args = {boardSeq, boardTypeSeq};
		return query(sql, boardCommentListRowMapper(), args);
	}
	
	private RowMapper<BoardCommentDto> boardCommentListRowMapper() {
		return (rs, rowNum) -> {
			BoardCommentDto boardCommentDto = new BoardCommentDto();
			boardCommentDto.setCommentSeq(rs.getInt("comment_seq"));
			boardCommentDto.setLvl(rs.getInt("lvl"));
			boardCommentDto.setBoardSeq(rs.getInt("board_seq"));
			boardCommentDto.setBoardTypeSeq(rs.getInt("board_type_seq"));
			boardCommentDto.setContent(rs.getString("content"));
			boardCommentDto.setParentCommentSeq(rs.getInt("parent_comment_seq"));
			boardCommentDto.setRegDtm(rs.getString("reg_dtm"));
			boardCommentDto.setMemberSeq(rs.getInt("member_seq"));
			boardCommentDto.setUpdateDtm(rs.getString("update_dtm"));
			boardCommentDto.setDeleteDtm(rs.getString("delete_dtm"));
			boardCommentDto.setMemberNm(rs.getString("member_nm"));
			
			return boardCommentDto;
		};
	}

	public int deleteReply(Integer commentSeq) {
		String sql = "UPDATE forum.board_comment "
				+ "SET delete_dtm = DATE_FORMAT(NOW(),'%Y%m%d%H%i%s') "
				+ "WHERE comment_seq = ?; ";
		Object[] args = {commentSeq};
		return update(sql, args);
	}

	public int updateReply(BoardCommentDto boardCommentDto) {
		String sql = "UPDATE forum.board_comment "
				+ "SET content = ?, update_dtm = DATE_FORMAT(NOW(),'%Y%m%d%H%i%s') "
				+ "WHERE comment_seq = ?; ";
		Object[] args = {boardCommentDto.getContent(), boardCommentDto.getCommentSeq()};
		return update(sql, args);
	}
}
