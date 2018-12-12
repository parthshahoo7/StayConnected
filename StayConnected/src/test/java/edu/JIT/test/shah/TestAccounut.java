package edu.JIT.test.shah;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.JIT.Controller.accountManagement.form.RegistrationForm;
import edu.JIT.model.accountManagement.JobHistory;
import edu.JIT.model.accountManagement.Skill;
import edu.JIT.model.accountManagement.UserAccount;

@SuppressWarnings("serial")

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAccounut {
	@Autowired
	protected WebApplicationContext webApplicationContext;


	private MockMvc mockMvc;

	private RegistrationForm testUserAccount;
	private UserAccount userAccount=new UserAccount();
	private String firstName = "Test_Parth";
	private String lastName = "Test_Shah";
	private String email = "Test_Parth@mail.com";
	private String royalID = "R01315393";
	private String phoneNumber = "0123456789";
	private String userAddress = "316 Taylor Avenue,Scranton PA-18510";
	private String password="123456789";
	private String confirmPassword="123456789";
	private ArrayList<String> roles = new ArrayList<String>() {
		{
			add("ROLE_ALUM");
		}
	};
	private ArrayList<Skill> skill = new ArrayList<Skill>() {
		{
			Skill skilltest = new Skill();
			skilltest.setProficiency("Intermediate");
			skilltest.setSkillID(0);
			skilltest.setSkillName("test_Developer");
			add(skilltest);
		}
	};

	private ArrayList<JobHistory> workExperience = new ArrayList<JobHistory>() {
		{
			JobHistory testJob = new JobHistory();
			testJob.setCompanyName("Company_Test");
			testJob.setAddress("Adrress_Test");
			testJob.setCurrentlyEmployed(false);
			testJob.setStartDate("10/11/2015");
			testJob.setEndDate("12/12/2018");
			testJob.setPosition("Software developer");
			testJob.setRid("R01315393");
			add(testJob);
		}
	};

	@Before
	public void setUp() {
		testUserAccount = new RegistrationForm();
		testUserAccount.setAccount(userAccount);
		testUserAccount.getAccount().setEmail(email);
		testUserAccount.getAccount().setFirstName(firstName);
		testUserAccount.getAccount().setLastName(lastName);
		testUserAccount.getAccount().setPhoneNumber(phoneNumber);
		testUserAccount.getAccount().setRoles(roles);
		testUserAccount.getAccount().setRoyalID(royalID);
		testUserAccount.getAccount().setSkill(skill);
		testUserAccount.getAccount().setUserAddress(userAddress);
		testUserAccount.getAccount().setWorkExperience(workExperience);
		testUserAccount.setPassword(password);
		testUserAccount.setConfirmPassword(confirmPassword);
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testRegistration() throws Exception {
		mockMvc.perform(get("/registration").accept(MediaType.TEXT_PLAIN)).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void testAddAccount() throws Exception{
		mockMvc.perform(post("/registration").accept(MediaType.APPLICATION_FORM_URLENCODED).sessionAttr("accountForm", testUserAccount).requestAttr("account", testUserAccount.getAccount()).param("account.firstName", testUserAccount.getAccount().getFirstName()).param("account.lastName",testUserAccount.getAccount().getLastName()).param("account.email", testUserAccount.getAccount().getEmail()).param("account.royalID", testUserAccount.getAccount().getRoyalID()).param("account.phoneNumber", testUserAccount.getAccount().getPhoneNumber()).param("account.userAddress", testUserAccount.getAccount().getUserAddress())
				.param("account.roles",testUserAccount.getAccount().getRoles().get(0)).param("skills", testUserAccount.getAccount().getSkill().get(0).getSkillName()).param("password", testUserAccount.getPassword()).param("confirmPassword", testUserAccount.getConfirmPassword()))
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/activateAccount"));
		
	}

	/*@Test
	public void testresultUpdateUserAccount() throws Exception{
		mockMvc.perform(post("/updateUserAccount").accept(MediaType.APPLICATION_FORM_URLENCODED).param(name, values))
	}*/

}
