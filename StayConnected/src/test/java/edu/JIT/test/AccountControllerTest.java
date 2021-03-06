package edu.JIT.test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import edu.JIT.Controller.accountManagement.AccountController;
import edu.JIT.Controller.accountManagement.form.RegistrationForm;
import edu.JIT.Controller.accountManagement.form.validator.RegistrationFormValidation;
import edu.JIT.Controller.accountManagement.form.validator.updateFormValidation;
import edu.JIT.dao.daoInterfaces.accountManagement.UserAccountDao;
import edu.JIT.model.accountManagement.JobHistory;
import edu.JIT.model.accountManagement.MailService;
import edu.JIT.model.accountManagement.Skill;
import edu.JIT.model.accountManagement.UserAccount;
import edu.JIT.model.accountManagement.UserActivation;

@SuppressWarnings("serial")

@RunWith(SpringRunner.class)
@WebMvcTest(value=AccountController.class, secure=false)

public class AccountControllerTest {
	
	private UserAccount testAccount;
	private String firstName = "testFirstName";
	private String lastName = "testLastName";
	private String email = "test@mail.com";
	private String royalID = "r012345678910";
	private String phoneNumber = "123456789";
	private String userAddress = "8 Jane Street, Scranton PA";
	private String password = "123456789";
	private String specialCode = "987654321";
	
	private ArrayList<String> roles = new ArrayList<String>() {{
	    add("ROLE_CURR");
	}};
	private ArrayList<String> company = new ArrayList<String>() {{
	    add("Test Company");
	}};
	private ArrayList<Skill> skill = new ArrayList<Skill>() {{
		Skill testSkill = new Skill();
		testSkill.setProficiency("Beginner");
		testSkill.setSkillID(-1);
		testSkill.setSkillName("test");
	    add(testSkill);
	}};
	private ArrayList<JobHistory> workExperience = new ArrayList<JobHistory>() {{
	    JobHistory testJob = new JobHistory();
	    testJob.setCompanyName("testCompany");
	    testJob.setAddress("8 Jane Street, Scranton PA");
	    testJob.setCurrentlyEmployed(false);
	    testJob.setEndDate("01/01/2018");
	    testJob.setStartDate("01/01/2017");
	    testJob.setPosition("tester");
	    testJob.setRid("r012345678910");
	    add(testJob);
	}};
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserAccountDao userAccountDao;
	
	@MockBean
	private RegistrationFormValidation registrationFormValidation;
	
	@MockBean
	private updateFormValidation updateformValidation;
	
	@MockBean
	private MailService mailingService;
	
	@Before
	public void setUp() {
		testAccount = new UserAccount();
		testAccount.setCompany(company);
		testAccount.setEmail(email);
		testAccount.setFirstName(firstName);
		testAccount.setLastName(lastName);
		testAccount.setPhoneNumber(phoneNumber);
		testAccount.setRoles(roles);
		testAccount.setRoyalID(royalID);
		testAccount.setSkill(skill);
		testAccount.setUserAddress(userAddress);
		testAccount.setWorkExperience(workExperience);
	}
	
	@Test
	public void testViewProfile() throws Exception {
		when(userAccountDao.getFullUserProfileByRoyalID(testAccount.getRoyalID())).thenReturn(testAccount);
		
		ResultActions resultActions = mockMvc.perform(get("/viewProfile?royalID=" + testAccount.getRoyalID())
		.accept(MediaType.TEXT_PLAIN)
		.sessionAttr("royalID", testAccount));
		
		resultActions.andDo(print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("viewProfile"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("user"))
		.andExpect(MockMvcResultMatchers.model().attribute("user", Matchers.instanceOf(UserAccount.class)));	
	}
	
	@Test
	public void testActivateAccount() throws Exception {
		UserActivation userActivation = new UserActivation();
		
		ResultActions resultActions = mockMvc.perform(get("/activateAccount")
				.accept(MediaType.TEXT_PLAIN)
				.sessionAttr("userActivation", userActivation));
		
		resultActions.andDo(print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("activateAccount"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("userActivation"))
		.andExpect(MockMvcResultMatchers.model().attribute("userActivation", Matchers.instanceOf(UserActivation.class)));
	}
	
	@Test 
	public void testVerifyAccount() throws Exception {
		RegistrationForm form = new RegistrationForm();
		form.setAccount(testAccount);
		form.setPassword(password);
		form.setConfirmPassword(password);
		form.setSpecialCode(specialCode);
		
		userAccountDao.createNewAccount(form);
		userAccountDao.activateAccountByRoyalID(testAccount.getRoyalID());
		
		boolean wasActivated = userAccountDao.isAccountActivated(testAccount.getRoyalID());
		assert(wasActivated);
		
		ResultActions resultActions = mockMvc.perform(
				post("/activate").accept(MediaType.APPLICATION_FORM_URLENCODED)
				.sessionAttr("UserActivation", form)
				);
		resultActions.andDo(print())
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("home"));				
	}
	
	@After
	public void tearDown() throws Exception {
		userAccountDao.deleteAccount(testAccount.getRoyalID());
	}
	
}