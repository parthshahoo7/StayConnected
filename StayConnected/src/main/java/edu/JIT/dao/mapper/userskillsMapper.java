package edu.JIT.dao.mapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import edu.JIT.dao.daoImplementation.UserSkill;

public class userskillsMapper implements RowMapper<UserSkill> {

	@Override
	public UserSkill mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserSkill mappedSkill = new UserSkill();
		mappedSkill.setRid(rs.getString("rid"));
		mappedSkill.setSkillName(rs.getString("skillname"));
		mappedSkill.setProficiency(rs.getString("proficiency"));
		return mappedSkill;
	}
}