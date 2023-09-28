package com.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.jdbc.dto.BoardDTO;

public class BoardDAO2 {
	
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}
	
	Connection conn = null;
	
	//num�� �ִ밪
	public int getMaxNum() {
		
		int maxNum = 0;
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select nvl(max(num), 0) from board");
		
		maxNum = jdbcTemplate.queryForInt(sql.toString());	
		
		return maxNum;
		
	}
	
	
	//�Է�
	public void insertData(BoardDTO dto) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("insert into board (num, name, pwd, email, subject, content, ipAddr, hitCount, created) ")
		.append("values (?, ?, ?, ?, ?, ?, ?, 0, sysdate)");
		
		jdbcTemplate.update(sql.toString(),
				dto.getNum(), dto.getName(), dto.getPwd(), dto.getEmail(), dto.getSubject(),
				dto.getContent(), dto.getIpAddr());
		
	}
	
	
	//��ü ������ ����
	public int getDataCount(String searchKey, String searchValue) {
		
		int totalCount = 0;
		
		StringBuilder sql = new StringBuilder();
			
		searchValue = "%" + searchValue + "%";
		
		sql.append("select nvl(count(*), 0) from board ")
		.append("where " + searchKey + " like ?");	
		
		totalCount = jdbcTemplate.queryForInt(sql.toString(), searchValue);
		
		return totalCount;
		
	}
	
	
	//��ü ������ �޾ƿ���
	public List<BoardDTO> getLists(int start, int end, String searchKey, String searchValue){
		
		StringBuilder sql = new StringBuilder();
			
		searchValue = "%" + searchValue + "%";
		
		sql.append("select * from (");
		sql.append("select rownum rnum, data.* from (");
		sql.append("select num, name, subject, hitCount, ");
		sql.append("to_char(created, 'YYYY-MM-DD') created ");
		sql.append("from board where " + searchKey + " like ? order by num desc) data) ");
		sql.append("where rnum >= ? and rnum <= ?");
		
		List<BoardDTO> lists = 
				//obejct[]�� rowmapper�� ������ �ٲ� �ȴ�.
				jdbcTemplate.query(sql.toString(), 
						new Object[] {searchValue,start,end}, 
						new RowMapper<BoardDTO>() {

							@Override
							public BoardDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
								
								BoardDTO dto = new BoardDTO();
								
								dto.setNum(rs.getInt("num"));
								dto.setName(rs.getString("name"));
								dto.setSubject(rs.getString("subject"));
								dto.setHitCount(rs.getInt("hitCount"));
								dto.setCreated(rs.getString("created"));
								
								return dto;
								
							}
					
						});
		
		return lists;
		
	}
	
	
	//��ȸ������
	public void updateHitCount(int num) {
		
		StringBuilder sql = new StringBuilder();
			
		sql.append("update board set hitCount=hitCount+1 where num = ?");
		
		jdbcTemplate.update(sql.toString(), num);
		
	}
	
	
	//num���� �Ѱ��� ������ ��������
	public BoardDTO getReadData(int num) {
		
		StringBuilder sql = new StringBuilder();
			
		sql.append("select num, name, pwd, email, subject, content, ipAddr, ")
		.append("hitCount, to_char(created, 'YYYY-MM-DD HH:mm:SS') created from board where num = ?");
			
		BoardDTO dtoOne = 
				jdbcTemplate.queryForObject(sql.toString(), 
						new RowMapper<BoardDTO>() {

							@Override
							public BoardDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
								
								BoardDTO dto = new BoardDTO();
									
								dto.setNum(rs.getInt("num"));
								dto.setName(rs.getString("name"));
								dto.setPwd(rs.getString("pwd"));
								dto.setEmail(rs.getString("email"));
								dto.setSubject(rs.getString("subject"));
								dto.setContent(rs.getString("content"));
								dto.setIpAddr(rs.getString("ipAddr"));
								dto.setHitCount(rs.getInt("hitCount"));
								dto.setCreated(rs.getString("created"));
								
								return dto;
								
							}
							
						}, num);

		return dtoOne;
		
	}
	
	
	//����
	public void updateData(BoardDTO dto) {
		
		StringBuilder sql = new StringBuilder();
			
		sql.append("update board set name = ?, pwd = ?, email = ?, subject = ?, content = ? where num = ?");
		
		jdbcTemplate.update(sql.toString(), dto.getName(), dto.getPwd(), dto.getEmail(),
				dto.getSubject(), dto.getContent(), dto.getNum());
		
	}
	
	
	//����
	public void deleteData(int num) {
		
		StringBuilder sql = new StringBuilder();
			
		sql.append("delete board where num=?");
		
		jdbcTemplate.update(sql.toString(), num);			
				
	}
	
	

}

