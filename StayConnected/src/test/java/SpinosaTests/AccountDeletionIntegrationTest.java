package SpinosaTests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.JIT.Controller.AccountController;
import edu.JIT.Controller.form.AcctDeletionForm;
import edu.JIT.Controller.form.DeleteConfirmationForm;
import edu.JIT.Controller.form.RegistrationForm;
import edu.JIT.dao.daoImplementation.UserAccountDaoImpl;
import edu.JIT.model.accountManagement.UserAccount;

@RunWith(SpringRunner.class)
@WebMvcTest(value=AccountController.class, secure=false)
public class AccountDeletionIntegrationTest {
	
	@Autowired
	private AccountController AccountController;
	
	@Autowired
	private UserAccountDaoImpl myimpl;
	
	@Autowired
	DeleteConfirmationForm choice;
	
	@Autowired 
	AcctDeletionForm deletion;
	
	RegistrationForm myform;
	
	
	@Before
	public void setUp() {
		deletion.setRID("R99999");
		choice.setSelection("YES");
		ArrayList<String> myRoles = new ArrayList<String>();
		myRoles.add("ROLE_ALUM");
		UserAccount mynewAccount = new UserAccount("fname" , "lname" , "email@email.com" , "R99999", "5708519324" ,
													myRoles , null , "" , null, null);
		
		myform = new RegistrationForm(mynewAccount , "1234" , "1234" , "");
	}
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Test
	public void deleteUser() {
		myimpl.createNewAccount(myform);
		AccountController.confirmDeletion(null, choice, deletion, null);
		assertThat(null , equalTo(myimpl.getAccountByRoyalID(deletion.getRID())));
	}
	
	
}
