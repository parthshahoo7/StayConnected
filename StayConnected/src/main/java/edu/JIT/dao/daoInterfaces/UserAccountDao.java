package edu.JIT.dao.daoInterfaces;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import edu.JIT.Controller.form.RegistrationForm;
import edu.JIT.model.accountManagement.Skill;
import edu.JIT.model.accountManagement.UserAccount;

@Repository
public interface UserAccountDao {

	public UserAccount createNewAccount(RegistrationForm account);

	public UserAccount getAccountByRoyalID(String royalID);

	public ArrayList<UserAccount> getAllAccounts();

	public int deleteAccount(UserAccount account);

	public int update(RegistrationForm webAccount);

	public List<String> getRoles();

	public List<Skill> getSkills();
}