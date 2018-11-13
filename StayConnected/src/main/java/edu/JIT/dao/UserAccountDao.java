package edu.JIT.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import edu.JIT.model.RegistrationForm;
import edu.JIT.model.UserAccount;

@Repository
public interface UserAccountDao {

	public UserAccount createNewAccount(RegistrationForm account);

	public UserAccount getAccountByRoyalID(Long royalID);

	public List<UserAccount> getAllAccounts();

	public int deleteAccount(UserAccount account);

	public int update(RegistrationForm webAccount);

	/*
	 * public List<UserAccount> getAllAlumni();
	 * 
	 * public List<UserAccount> getAllCurrentStudents();
	 */ // We don't need this as we fetch all the data once than we can manipulate with
		// list

	// public List<UserAccount> getAllUserRoles(); //this is going in different DAO
	// as it's functionality is different

}
