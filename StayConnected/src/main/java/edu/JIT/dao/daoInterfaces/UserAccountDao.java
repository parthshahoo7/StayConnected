package edu.JIT.dao.daoInterfaces;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import edu.JIT.Controller.form.RegistrationForm;
import edu.JIT.Controller.form.UpdateAccountForm;
import edu.JIT.model.accountManagement.JobHistory;
import edu.JIT.model.accountManagement.Skill;
import edu.JIT.model.accountManagement.UserAccount;
import edu.JIT.model.accountManagement.UserActivation;

@Repository
public interface UserAccountDao {

	public UserAccount createNewAccount(RegistrationForm account);

	public UserAccount getAccountByRoyalID(String royalID);

	public ArrayList<UserAccount> getAllAccounts();
	
	public UserAccount getFullUserProfileByRoyalID(String royalID);

	public int deleteAccount(String royalID);

	public int update(RegistrationForm webAccount);

	public List<String> getRoles();

	public List<Skill> getSkills();
	
	public UserActivation getSpecialCodeByRoyalID(String royalID);
	
	public boolean activateAccountByRoyalID(String royalID);

	public void update(UpdateAccountForm update, Principal user);
	
	public Boolean isAccountActivated(String royalID);

	public ArrayList<String> getRolesByID(String royalID);

	public List<Skill> getSkillsByID(String royalID);

	public List<JobHistory> getJobHistoryByID(String royalID);

	public int updateUserAccount(UserAccount userAccount); 
}