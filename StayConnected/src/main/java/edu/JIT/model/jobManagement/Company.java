package edu.JIT.model.jobManagement;
/*
import java.util.ArrayList;

import edu.JIT.model.accountManagement.UserAccount;*/

public class Company {

	private String companyName;
	private String location;
	private String phoneNumber;
	/*
	 * private String companyAddress; private ArrayList<JobOpening> jobOpenings;
	 * private ArrayList<UserAccount> alumni; private ArrayList<UserAccount>
	 * faculty; private ArrayList<UserAccount> currentStudents;
	 */

	public Company() {
		super();
	}

	public Company(String companyName, String location, String phoneNumber) {
		super();
		this.companyName = companyName;
		this.location = location;
		this.phoneNumber = phoneNumber;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}