package edu.JIT.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import edu.JIT.model.UserAccount;


@Repository
public interface UserAccountDao {
	
	public UserAccount createNewAccount(UserAccount newAccount);
	public UserAccount getAccountByRoyalID(Long royalID);
	public List<UserAccount> getAllAccounts();
	public List<UserAccount> getAllAlumni();
	public List<UserAccount> getAllCurrentStudents();
	public List<UserAccount> getAllUserRoles();
	
}
