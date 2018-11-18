package edu.JIT.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;

import edu.JIT.model.accountManagement.UserActivation;

public class UserActivationMapper implements RowMapper<UserActivation> {

	@Override
	public UserActivation mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserActivation user = new UserActivation();
		user.setRoyalID(rs.getString("RID"));
		user.setDate(rs.getObject("expiration", LocalDate.class));
		user.setSpecialCode(rs.getString("code"));
		return user;
	}
	
}
