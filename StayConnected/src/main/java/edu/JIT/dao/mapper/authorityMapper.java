package edu.JIT.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.JIT.dao.daoImplementation.Authority;

public class authorityMapper implements RowMapper<Authority> {

	@Override
	public Authority mapRow(ResultSet rs, int rowNum) throws SQLException {
		Authority mappedAuthority = new Authority();
		mappedAuthority.setRid(rs.getString("rid"));
		mappedAuthority.setRole(rs.getString("role"));
		return mappedAuthority;
	}
}