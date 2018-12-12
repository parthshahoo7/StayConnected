package SpinosaTests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import edu.JIT.Controller.accountManagement.AccountController;
import edu.JIT.Controller.accountManagement.form.AcctDeletionForm;
import edu.JIT.Controller.accountManagement.form.DeleteConfirmationForm;
import edu.JIT.dao.daoImplementation.accountManagemnet.UserAccountDaoImpl;
import edu.JIT.dao.daoInterfaces.accountManagement.UserAccountDao;

@RunWith(SpringRunner.class)
@WebMvcTest(value=AccountController.class, secure=false)
public class AccountDeletionTest {

	@Autowired
	private AccountController AccountController;
	
	@Mock
	private UserAccountDao mockdao;
	
	@Mock
	private AcctDeletionForm mockDeletionForm;
	
	@Mock
	private DeleteConfirmationForm mockConfirmation;
	
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@Before
	public void setUp() {
		mockdao = Mockito.mock(UserAccountDaoImpl.class);
		mockDeletionForm.setRID("R12345678");
		
	}
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Test
	public void getToPage() throws Exception{
		ResultActions resultActions = mockMvc.perform(get("/accountDeletion"));
		resultActions.andDo(print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("accountDeletion"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("formSelection"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("formA"));
	}
	
	@Test
	public void NoUserFound() throws Exception {
		when(mockdao.deleteAccount(mockDeletionForm.getRID())).thenReturn(null);
		AccountController.postAccountDeletion(null, mockDeletionForm, null);
		assertThat(true , equalTo(MockMvcResultMatchers.model().attributeExists("error")));
	}
}
