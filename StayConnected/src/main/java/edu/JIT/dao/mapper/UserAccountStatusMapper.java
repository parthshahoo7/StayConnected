package edu.JIT.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class UserAccountStatusMapper implements RowMapper<Boolean> {
	
	@Override
	public Boolean mapRow(ResultSet rs, int rowNum) throws SQLException {
		return rs.getBoolean("status");
	}
	
}
