package edu.JIT.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import edu.JIT.Controller.AccountController;
import edu.JIT.model.accountManagement.JobHistory;
import edu.JIT.model.accountManagement.Skill;
import edu.JIT.model.accountManagement.UserAccount;

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
	}};;
	private ArrayList<JobHistory> workExperience = new ArrayList<JobHistory>() {{
	    JobHistory testJob = new JobHistory();
	    testJob.setCompanyName("testCompany");
	    testJob.setAddress("8 Jane Street, Scranton PA");
	    testJob.setCurrentlyEmployed(false);
	    testJob.setEndDate("01/01/2018");
	    testJob.setStartDate("01/01/2017");
	    testJob.setPosition("tester");
	    testJob.setRid("r012345678910");
	}};
	
	@Autowired
	private MockMvc mockMvc;
	
	@Before
	public void setUp() {
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
}
