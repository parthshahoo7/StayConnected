package edu.JIT.Controller;

import java.security.Principal;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.JIT.Controller.form.BrowseUserForm;
import edu.JIT.Controller.form.RegistrationForm;
import edu.JIT.Controller.form.validator.RegistrationFormValidation;
import edu.JIT.dao.daoInterfaces.UserAccountDao;
import edu.JIT.model.accountManagement.MailService;
import edu.JIT.model.accountManagement.Skill;
import edu.JIT.model.accountManagement.UserAccount;
import edu.JIT.model.accountManagement.UserActivation;

@Controller
public class AccountController {

	@Autowired
	UserAccountDao dao;

	@Autowired
	private RegistrationFormValidation validation;
	
	@Autowired
	private MailService mailingService;

	@GetMapping("/home")
	public String home() {
		return "homePage";
	}
	
	@GetMapping("/registration")
	public String registration(RegistrationForm accountForm, Model model) {
		model.addAttribute("accountForm", accountForm);
		model.addAttribute("roles", dao.getRoles());
		model.addAttribute("skills", dao.getSkills());
		return "registration";
	}

	@PostMapping(value = "/registration")
	public String addAccount(@RequestParam(value = "ski", required = false) int[] ski,
			@Valid RegistrationForm accountForm, final BindingResult result, Model model) {
		validation.validate(accountForm, result);
		if (result.hasErrors()) {
			System.out.println(result.getFieldError());
			List<ObjectError> errors = result.getAllErrors();
			for (ObjectError e : errors) {
				if (e.getCode().equals("Size.webAccount.password")) {
					model.addAttribute("PasswordShort", true);
				} else if (e.getCode().equals("Diff.webAccount.confimPassword")) {
					model.addAttribute("PasswordNoMatch", true);
				} else if (e.getCode().equals("Role.webAccount.roles")) {
					model.addAttribute("NoRole", true);
				}
			}
			model.addAttribute("accountForm", accountForm);
			model.addAttribute("roles", dao.getRoles());
			model.addAttribute("skills", dao.getSkills());
			return "registration";
		} else {

			ArrayList<Skill> skills = (ArrayList<Skill>) dao.getSkills();
			if (ski != null) {
				Skill skill = null;
				for (int i = 0; i < ski.length; i++) {					
					for (int j = 0; j < skills.size(); j++) {
						if (skills.get(j).getSkillID() == ski[i]) {
							skill = new Skill();
							skill.setSkillName(skills.get(j).getSkillName());
							skill.setSkillID(skills.get(j).getSkillID());
							accountForm.getAccount().addSkills(skill);
						}
					}
				}
			}
			String rawPassword = accountForm.getPassword();
			accountForm.setPassword(encodePassword(accountForm.getPassword()));
			String generatedString = accountForm.createSpecialCode();
			System.out.println("Special Code == " + generatedString);
			dao.createNewAccount(accountForm);
			System.out.println("RoyalID & Password:" + accountForm.getAccount().getRoyalID() + "-" + rawPassword);
			autologin(accountForm);
			//mailingService.sendEmail(accountForm);
			return "redirect:/activateAccount";
		}
	}
	
	@GetMapping("/browseUsers")
	public String browseUsers(Model model, BrowseUserForm filters) {
		ArrayList<UserAccount> users = dao.getAllAccounts();
		Collections.sort(users, new AccountComparator());
		model.addAttribute("systemusers" , users);
		model.addAttribute("filters" , filters);
		return "browseUsers";
		
	}
	
	@PostMapping("/browseUsers")
	public String applyFilters(Model model , BrowseUserForm filters) {
		ArrayList<UserAccount> users = dao.getAllAccounts();
		if(filters.getUserType() == null || filters.getUserType().equals("All")) {
			
		}
		else if(filters.getUserType().equals("student")) {
			for(int i=0; i<users.size(); i++) {
				if(users.get(i).getRoles().contains("ROLE_ALUM")) {
					users.remove(i);
				}
			}
		}
		
		else {
			for(int i=0; i<users.size(); i++) {
				if(users.get(i).getRoles().contains("ROLE_CURR")) {
					users.remove(i);
				}
			}
		}
		model.addAttribute("systemusers" , users);
		model.addAttribute("filters" , filters);
		return "browseUsers";
	}
	
	@GetMapping("/activateAccount")
	public String activateAccount(Model model) {
		UserActivation userActivation = new UserActivation();
		model.addAttribute("userActivation", userActivation);
		return "activateAccount";
	}
	
	@PostMapping(value = "/activateAccount")
	public String verifyAccount(@Valid UserActivation userActivation, Model model) {
		UserActivation dbUserActivation = dao.getSpecialCodeByRoyalID((userActivation.getRoyalID()));
		if(dbUserActivation == null) {
			//Account cannot be found for royal id
			model.addAttribute("NoMatchSpecialCodeOrRoyal", true);
			return "activateAccount";
		}
		
		Period intervalPeriod = Period.between(dbUserActivation.getDate(), userActivation.getDate());
		
		if(intervalPeriod.getDays() > 2) {
			model.addAttribute("SpecialCodeExpired", true);
			return "activateAccount"; 
		}
		else if(dbUserActivation.getRoyalID().equals(userActivation.getRoyalID()) &&
				dbUserActivation.getSpecialCode().equals(userActivation.getSpecialCode())) {
			dao.activateAccountByRoyalID(userActivation.getRoyalID());
			return "homePage";
		}
		else { //Don't match
			model.addAttribute("NoMatchSpecialCodeOrRoyal", true);
			return "activateAccount";
		}
	}
	
	@GetMapping("/updateAccount")
	public String updateAccount(Model model , Principal user) {
		String name;
		if(!(user == null)) {
			name = user.getName();
			System.out.println(name);
		}
		return "updateAccount";
	}
	
	private String encodePassword(String rawPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encryptedPassword = passwordEncoder.encode(rawPassword);
		return encryptedPassword;
	}

// =====================Auto Login Feature============================================
	private void autologin(RegistrationForm accountForm) {
		Authentication auth = new UsernamePasswordAuthenticationToken(accountForm.getAccount().getRoyalID(), null,
				getGrantedAuthorities(accountForm));
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	private Collection<? extends GrantedAuthority> getGrantedAuthorities(RegistrationForm accountForm) {
		return AuthorityUtils
				.createAuthorityList(accountForm.getAccount().getRoles().stream().toArray(size -> new String[size]));
	}
}