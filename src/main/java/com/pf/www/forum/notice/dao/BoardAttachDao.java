package com.pf.www.forum.notice.dao;

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
}
