package edu.JIT.dao.daoImplementation;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import edu.JIT.Controller.form.RegistrationForm;
import edu.JIT.Controller.form.UpdateAccountForm;
import edu.JIT.dao.daoInterfaces.UserAccountDao;
import edu.JIT.dao.mapper.UserAccountStatusMapper;
import edu.JIT.dao.mapper.UserActivationMapper;
import edu.JIT.dao.mapper.accountMapper;
import edu.JIT.dao.mapper.authorityMapper;
import edu.JIT.dao.mapper.jobhistoryMapper;
import edu.JIT.dao.mapper.roleMapper;
import edu.JIT.dao.mapper.skillMapper;
import edu.JIT.dao.mapper.userskillsMapper;
import edu.JIT.model.accountManagement.JobHistory;
import edu.JIT.model.accountManagement.Skill;
import edu.JIT.model.accountManagement.UserAccount;
import edu.JIT.model.accountManagement.UserActivation;

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
	public UserActivation getSpecialCodeByRoyalID(String royalID) {
		try {
			String SQL = "SELECT RID, code, expiration from stayconnected.UserActivation where RID = ?";
			UserActivation userActivation = jdbcTemplate.queryForObject(SQL, new Object[] { royalID }, new UserActivationMapper());
			return userActivation;
		}
		catch (DataAccessException e) {
			System.out.println("Can't find special code");
		}
		return null;
	}
	
	@Override
	public boolean activateAccountByRoyalID(String royalID) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			String SQL = "INSERT INTO stayconnected.AccountStatus(RID,status) values(?,?)";
			jdbcTemplate.update(SQL, royalID, true);
			transactionManager.commit(status);
			return true;
		}
		catch (DataAccessException e) {
			System.out.println("Can't activate Account");
			transactionManager.rollback(status);
			return false;
		}
	}

	@Override
	public UserAccount getAccountByRoyalID(String royalID) {
		UserAccount user = new UserAccount();
		String SQL = "SELECT * from stayconnected.useraccount WHERE rid= '" +
		 royalID + "';" ;
		
		try {
		user = jdbcTemplate.queryForObject(SQL, new Object[] {}, new accountMapper());
		return user; }
		
		catch(DataAccessException e) {
			return null;
		}
	}

	@Override
	public ArrayList<UserAccount> getAllAccounts() {
		ArrayList<UserAccount> users = new ArrayList<UserAccount>();
		String SQL = "SELECT * from stayconnected.useraccount;";
		List<UserAccount> userResults;
		userResults = jdbcTemplate.query(SQL, new accountMapper());
		
		SQL = "SELECT stayconnected.userskills.rid,"
				+ "stayconnected.skills.skillname,"
				+ "stayconnected.userskills.proficiency"
				+ " FROM stayconnected.userskills,"
				+ "stayconnected.skills  "
				+ "WHERE userskills.skillid"
				+ " = skills.skillid;";
		
		List<UserSkill> userskillResults;
		userskillResults = jdbcTemplate.query(SQL, new userskillsMapper());
		
		SQL = "SELECT * from stayconnected.jobhistory;";
		List<JobHistory> jobhistoryResults;
		jobhistoryResults = jdbcTemplate.query(SQL, new jobhistoryMapper());
		
		SQL = "SELECT stayconnected.authority.rid , "
				+ "stayconnected.userroles.role "
				+ " FROM stayconnected.authority , stayconnected.userroles "
				+ "WHERE authority.userroleid = userroles.uid;";
		List<Authority> authorityResults;
		authorityResults = jdbcTemplate.query(SQL, new authorityMapper());
		
		for(int i=0; i<userResults.size(); i++) {
			for(int skill=0; skill<userskillResults.size(); skill++) {
				if(userResults.get(i).getRoyalID().equals(userskillResults.get(skill).getRid())) {
					Skill newSkill = new Skill(-1 , userskillResults.get(skill).getProficiency(),
							userskillResults.get(skill).getSkillName());
					userResults.get(i).addSkills(newSkill);
				}
			}
			for(int history=0; history<jobhistoryResults.size(); history++) {
				if(userResults.get(i).getRoyalID().equals(jobhistoryResults.get(history).getRid())) {
					userResults.get(i).addWorkHistory(jobhistoryResults.get(history));
				}
			}
			for(int authority=0; authority<authorityResults.size(); authority++) {
				if(userResults.get(i).getRoyalID().equals(authorityResults.get(authority).getRid())) {
					userResults.get(i).addRole(authorityResults.get(authority));
				}
			}
		}
		
		for(int user=0; user < userResults.size(); user++) {
			users.add(userResults.get(user));
		}
		
		return users;
	}
	
	@Override 
	public UserAccount getFullUserProfileByRoyalID(String royalID) {
		UserAccount user = new UserAccount();
		user.setRoyalID("-1");
		try {
			String userSQL = "SELECT * from stayconnected.useraccount where rid = '" + royalID + "';";
			user = jdbcTemplate.queryForObject(userSQL, new accountMapper());
		}
		catch(DataAccessException e) {
			//No user exists for ID
			return user;
		}
		
		List<UserSkill> userskillResults = new ArrayList<>();
		try {
			String skillSQL = "SELECT stayconnected.userskills.rid,"
					+ "stayconnected.skills.skillname,"
					+ "stayconnected.userskills.proficiency"
					+ " FROM stayconnected.userskills,"
					+ "stayconnected.skills  "
					+ "WHERE userskills.skillid"
					+ " = skills.skillid and userskills.rid = '" + royalID + "';";
			userskillResults = jdbcTemplate.query(skillSQL, new userskillsMapper());
		}
		catch(DataAccessException e) {
			System.out.println("No Skills found");
		}
		
		List<JobHistory> jobhistoryResults = new ArrayList<>();
		try {
			String jobSQL = "SELECT * from stayconnected.jobhistory where rid = '" + royalID + "';";
			jobhistoryResults = jdbcTemplate.query(jobSQL, new jobhistoryMapper());
		}
		catch(DataAccessException e) {
			System.out.println("NO JOB HISTORY FOUND");
		}
		
		List<Authority> authorityResults = new ArrayList<>();
		try {
			String authoritySQL = "SELECT stayconnected.authority.rid , "
					+ "stayconnected.userroles.role "
					+ " FROM stayconnected.authority , stayconnected.userroles "
					+ "WHERE authority.userroleid = userroles.uid and "
					+ "stayconnected.authority.rid = '" + royalID + "';";
			authorityResults = jdbcTemplate.query(authoritySQL, new authorityMapper());
		}
		catch(DataAccessException e) {
			System.out.println("NO Authorities Found");
		}
		
		for(int skill=0; skill<userskillResults.size(); skill++) {
			if(user.getRoyalID().equals(userskillResults.get(skill).getRid())) {
				Skill newSkill = new Skill(-1 , userskillResults.get(skill).getProficiency(),
						userskillResults.get(skill).getSkillName());
				user.addSkills(newSkill);
			}
		}
		
		for(JobHistory history : jobhistoryResults) {
			user.addWorkHistory(history);
		}
		
		for(Authority authority : authorityResults) {
			authority.setRole(authority.getRole().replaceAll("ROLE_", ""));
			user.addRole(authority);
		}
		
		return user;
	}

	@Override
	public int deleteAccount(String RID) {
		String SQL = "DELETE FROM stayconnected.useraccount WHERE useraccount.rid = ?";
		jdbcTemplate.update(SQL , RID);
		try {
		jdbcTemplate.execute(SQL); 
		}
		
		catch(DataAccessException e){ 
			
		}
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

	@Override
	public void update(UpdateAccountForm update, Principal user) {
		String SQL;
		if(update.getFirstName() != "") {
			SQL = "UPDATE stayconnected.useraccount SET fname = " + "'" +update.getFirstName() + "'"  
					+ " WHERE rid = " + "'" + user.getName() + "'";
			jdbcTemplate.update(SQL);
		}
		if(update.getLastName() != "") {
			SQL = "UPDATE stayconnected.useraccount SET lname = " + "'" + update.getLastName() + "'" 
					+ " WHERE rid = " + "'" + user.getName() + "'";
			jdbcTemplate.update(SQL);
		}
		if(update.getEmail() != "") {
			SQL = "UPDATE stayconnected.useraccount SET email = " + "'" + update.getEmail() + "'" 
					+ " WHERE rid = " + "'" + user.getName() + "'";
			jdbcTemplate.update(SQL);
		}
		if(update.getPhoneNumber() != "") {
			SQL = "UPDATE stayconnected.useraccount SET phone_number = " + "'" + update.getPhoneNumber() + "'" 
					+ " WHERE rid = " + "'" + user.getName() + "'";
			jdbcTemplate.update(SQL);
		}
		if(update.getAddress() != "") {
			SQL = "UPDATE stayconnected.useraccount SET address = " + "'" + update.getAddress() + "'" 
					+ " WHERE rid = " + "'" + user.getName() + "'";
			jdbcTemplate.update(SQL);
		}
		
	}
	
	@Override
	public Boolean isAccountActivated(String royalID) {
		try	{
			String SQL = "select status from stayconnected.AccountStatus where RID = ?";
			Boolean result = jdbcTemplate.queryForObject(SQL, new Object[] { royalID }, new UserAccountStatusMapper());
			return result;
		}
		catch (DataAccessException e) {
			return false;
		}		
	}
}