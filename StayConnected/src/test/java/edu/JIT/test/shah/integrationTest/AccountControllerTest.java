package edu.JIT.test.shah.integrationTest;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.JIT.model.accountManagement.JobHistory;
import edu.JIT.model.accountManagement.UserAccount;

@SuppressWarnings("serial")

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountControllerTest {
	@Autowired
	protected WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	private UserAccount testUserAccount;
	private String firstName = "abc";
	private String lastName = "abc";
	private String email = "abc@mail.com";
	private String royalID = "R01315210";
	private String phoneNumber = "123456789";
	private String userAddress = "Scranton";
	private ArrayList<String> roles = new ArrayList<String>() {
		{
			add("ROLE_CURR");
		}
	};

	private ArrayList<JobHistory> workExperience = new ArrayList<JobHistory>() {

	};

	private String newFirstName = "Test_Parth";
	private String newLastName = "Test_Shah";
	private String newEmail = "Test_Parth@mail.com";
	private String newPhoneNumber = "0123456789";
	private String newUserAddress = "316 Taylor Avenue,Scranton PA-18510";
	private ArrayList<String> newRoles = new ArrayList<String>() {
		{
			add("ROLE_ALUM");
		}
	};
	private ArrayList<JobHistory> newWorkExperience = new ArrayList<JobHistory>() {
		{
			JobHistory testJob = new JobHistory();
			testJob.setCompanyName("Company_Test");
			testJob.setAddress("Adrress_Test");
			testJob.setStartDate("10/11/2015");
			testJob.setEndDate("12/12/2018");
			testJob.setPosition("Software developer");
			testJob.setRid("R01315393");
			add(testJob);
		}
	};

	@Before
	public void setUp() {
		testUserAccount = new UserAccount();
		testUserAccount.setEmail(email);
		testUserAccount.setFirstName(firstName);
		testUserAccount.setLastName(lastName);
		testUserAccount.setPhoneNumber(phoneNumber);
		testUserAccount.setRoles(roles);
		testUserAccount.setRoyalID(royalID);
		testUserAccount.setUserAddress(userAddress);
		testUserAccount.setWorkExperience(workExperience);
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testUpdateUserAccount() throws Exception {
		mockMvc.perform(get("/updateUserAccount?royalID=" + testUserAccount.getRoyalID()).accept(MediaType.TEXT_PLAIN))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("accountForm"));
	}

	@Test
	public void testUpdateUserAccountResult() throws Exception {
		testUserAccount.setEmail(newEmail);
		testUserAccount.setFirstName(newFirstName);
		testUserAccount.setLastName(newLastName);
		testUserAccount.setPhoneNumber(newPhoneNumber);
		testUserAccount.setRoles(newRoles);
		testUserAccount.setUserAddress(newUserAddress);
		testUserAccount.setWorkExperience(newWorkExperience);

		mockMvc.perform(post("/updateUserAccount").accept(MediaType.APPLICATION_FORM_URLENCODED)
				.sessionAttr("accountForm", testUserAccount).param("firstName", testUserAccount.getFirstName())
				.param("lastName", testUserAccount.getLastName()).param("email", testUserAccount.getEmail())
				.param("royalID", testUserAccount.getRoyalID()).param("phoneNumber", testUserAccount.getPhoneNumber())
				.param("userAddress", testUserAccount.getUserAddress())
				.param("roles", testUserAccount.getRoles().get(0))
				.param("workExperience[0].companyName", testUserAccount.getWorkExperience().get(0).getCompanyName())
				.param("workExperience[0].position", testUserAccount.getWorkExperience().get(0).getPosition())
				.param("workExperience[0].startDate", testUserAccount.getWorkExperience().get(0).getStartDate())
				.param("workExperience[0].endDate", testUserAccount.getWorkExperience().get(0).getEndDate())
				.param("workExperience[0].address", testUserAccount.getWorkExperience().get(0).getAddress()))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/viewProfile?royalID=" + testUserAccount.getRoyalID()))
				.andExpect(MockMvcResultMatchers.view()
						.name("redirect:/viewProfile?royalID=" + testUserAccount.getRoyalID()));
	}
}