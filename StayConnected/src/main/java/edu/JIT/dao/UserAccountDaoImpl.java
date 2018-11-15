package edu.JIT.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import edu.JIT.mapper.roleMapper;
import edu.JIT.mapper.skillMapper;
import edu.JIT.model.JobHistory;
import edu.JIT.model.RegistrationForm;
import edu.JIT.model.Skill;
import edu.JIT.model.UserAccount;

@Repository
public class UserAccountDaoImpl implements UserAccountDao {

	@Autowired
	private DataSourceTransactionManager transactionManager;

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
	}

	@Override
	public UserAccount createNewAccount(final RegistrationForm account) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			String userAccountSQL = "insert into stayconnected.userAccount(RID,Fname,Lname,email,address,phone_number) values(?,?,?,?,?,?)";
			jdbcTemplate.update(userAccountSQL, account.getAccount().getRoyalID(), account.getAccount().getFirstName(),
					account.getAccount().getLastName(), account.getAccount().getEmail(),
					account.getAccount().getUserAddress(), account.getAccount().getPhoneNumber());
			String jobHistorySQL = "insert into stayconnected.jobHistory(position,companyName,address,startDate,endDate,currentlyemplyed,RID) values(?,?,?,?,?,?,?)";
			ArrayList<JobHistory> usersWorkExp = account.getAccount().getWorkExperience();
			for (int i = 0; i < usersWorkExp.size(); i++) {
				if (!usersWorkExp.get(i).getPosition().equals("")) {
					System.out.println(usersWorkExp.get(i).getPosition());
					jdbcTemplate.update(jobHistorySQL, usersWorkExp.get(i).getPosition(),
							usersWorkExp.get(i).getCompanyName(), usersWorkExp.get(i).getAddress(),
							usersWorkExp.get(i).getStartDate(), usersWorkExp.get(i).getEndDate(),
							usersWorkExp.get(i).isCurrentlyEmployed(), account.getAccount().getRoyalID());
				}
			}
			String userSkillsSQL = "INSERT INTO stayconnected.UserSkills(RID,skillID,proficiency) VALUES(?,?,?)";
			for (int i = 0; i < account.getAccount().getSkill().size(); i++) {
				jdbcTemplate.update(userSkillsSQL, account.getAccount().getRoyalID(),
						account.getAccount().getSkill().get(i).getSkillID(),
						account.getAccount().getSkill().get(i).getProficiency());
			}
			String userAuthoritySQL = "INSERT INTO stayconnected.Authority(RID,UserRoleID) VALUES(?,?)";
			ArrayList<String> usersRoles = account.getAccount().getRoles();
			for (int i = 0; i < usersRoles.size(); i++) {
				int userRoleId = 0;
				if (usersRoles.get(i).equalsIgnoreCase("ROLE_CURR")) {
					userRoleId = 1;
				} else if (usersRoles.get(i).equalsIgnoreCase("ROLE_ALUM")) {
					userRoleId = 2;
				} else if (usersRoles.get(i).equalsIgnoreCase("ROLE_FACULTY")) {
					userRoleId = 3;
				}
				jdbcTemplate.update(userAuthoritySQL, account.getAccount().getRoyalID(), userRoleId);
			}
			if (!account.getSpecialCode().equals("")) {
				String userActivationSQL = "INSERT INTO stayconnected.UserActivation(code,expiration,RID) values(?,?,?)";
				jdbcTemplate.update(userActivationSQL, account.getSpecialCode(), LocalDate.now(),
						account.getAccount().getRoyalID());
			}
			String userLoginSQL = "INSERT INTO stayconnected.UserLogin(RID,password) values(?,?)";
			jdbcTemplate.update(userLoginSQL, account.getAccount().getRoyalID(), account.getPassword());
			transactionManager.commit(status);
		} catch (DataAccessException e) {
			System.out.println("Error in creating product record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
		return null;
	}

	@Override
	public UserAccount getAccountByRoyalID(String royalID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserAccount> getAllAccounts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteAccount(UserAccount account) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(RegistrationForm webAccount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<String> getRoles() {
		String SQL = "SELECT * from stayconnected.UserRoles";
		List<String> roleresults;
		ArrayList<String> roles = new ArrayList<String>();
		roleresults = jdbcTemplate.query(SQL, new roleMapper());
		for (int i = 0; i < roleresults.size(); i++) {
			roles.add(roleresults.get(i));
		}
		return roles;
	}

	@Override
	public List<Skill> getSkills() {
		String SQL = "SELECT * from stayconnected.skills";
		List<Skill> skillresults;
		ArrayList<Skill> skills = new ArrayList<Skill>();
		skillresults = jdbcTemplate.query(SQL, new skillMapper());
		for (int i = 0; i < skillresults.size(); i++) {
			skills.add(skillresults.get(i));
		}

		return skills;
	}
}