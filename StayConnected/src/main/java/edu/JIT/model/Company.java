package edu.JIT.model;

import java.util.ArrayList;

public class Company {
	
	private String companyName;
	private String location;
	private String phoneNumber;
	private String companyAddress;
	private ArrayList<JobOpening> jobOpenings;
	private ArrayList<UserAccount> alumni;
	private ArrayList<UserAccount> faculty;
	private ArrayList<UserAccount> currentStudents;
	
	public Company() {
		super();
	}

	public Company(String companyName, String location, String phoneNumber, String companyAddress,
			ArrayList<JobOpening> jobOpenings, ArrayList<UserAccount> alumni, ArrayList<UserAccount> faculty,
			ArrayList<UserAccount> currentStudents) {
		super();
		this.companyName = companyName;
		this.location = location;
		this.phoneNumber = phoneNumber;
		this.companyAddress = companyAddress;
		this.jobOpenings = jobOpenings;
		this.alumni = alumni;
		this.faculty = faculty;
		this.currentStudents = currentStudents;
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

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public ArrayList<JobOpening> getJobOpenings() {
		return jobOpenings;
	}

	public void setJobOpenings(ArrayList<JobOpening> jobOpenings) {
		this.jobOpenings = jobOpenings;
	}

	public ArrayList<UserAccount> getAlumni() {
		return alumni;
	}

	public void setAlumni(ArrayList<UserAccount> alumni) {
		this.alumni = alumni;
	}

	public ArrayList<UserAccount> getFaculty() {
		return faculty;
	}

	public void setFaculty(ArrayList<UserAccount> faculty) {
		this.faculty = faculty;
	}

	public ArrayList<UserAccount> getCurrentStudents() {
		return currentStudents;
	}

	public void setCurrentStudents(ArrayList<UserAccount> currentStudents) {
		this.currentStudents = currentStudents;
	}
	
}
