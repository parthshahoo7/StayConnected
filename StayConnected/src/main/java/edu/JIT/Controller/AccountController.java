package edu.JIT.Controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import edu.JIT.model.UserAccount;
import edu.JIT.dao.UserAccountDao;
import edu.JIT.model.RegistrationForm;

public class AccountController {

	@Autowired
	UserAccountDao dao;
	
	@GetMapping("/registration")
	public String registration(RegistrationForm accountForm, Model model) {
		model.addAttribute("newUser" , accountForm);
		return "registration"; }
	
	@PostMapping("/registration")
	public String addAccount( @Valid RegistrationForm accountForm, BindingResult result, Model model) {
		dao.createNewAccount(accountForm);
		return "activateAccount";
	}
}
