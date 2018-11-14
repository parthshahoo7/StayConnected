package edu.JIT.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import edu.JIT.model.RegistrationForm;
import edu.JIT.model.Skill;
import edu.JIT.model.UserAccount;

@Repository
public interface UserAccountDao {

	public UserAccount createNewAccount(RegistrationForm account);

	public UserAccount getAccountByRoyalID(String royalID);

	public List<UserAccount> getAllAccounts();

	public int deleteAccount(UserAccount account);

	public int update(RegistrationForm webAccount);

	public List<String> getRoles();

	public List<Skill> getSkills();
}