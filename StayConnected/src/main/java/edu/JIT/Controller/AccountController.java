package edu.JIT.Controller;

import java.security.Principal;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.JIT.Controller.form.BrowseUserForm;
import edu.JIT.Controller.form.RegistrationForm;
import edu.JIT.Controller.form.UpdateAccountForm;
import edu.JIT.Controller.form.validator.RegistrationFormValidation;
import edu.JIT.Controller.form.validator.updateFormValidation;
import edu.JIT.dao.daoInterfaces.UserAccountDao;
import edu.JIT.model.accountManagement.JobHistory;
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
	private updateFormValidation updateValidation;

	@Autowired
	private MailService mailingService;

	private String royalID;

	@GetMapping(value = { "/home", "/" })
	public String home(Principal principal) {
		if (principal != null) {
			boolean isActivated = dao.isAccountActivated(principal.getName());
			if (!isActivated) {
				return "redirect:/activateAccount";
			}
		}
		return "homePage";
	}

	@GetMapping("/confirmation")
	public String confirmation(Model model) {
		return "confirmation";
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
			@Valid RegistrationForm accountForm, final BindingResult result, Model model, Principal principal) {
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
			String generatedString = accountForm.createSpecialCode();
			System.out.println("Special Code == " + generatedString);

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

			if (principal != null && principal.getName() != null && principal.getName() != "") {
				// Faculty making account
				try {
					mailingService.sendEmail(accountForm.getAccount().getEmail(), "StayConnected Account Creation",
							"A faculty member at the University of Scranton has created an account for you on StayConnected.  "
									+ "You can sign in using your Royal ID and this password: " + rawPassword
									+ "The system will " + "prompt you for a special code, your code is "
									+ accountForm.getSpecialCode());
					dao.createNewAccount(accountForm);
					return "redirect:/manageAccount";
				} catch (MailException e) {
					model.addAttribute("accountForm", accountForm);
					model.addAttribute("roles", dao.getRoles());
					model.addAttribute("skills", dao.getSkills());
					model.addAttribute("EmailNotValid", true);
					return "registration";
				}
			} else {
				try {
					mailingService.sendEmail(accountForm.getAccount().getEmail(), "StayConnected Special Code",
							"Your special code is: " + accountForm.getSpecialCode());
					dao.createNewAccount(accountForm);
				} catch (MailException e) {
					model.addAttribute("accountForm", accountForm);
					model.addAttribute("roles", dao.getRoles());
					model.addAttribute("skills", dao.getSkills());
					model.addAttribute("EmailNotValid", true);
					return "registration";
				}
			}
			System.out.println("RoyalID & Password:" + accountForm.getAccount().getRoyalID() + "-" + rawPassword);

			autologin(accountForm);
			return "redirect:/activateAccount";
		}
	}

	@GetMapping("/browseUsers")
	public String browseUsers(Model model, BrowseUserForm filters) {
		ArrayList<UserAccount> users = dao.getAllAccounts();
		Collections.sort(users, new AccountComparator());
		model.addAttribute("systemusers", users);
		model.addAttribute("filters", filters);
		model.addAttribute("skills", dao.getSkills());
		return "browseUsers";
	}

	@PostMapping("/browseUsers")
	public String applyFilters(Model model, BrowseUserForm filters) {
		ArrayList<UserAccount> users = dao.getAllAccounts();			
		if (filters.getUserType() == null || filters.getUserType().equals("All")) {

		} else if (filters.getUserType().equals("student")) {
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).getRoles().contains("ROLE_ALUM")) {
					users.remove(i);
				}
			}
		}

		else {
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).getRoles().contains("ROLE_CURR")) {
					users.remove(i);
				}
			}
		}

		if (!filters.getSelectedSkills().isEmpty()) {
			users = filterBySkill(filters.getSelectedSkills(), users);
		}
		if (!filters.getSearchName().equals("")) {
			users = filterByName(filters.getSearchName(), users);
		}
		Collections.sort(users, new AccountComparator());
		model.addAttribute("systemusers", users);
		model.addAttribute("filters", filters);
		model.addAttribute("skills", dao.getSkills());
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
		if (dbUserActivation == null) {
			// Account cannot be found for royal id
			model.addAttribute("NoMatchSpecialCodeOrRoyal", true);
			return "activateAccount";
		}

		Period intervalPeriod = Period.between(dbUserActivation.getDate(), userActivation.getDate());

		if (intervalPeriod.getDays() > 2) {
			model.addAttribute("SpecialCodeExpired", true);
			return "activateAccount";
		} else if (dbUserActivation.getRoyalID().equals(userActivation.getRoyalID())
				&& dbUserActivation.getSpecialCode().equals(userActivation.getSpecialCode())) {
			dao.activateAccountByRoyalID(userActivation.getRoyalID());
			return "redirect:/accountActivated";
		} else { // Don't match
			model.addAttribute("NoMatchSpecialCodeOrRoyal", true);
			return "activateAccount";
		}
	}

	@GetMapping("/updateAccount")
	public String updateAccount(Model model, UpdateAccountForm update, Principal user) {
		String name;
		name = user.getName();
		try {
			UserAccount loggedInUser = dao.getAccountByRoyalID(name);
			model.addAttribute("user", loggedInUser);
			model.addAttribute("updateform", update);
			model.addAttribute("error", false);
		} catch (DataAccessException e) {
			System.out.print("couldnt get user!!");
		}
		return "updateAccount";
	}

	@PostMapping("/updateAccount")
	public String submitUpdateAccount(UpdateAccountForm update, Principal user, final BindingResult result, Model model,
			final RedirectAttributes redirectAttributes) {
		updateValidation.validate(update, result);
		if (!result.hasErrors()) {
			dao.update(update, user);
			redirectAttributes.addFlashAttribute("confirmationMessage", "Account has been updated");
			return "redirect:/confirmation";
		} else {
			model.addAttribute("error", true);
			UserAccount loggedInUser = dao.getAccountByRoyalID(user.getName());
			model.addAttribute("user", loggedInUser);
			model.addAttribute("updateform", update);
			return "updateAccount";
		}
	}

	@GetMapping("/manageAccount")
	public String manageAccount() {
		return "/manageAccount";
	}

	@GetMapping("/viewProfile")
	public String viewProfile(@RequestParam(name = "royalID", required = true) String royalID, Model model) {
		UserAccount profileOfUser = dao.getFullUserProfileByRoyalID(royalID);
		if (profileOfUser == null || profileOfUser.getRoyalID().equals("-1")) {
			model.addAttribute("notFound", true);
			return "viewProfile";
		}
		model.addAttribute("notFound", false);
		model.addAttribute("user", profileOfUser);
		return "viewProfile";
	}

	private String encodePassword(String rawPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encryptedPassword = passwordEncoder.encode(rawPassword);
		return encryptedPassword;
	}

