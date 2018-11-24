package edu.JIT.Controller.form.validator;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.JIT.Controller.form.UpdateAccountForm;

public class updateFormValidation implements Validator {
	String phoneNumber = "\\d{10}";
	
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		UpdateAccountForm updatedInfo = (UpdateAccountForm) target;
		try {
		      InternetAddress emailAddr = new InternetAddress(updatedInfo.getEmail());
		      emailAddr.validate();
		   } catch (AddressException ex) {
		      errors.rejectValue("email", "invalid email address");
		   }
		
		if(!updatedInfo.getPhoneNumber().matches(phoneNumber)) {
			errors.rejectValue("phoneNumber", "badly formed phone #");
		}
		
		if (updatedInfo.getPassword().length() < 8 || updatedInfo.getPassword().length() > 32)
			errors.rejectValue("password", "Size.webAccount.password");
		if (!updatedInfo.getPassword().equals(updatedInfo.getConfirmPassword()))
			errors.rejectValue("confirmPassword", "Diff.webAccount.confimPassword");
		
		
	}

}
