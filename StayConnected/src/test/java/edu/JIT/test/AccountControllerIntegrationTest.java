package edu.JIT.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.JIT.Controller.accountManagement.form.RegistrationForm;
import edu.JIT.Controller.accountManagement.form.validator.RegistrationFormValidation;
import edu.JIT.Controller.accountManagement.form.validator.updateFormValidation;
import edu.JIT.dao.daoInterfaces.accountManagement.UserAccountDao;
import edu.JIT.model.accountManagement.JobHistory;
import edu.JIT.model.accountManagement.MailService;
import edu.JIT.model.accountManagement.Skill;
import edu.JIT.model.accountManagement.UserAccount;

@SuppressWarnings("serial")

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountControllerIntegrationTest {
	
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
		testSkill.setSkillID(1);
		testSkill.setSkillName("SoftwareDeveloper");
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
	
	private MockMvc mockMvc;
	
	@Autowired
	protected WebApplicationContext webApplicationContext;
	
	@Autowired
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
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
			post("/activateAccount").accept(MediaType.APPLICATION_FORM_URLENCODED)
			.sessionAttr("UserActivation", form));
		resultActions.andDo(print())
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("activateAccount"));				
	}
	
	@After
	public void tearDown() throws Exception {
		userAccountDao.deleteAccount(testAccount.getRoyalID());
	}
	
}