//=======================Faculty Updates User Account===============================
	@GetMapping("/updateUserAccount")
	public String updateUserAccount(@RequestParam(value = "royalID", required = true) String royalID, Model model) {
		UserAccount accountForm = dao.getAccountByRoyalID(royalID);
		this.royalID = royalID;
		model.addAttribute("accountForm", accountForm);
		model.addAttribute("royalID", royalID);
		model.addAttribute("roles", dao.getRoles());
		return "updateUserAccount";
	}

	@PostMapping("/updateUserAccount")
	public String resultUserUpdateAccount(@Valid UserAccount userAccount, final BindingResult result, Model model,
			final RedirectAttributes redirectAttributes) {
		userAccount.setRoyalID(royalID);
		if (result.hasErrors()) {
			userAccount = dao.getAccountByRoyalID(this.royalID);
			JobHistory jobHistory = new JobHistory();
			for (int i = 0; i < dao.getJobHistoryByID(this.royalID).size(); i++) {
				jobHistory.setAddress(dao.getJobHistoryByID(this.royalID).get(i).getAddress());
				jobHistory.setCompanyName(dao.getJobHistoryByID(this.royalID).get(i).getCompanyName());
				jobHistory.setEndDate(dao.getJobHistoryByID(this.royalID).get(i).getEndDate());
				jobHistory.setPosition(dao.getJobHistoryByID(this.royalID).get(i).getPosition());
				jobHistory.setRid(dao.getJobHistoryByID(this.royalID).get(i).getRid());
				jobHistory.setStartDate(dao.getJobHistoryByID(this.royalID).get(i).getStartDate());
			}
			model.addAttribute("accountForm", userAccount);
			model.addAttribute("royalID", royalID);
			model.addAttribute("roles", dao.getRoles());
			return "updateUserAccount";
		} else {
			try {
				if (!userAccount.getEmail().equals(dao.getAccountByRoyalID(royalID).getEmail())) {
					RegistrationForm form = new RegistrationForm();
					String specialCode = form.createSpecialCode();
					userAccount.setSpecialCode(specialCode);
					System.out.println("Special Code == " + specialCode);
					System.out.println("RoyalID:" + userAccount.getRoyalID());
					mailingService.sendEmail(userAccount.getEmail(), "StayConnected Account Creation",
							"A faculty member at the University of Scranton has updated an account for you on StayConnected.  "
									+ "You can sign in using your Royal ID and your password: and The system will "
									+ "prompt you for a special code, your code is " + userAccount.getSpecialCode());
					dao.updateUserAccount(userAccount);
				} else {
					dao.updateUserAccount(userAccount);
				}
				return "redirect:/viewProfile?royalID=" + userAccount.getRoyalID();
			} catch (MailException e) {
				userAccount = dao.getAccountByRoyalID(this.royalID);
				userAccount.setRoles(dao.getRolesByID(this.royalID));
				JobHistory jobHistory = new JobHistory();
				for (int i = 0; i < dao.getJobHistoryByID(this.royalID).size(); i++) {
					jobHistory.setAddress(dao.getJobHistoryByID(this.royalID).get(i).getAddress());
					jobHistory.setCompanyName(dao.getJobHistoryByID(this.royalID).get(i).getCompanyName());
					jobHistory.setEndDate(dao.getJobHistoryByID(this.royalID).get(i).getEndDate());
					jobHistory.setPosition(dao.getJobHistoryByID(this.royalID).get(i).getPosition());
					jobHistory.setRid(dao.getJobHistoryByID(this.royalID).get(i).getRid());
					jobHistory.setStartDate(dao.getJobHistoryByID(this.royalID).get(i).getStartDate());
				}
				userAccount.setWorkExperience((ArrayList<JobHistory>) dao.getJobHistoryByID(royalID));
				model.addAttribute("accountForm", userAccount);
				model.addAttribute("workExperience", userAccount.getWorkExperience());
				model.addAttribute("roles", dao.getRoles());
				return "updateUserAccount";
			}
		}

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

// ======================Private Functions================================================================	
	private ArrayList<UserAccount> filterBySkill(ArrayList<String> skills, ArrayList<UserAccount> users) {
		ArrayList<UserAccount> filtedUsers = new ArrayList<UserAccount>();

		for (UserAccount user : users) {
			for (Skill userSkill : user.getSkill()) {
				for (String skill : skills) {
					if (skill.equals(userSkill.getSkillName()) && !filtedUsers.contains(user)) {
						filtedUsers.add(user);
					}
				}
			}
		}
		return filtedUsers;
	}

	private ArrayList<UserAccount> filterByName(String searchName, ArrayList<UserAccount> users) {
		ArrayList<UserAccount> filteredUsers = new ArrayList<UserAccount>();

		for (UserAccount user : users) {
			if (user.getFirstName().equals(searchName)) {
				filteredUsers.add(user);
			}
		}
		return filteredUsers;
	}
}