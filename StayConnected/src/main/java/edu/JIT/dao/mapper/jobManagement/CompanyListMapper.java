package edu.JIT.dao.mapper.jobManagement;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.JIT.model.jobManagement.Company;

public class CompanyListMapper implements RowMapper<Company> {

	@Override
	public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		Company company = new Company();
		company.setCompanyName(rs.getString("name"));
		company.setPhoneNumber(rs.getString("phone_number"));
		return company;
	}
}