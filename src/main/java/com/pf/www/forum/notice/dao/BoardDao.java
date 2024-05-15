package com.pf.www.forum.notice.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.pf.www.forum.notice.dto.BoardAttachDto;
import com.pf.www.forum.notice.dto.BoardDto;

@Repository
public class BoardDao extends JdbcTemplate {
	@Autowired
	BoardDao (DataSource dataSource) {
		super(dataSource);
	};
	
	public List<BoardDto> findAll(Integer startBoardSeq, Integer postsPerPage) {
		String sql = "SELECT b.board_seq, b.board_type_seq, b.title, b.content, b.hit, b.del_yn, b.reg_dtm, b.reg_member_seq, b.update_dtm, b.update_member_seq, m.member_id "
				+ "FROM forum.`board` b "
				+ "JOIN forum.`member` m "
				+ "ON b.reg_member_seq = m.member_seq "
				+ "LIMIT ?, ?; ";
		Object[] args = {startBoardSeq, postsPerPage};
		return query(sql, boardRowMapper(), args);
	}
	
	public BoardDto findBoardByBoardSeqAndBoardTypeSeq(Integer boardSeq, Integer boardTypeSeq) {
		String sql = "SELECT b.board_seq, b.board_type_seq, b.title, b.content, b.hit, b.del_yn, b.reg_dtm, b.reg_member_seq, b.update_dtm, b.update_member_seq, m.member_id "
				+ "FROM forum.`board` b "
				+ "JOIN forum.`member` m "
				+ "ON b.reg_member_seq = m.member_seq "
				+ "WHERE b.board_seq = ? AND b.board_type_seq = ?; ";
		Object[] args = {boardSeq, boardTypeSeq};
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

	public int cntTotalBoards() {
		String sql = "SELECT COUNT(*) FROM forum.`board`";
		return queryForObject(sql, Integer.class);
	}

	public String findIsLikeByBoardSeqAndBoardTypeSeqAndMemberSeq(Integer boardSeq, Integer boardTypeSeq, Integer memberSeq) {
		String sql = "SELECT is_like "
				+ "FROM forum.board_vote "
				+ "WHERE board_seq = ? AND board_type_seq = ? AND member_seq = ?; ";
		Object[] args = {boardSeq, boardTypeSeq, memberSeq};
		return queryForObject(sql, String.class, args);
	}
	
	public int addVote(Integer boardSeq, Integer boardTypeSeq, Integer memberSeq, String isLike) {
		String sql = "INSERT INTO forum.board_vote "
				+ "(board_seq, board_type_seq, member_seq, is_like, reg_dtm) "
				+ "VALUES(?, ?, ?, ?, DATE_FORMAT(NOW(), '%Y%m%d%H%i%s')); ";
		Object[] args = {boardSeq, boardTypeSeq, memberSeq, isLike};
		return update(sql, args);
	}

	public int updateVote(Integer boardSeq, Integer boardTypeSeq, Integer memberSeq, String isLike) {
		String sql = "UPDATE forum.board_vote "
				+ "SET is_like=?, reg_dtm = DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') "
				+ "WHERE board_seq=? AND board_type_seq=? AND member_seq=?;";
		Object[] args = {isLike, boardSeq, boardTypeSeq, memberSeq};
		return update(sql, args);
	}

	public int addBoard(BoardDto boardDto) {
		String sql = "INSERT INTO forum.board "
				+ "(board_type_seq, title, content, reg_dtm, reg_member_seq) "
				+ "VALUES(?, ?, ?, DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), ?);";
		
		KeyHolder keyHolder = new GeneratedKeyHolder(); 
		update(connection -> {
	        PreparedStatement ps = connection
	          .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	          ps.setInt(1, boardDto.getBoardTypeSeq());
	          ps.setString(2, boardDto.getTitle());
	          ps.setString(3, boardDto.getContent());
	          ps.setInt(4, boardDto.getRegMemberSeq());
	          return ps;
	    }, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public int updateBoard(BoardDto boardDto) {
		String sql = "UPDATE forum.board "
				+ "SET title=?, content=?, update_dtm = DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), update_member_seq=? "
				+ "WHERE board_seq=? AND board_type_seq=?;";
		Object[] args = {boardDto.getTitle(), boardDto.getContent(), boardDto.getUpdateMemberSeq(), boardDto.getBoardSeq(), boardDto.getBoardTypeSeq()};
		return update(sql, args);		
	}
	
	public int cntVote(Integer boardSeq, Integer boardTypeSeq, Integer memberSeq) {
		String sql = "SELECT COUNT(*) FROM forum.board_vote "
				+ "WHERE board_seq=? AND board_type_seq=? AND member_seq=?;";
		Object[] args = {boardSeq, boardTypeSeq, memberSeq};
		return queryForObject(sql, Integer.class, args);
	}

	public int cntVote(Integer boardSeq, Integer boardTypeSeq, Integer memberSeq, String isLike) {
		String sql = "SELECT COUNT(*) FROM forum.board_vote "
				+ "WHERE board_seq=? AND board_type_seq=? AND member_seq=? AND is_like=?;";
		Object[] args = {boardSeq, boardTypeSeq, memberSeq, isLike};
		return queryForObject(sql, Integer.class, args);
	}

	public int deleteVote(Integer boardSeq, Integer boardTypeSeq, Integer memberSeq) {
		String sql = "DELETE FROM forum.board_vote "
				+ "WHERE board_seq=? AND board_type_seq=? AND member_seq=?;";
		Object[] args = {boardSeq, boardTypeSeq, memberSeq};
		return update(sql, args);
	}

	public List<BoardAttachDto> findBoardAttList(Integer boardSeq, Integer boardTypeSeq) {
		String sql = "SELECT * FROM forum.board_attach "
				+ "WHERE board_seq = ? AND board_type_seq = ? ";
		Object[] args = {boardSeq, boardTypeSeq};
		return query(sql, boardAttRowMapper(), args);		
	}
	
	private RowMapper<BoardAttachDto> boardAttRowMapper() {
		return (rs, rowNum) -> {
			BoardAttachDto boardAttachDto = new BoardAttachDto();
			boardAttachDto.setAttachSeq(rs.getInt("attach_seq"));
			boardAttachDto.setBoardSeq(rs.getInt("board_seq"));
			boardAttachDto.setBoardTypeSeq(rs.getInt("board_type_seq"));
			boardAttachDto.setOrgFileNm(rs.getString("org_file_nm"));
			boardAttachDto.setSavePath(rs.getString("save_path"));
			boardAttachDto.setChngFileNm(rs.getString("chng_file_nm"));
			boardAttachDto.setFileSize(rs.getLong("file_size"));
			boardAttachDto.setFileType(rs.getString("file_type"));
			boardAttachDto.setAccessUri(rs.getString("access_uri"));
			boardAttachDto.setRegDtm(rs.getString("reg_dtm"));
			
			return boardAttachDto;
		};
	}
	
}
