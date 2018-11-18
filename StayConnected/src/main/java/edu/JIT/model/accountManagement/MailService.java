package edu.JIT.model.accountManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import edu.JIT.Controller.form.RegistrationForm;

@Service
public class MailService {
	
	private JavaMailSender javaMailSender;
	
	@Autowired
	public MailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	public void sendEmail(RegistrationForm user) throws MailException {

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getAccount().getEmail());
		mail.setFrom("stayconnectedjit@yahoo.com");
		mail.setSubject("StayConnected Special Code");
		mail.setText("Your special code is: " + user.getSpecialCode());

		javaMailSender.send(mail);
	}
}
