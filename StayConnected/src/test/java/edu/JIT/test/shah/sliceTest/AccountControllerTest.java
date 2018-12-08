package edu.JIT.test.shah.sliceTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.ModelAndView;

import edu.JIT.Controller.AccountController;
import edu.JIT.Controller.form.validator.RegistrationFormValidation;
import edu.JIT.Controller.form.validator.updateFormValidation;
import edu.JIT.dao.daoInterfaces.UserAccountDao;
import edu.JIT.model.accountManagement.JobHistory;
import edu.JIT.model.accountManagement.MailService;
import edu.JIT.model.accountManagement.Skill;
import edu.JIT.model.accountManagement.UserAccount;

@SuppressWarnings("serial")

@RunWith(SpringRunner.class)
@WebMvcTest(value = AccountController.class, secure = false)
public class AccountControllerTest {
	private UserAccount testUserAccount;
	private String firstName = "Test_Parth";
	private String lastName = "Test_Shah";
	private String email = "Test_Parth@mail.com";
	private String royalID = "R01315393";
	private String phoneNumber = "0123456789";
	private String userAddress = "316 Taylor Avenue,Scranton PA-18510";
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

	private String newFirstName = "abc";
	private String newLastName = "abc";
	private String newEmail = "abc@mail.com";
	private String newPhoneNumber = "123456789";
	private String newUserAddress = "Scranton";
	private ArrayList<String> newRoles = new ArrayList<String>() {
		{
			add("ROLE_CURR");
		}
	};

	private ArrayList<JobHistory> newWorkExperience = new ArrayList<JobHistory>() {

	};

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserAccountDao userAccountDao;

	@MockBean
	private RegistrationFormValidation registrationFormValidation;

	@MockBean
	private updateFormValidation updateFormValidation;

	@MockBean
	private MailService mailService;

	@Before
	public void setUp() {
		testUserAccount = new UserAccount();
		testUserAccount.setEmail(email);
		testUserAccount.setFirstName(firstName);
		testUserAccount.setLastName(lastName);
		testUserAccount.setPhoneNumber(phoneNumber);
		testUserAccount.setRoles(roles);
		testUserAccount.setRoyalID(royalID);
		testUserAccount.setSkill(skill);
		testUserAccount.setUserAddress(userAddress);
		testUserAccount.setWorkExperience(workExperience);
	}

	@Test
	public void testUpdateUserAccount() throws Exception {
		when(userAccountDao.getAccountByRoyalID(testUserAccount.getRoyalID())).thenReturn(testUserAccount);
		ResultActions resultActions = mockMvc.perform(
				get("/updateUserAccount?royalID=" + testUserAccount.getRoyalID()).accept(MediaType.TEXT_PLAIN));
		resultActions.andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("updateUserAccount"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("accountForm")).andExpect(
						MockMvcResultMatchers.model().attribute("accountForm", Matchers.instanceOf(UserAccount.class)));
	}

	@Test
	public void testUpdateUserAccountResult() throws Exception {
		when(userAccountDao.getAccountByRoyalID(testUserAccount.getRoyalID())).thenReturn(testUserAccount);
		when(userAccountDao.updateUserAccount(testUserAccount)).thenReturn(1);
		testUserAccount.setEmail(newEmail);
		testUserAccount.setFirstName(newFirstName);
		testUserAccount.setLastName(newLastName);
		testUserAccount.setPhoneNumber(newPhoneNumber);
		testUserAccount.setRoles(newRoles);
		testUserAccount.setUserAddress(newUserAddress);
		testUserAccount.setWorkExperience(newWorkExperience);
		ResultActions resultActions = mockMvc
				.perform(post("/updateUserAccount").accept(MediaType.APPLICATION_FORM_URLENCODED)
						.sessionAttr("accountForm", testUserAccount).param("firstName", testUserAccount.getFirstName())
						.param("lastName", testUserAccount.getLastName()).param("email", testUserAccount.getEmail())
						.param("royalID", testUserAccount.getRoyalID())
						.param("phoneNumber", testUserAccount.getPhoneNumber())
						.param("userAddress", testUserAccount.getUserAddress())
						.param("roles", testUserAccount.getRoles().get(0))
						.param("workExperience[0].companyName", testUserAccount.getWorkExperience().toString()))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/viewProfile?royalID=" + testUserAccount.getRoyalID()))
				.andExpect(MockMvcResultMatchers.view()
						.name("redirect:/viewProfile?royalID=" + testUserAccount.getRoyalID()));
		MvcResult mvcResult = resultActions.andReturn();
		ModelAndView modelAndView = mvcResult.getModelAndView();
		UserAccount account = (UserAccount) modelAndView.getModel().get("account");
		System.out.println("User Account:" + account);
	}
}