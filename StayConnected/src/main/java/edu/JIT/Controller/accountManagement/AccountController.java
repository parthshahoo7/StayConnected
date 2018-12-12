package edu.JIT.Controller.accountManagement;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.JIT.Controller.accountManagement.form.AcctDeletionForm;
import edu.JIT.Controller.accountManagement.form.BrowseUserForm;
import edu.JIT.Controller.accountManagement.form.DeleteConfirmationForm;
import edu.JIT.Controller.accountManagement.form.RegistrationForm;
import edu.JIT.Controller.accountManagement.form.UpdateAccountForm;
import edu.JIT.Controller.accountManagement.form.validator.RegistrationFormValidation;
import edu.JIT.Controller.accountManagement.form.validator.updateFormValidation;
import edu.JIT.dao.daoInterfaces.accountManagement.UserAccountDao;
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

	String myRid = "";

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
		;
		System.out.println(accountForm.getAccount().getEmail());
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
					UserAccount account = dao.createNewAccount(accountForm);
					if (account == null) {
						model.addAttribute("accountForm", accountForm);
						model.addAttribute("roles", dao.getRoles());
						model.addAttribute("skills", dao.getSkills());
						model.addAttribute("RoyalIDNotValid", true);
						model.addAttribute("EmailNotValid", false);
						return "registration";
					}
					return "redirect:/manageAccount";
				} catch (MailException e) {
					model.addAttribute("accountForm", accountForm);
					model.addAttribute("roles", dao.getRoles());
					model.addAttribute("skills", dao.getSkills());
					model.addAttribute("EmailNotValid", true);
					model.addAttribute("RoyalIDNotValid", false);
					return "registration";
				}
			} else {
				try {
					mailingService.sendEmail(accountForm.getAccount().getEmail(), "StayConnected Special Code",
							"Your special code is: " + accountForm.getSpecialCode());
					UserAccount account = dao.createNewAccount(accountForm);
					if (account == null) {
						model.addAttribute("accountForm", accountForm);
						model.addAttribute("roles", dao.getRoles());
						model.addAttribute("skills", dao.getSkills());
						model.addAttribute("RoyalIDNotValid", true);
						model.addAttribute("EmailNotValid", false);
						return "registration";
					}
				} catch (MailException e) {
					model.addAttribute("accountForm", accountForm);
					model.addAttribute("roles", dao.getRoles());
					model.addAttribute("skills", dao.getSkills());
					model.addAttribute("EmailNotValid", true);
					model.addAttribute("RoyalIDNotValid", false);
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
		} else {
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
		ArrayList<String> formErrors = new ArrayList<String>();
		updateValidation.validate(update, result);
		if (!result.hasErrors()) {
			dao.update(update, user);
			redirectAttributes.addFlashAttribute("confirmationMessage", "Account has been updated");
			return "redirect:/confirmation";
		} else {
			model.addAttribute("error", true);
			for (int i = 0; i < result.getFieldErrors().size(); i++) {
				formErrors.add(result.getFieldErrors().get(i).getField() + " is invalid!");
			}
			model.addAttribute("formErrors", formErrors);
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
		profileOfUser.setRoles(makeRolesPretty(profileOfUser.getRoles()));
		model.addAttribute("notFound", false);
		model.addAttribute("user", profileOfUser);
		return "viewProfile";
	}

	@GetMapping("/accountDeletion")
	public String getAccountDeletion(Model model, AcctDeletionForm deletion) {
		model.addAttribute("formSelection", 1);
		model.addAttribute("formA", deletion);
		return "accountDeletion";
	}

	@RequestMapping(value = "/accountDeletion/formA", method = RequestMethod.POST)
	public String postAccountDeletion(Model model, AcctDeletionForm deletion, DeleteConfirmationForm choice) {
		UserAccount user = dao.getAccountByRoyalID(deletion.getRID());
		if (user != null) {
			model.addAttribute("user", user);
			model.addAttribute("formSelection", 2);
			model.addAttribute("choice", choice);
			myRid = deletion.getRID();
			return "accountDeletion";
		} else {
			model.addAttribute("error", "No user with RID " + deletion.getRID() + " exists.");
			model.addAttribute("formSelection", 1);
			model.addAttribute("formA", deletion);
			return "accountDeletion";
		}

	}

	@RequestMapping(value = "/accountDeletion/formB", method = RequestMethod.POST)
	public String confirmDeletion(Model model, DeleteConfirmationForm choice, AcctDeletionForm deletion,
			RedirectAttributes redirect) {
		System.out.println("here is the rid we want to delete: " + myRid);
		if (choice.getSelection().equals("YES") && !myRid.isEmpty()) {
			dao.deleteAccount(myRid);
			redirect.addFlashAttribute("success", "The user with RID " + myRid + " has been deleted.");
			return "redirect:/accountDeletion";
		} else {
			model.addAttribute("formSelection", 1);
			model.addAttribute("formA", deletion);
			model.addAttribute("error", "");
			return "redirect:/accountDeletion";

		}
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
		if (accountForm == null || accountForm.getRoyalID().equals("-1")) {
			return "updateUserAccount";
		}
		model.addAttribute("accountForm", accountForm);
		model.addAttribute("royalID", royalID);
		model.addAttribute("roles", dao.getRoles());
		return "updateUserAccount";
	}

	@PostMapping("/updateUserAccount")
	public String resultUpdateUserAccount(@Valid UserAccount userAccount, final BindingResult result, Model model,
			final RedirectAttributes redirectAttributes) {
		System.out.println(userAccount.getRoyalID());
		if (result.hasErrors()) {
			System.out.println(result.getFieldError());
			userAccount = dao.getAccountByRoyalID(userAccount.getRoyalID());
			model.addAttribute("accountForm", userAccount);
			model.addAttribute("royalID", userAccount.getRoyalID());
			model.addAttribute("roles", dao.getRoles());
			return "updateUserAccount";
		} else {
			try {
				if (!userAccount.getEmail().equals(dao.getAccountByRoyalID(userAccount.getRoyalID()).getEmail())) {
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
				userAccount = dao.getAccountByRoyalID(userAccount.getRoyalID());
				model.addAttribute("accountForm", userAccount);
				model.addAttribute("emailerror", true);
				model.addAttribute("royalID", userAccount.getRoyalID());
				model.addAttribute("roles", dao.getRoles());
				return "updateUserAccount";
			}
		}
	}

// ==========================RequestMapping for "/403"=====================================
	@RequestMapping(value = "/403")
	public String accessDenied() {
		return "error/403";
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

	private ArrayList<String> makeRolesPretty(ArrayList<String> roles) {
		ArrayList<String> result = new ArrayList<String>();
		for (String role : roles) {
			switch (role) {
			case "CURR":
				result.add("Current Student");
				break;
			case "ALUM":
				result.add("Alumni");
				break;
			case "FACULTY":
				result.add("Faculty");
				break;
			}
		}
		return result;
	}
}