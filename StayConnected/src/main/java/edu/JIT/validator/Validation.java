package edu.JIT.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.JIT.model.RegistrationForm;

@Component
public class Validation implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		RegistrationForm webAccount = (RegistrationForm) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
		if (webAccount.getPassword().length() < 8 || webAccount.getPassword().length() > 32)
			errors.rejectValue("password", "Size.webAccount.password");
		if (!webAccount.getPassword().equals(webAccount.getConfirmPassword()))
			errors.rejectValue("confirmPassword", "Diff.webAccount.confimPassword");
		if(webAccount.getAccount().getRoles().size() < 1) 
			errors.rejectValue("account.roles", "Role.webAccount.roles");
	}
}
