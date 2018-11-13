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

import edu.JIT.model.RegistrationForm;
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
		// TODO Auto-generated method stub
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			String sql = "insert into stayconnected.userAccount(RID,Fname,Lname,email,address,phone_number) values(?,?,?,?,?,?)";
			jdbcTemplate.update(sql, account.getAccount().getRoyalID(), account.getAccount().getFirstName(),
					account.getAccount().getLastName(), account.getAccount().getEmail(),
					account.getAccount().getUserAddress(), account.getAccount().getPhoneNumber());
			String sql1 = "insert into stayconnected.jobHistory(position,companyName,address,startDate,endDate,currentlyEmployed,RID) values(?,?,?,?,?,?,?)";
			for (int i = 0; i < account.getAccount().getWorkExperience().size(); i++) {
				jdbcTemplate.update(sql1, account.getAccount().getWorkExperience().get(i).getPosition(),
						account.getAccount().getWorkExperience().get(i).getCompanyName(),
						account.getAccount().getWorkExperience().get(i).getAddress(),
						account.getAccount().getWorkExperience().get(i).getStartDate(),
						account.getAccount().getWorkExperience().get(i).getEndDate(),
						account.getAccount().getWorkExperience().get(i).isCurrentlyEmployed(),
						account.getAccount().getRoyalID());
			}
			String sql2 = "INSERT INTO stayconnected.UserSkills(RID,skillID,proficiancy) VALUES(?,?,?)";
			for (int i = 0; i < account.getAccount().getSkill().size(); i++) {
				jdbcTemplate.update(sql2, account.getAccount().getRoyalID(),
						account.getAccount().getSkill().get(i).getSkillID(),
						account.getAccount().getSkill().get(i).getProficiency());
			}
			String sql3 = "INSERT INTO stayconnected.Authority(RID,UserRoleID) VALUES(?,?)";
			for (int i = 0; i < account.getAccount().getRoles().size(); i++) {
				int userRoleId = 0;
				if (account.getAccount().getRoles().get(i) == "ROLE_CURR") {
					userRoleId = 1;
				} else if (account.getAccount().getRoles().get(i) == "ROLE_ALUM") {
					userRoleId = 2;
				} else if (account.getAccount().getRoles().get(i) == "ROLE_FACULTY") {
					userRoleId = 3;
				}
				jdbcTemplate.update(sql3, account.getAccount().getRoyalID(), userRoleId);
			}
			String sql4 = "INSERT INTO stayconnected.UserActivation(code,expiration,RID) values(?,?,?)";
			jdbcTemplate.update(sql4, account.getSpecialCode(), LocalDate.now(), account.getAccount().getRoyalID());
			String sql5 = "INSERT INTO stayconnected.UserLogin(RID,password) values(?,?)";
			jdbcTemplate.update(sql5, account.getAccount().getRoyalID(), account.getPassword());
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
		String SQL = "SELECT * from stayconnected.roles;";
		List<String> roleresults;
		ArrayList<String> roles = new ArrayList<String>();
		roleresults = jdbcTemplate.query(SQL, new edu.JIT.mapper.roleMapper());
		for(int i=0; i<roleresults.size(); i++) {
			roles.add(roleresults.get(i));
		}
		
		return roles;
	}

	@Override
	public List<String> getSkills() {
		String SQL = "SELECT * from stayconnected.skills;";
		List<String> skillresults;
		ArrayList<String> skills = new ArrayList<String>();
		skillresults = jdbcTemplate.query(SQL, new edu.JIT.mapper.skillMapper());
		for(int i=0; i<skillresults.size(); i++) {
			skills.add(skillresults.get(i));
		}
		
		return skills;
	}

}
