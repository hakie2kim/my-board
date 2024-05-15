package com.pf.www.forum.notice.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.pf.www.forum.notice.dto.BoardAttachDto;

@Repository
public class BoardAttachDao extends JdbcTemplate {
	BoardAttachDao (DataSource dataSource) {
		super(dataSource);
	}

	public int addBoardAttach(BoardAttachDto boardAttachDto) {
		String sql = "INSERT INTO forum.board_attach "
				+ "(board_seq, board_type_seq, org_file_nm, save_path, chng_file_nm, file_size, file_type, access_uri, reg_dtm) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, DATE_FORMAT(NOW(),'%Y%m%d%H%i%s'));";
		Object[] args = {
					boardAttachDto.getBoardSeq(), boardAttachDto.getBoardTypeSeq(), 
					boardAttachDto.getOrgFileNm(), boardAttachDto.getSavePath(), 
					boardAttachDto.getChngFileNm(), boardAttachDto.getFileSize(), 
					boardAttachDto.getFileType(), boardAttachDto.getAccessUri()
				};
		return update(sql, args);
	}
	

	public List<BoardAttachDto> findBoardAttList(Integer boardSeq, Integer boardTypeSeq) {
		String sql = "SELECT * FROM forum.board_attach "
				+ "WHERE board_seq = ? AND board_type_seq = ? ";
		Object[] args = {boardSeq, boardTypeSeq};
		return query(sql, boardAttRowMapper(), args);		
	}
	
	
	public BoardAttachDto findBoardAtt(Integer attSeq) {
		String sql = "SELECT * FROM forum.board_attach "
				+ "WHERE attach_seq = ? ";
		Object[] args = {attSeq};
		return queryForObject(sql, boardAttRowMapper(), args);
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

	public int deleteBoardAtt(Integer attSeq) {
		String sql = "DELETE FROM forum.board_attach "
				+ "WHERE attach_seq = ? ";
		Object[] args = {attSeq};		
		return update(sql, args);
	}
}
