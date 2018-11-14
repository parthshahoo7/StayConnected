package edu.JIT.model;

public class RegistrationForm {

	private UserAccount account;
	private String password;
	private String confirmPassword;
	private String specialCode;

	public RegistrationForm() {
		super();
		account = new UserAccount();
	}

	public RegistrationForm(UserAccount account, String password, String confirmPassword, String specialCode) {
		super();
		this.account = account;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.specialCode = specialCode;
	}

	public UserAccount getAccount() {
		return account;
	}

	public void setAccount(UserAccount account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getSpecialCode() {
		return specialCode;
	}

	public void setSpecialCode(String specialCode) {
		this.specialCode = specialCode;
	}
}