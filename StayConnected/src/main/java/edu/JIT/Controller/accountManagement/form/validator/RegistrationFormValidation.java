package edu.JIT.Controller.accountManagement.form.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.JIT.Controller.accountManagement.form.RegistrationForm;

@Component
public class RegistrationFormValidation implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		RegistrationForm webAccount = (RegistrationForm) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
		if (webAccount.getPassword().length() < 8 || webAccount.getPassword().length() > 32)
			errors.rejectValue("password", "Size.webAccount.password");
		if (!webAccount.getPassword().equals(webAccount.getConfirmPassword()))
			errors.rejectValue("confirmPassword", "Diff.webAccount.confimPassword");
		if (webAccount.getAccount().getRoles().size() < 1)
			errors.rejectValue("account.roles", "Role.webAccount.roles");
	}
}