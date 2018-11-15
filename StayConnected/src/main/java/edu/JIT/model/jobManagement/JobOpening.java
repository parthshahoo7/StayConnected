package edu.JIT.model.jobManagement;

import java.util.ArrayList;
import java.util.Date;

import edu.JIT.model.accountManagement.Skill;

public class JobOpening {

	private String position;
	private String location;
	private Date startDate;
	private String payrate;
	private Company companyName;
	private String jobDescription;
	private ArrayList<Skill> skills;

	public JobOpening() {
		super();
	}

	public JobOpening(String position, String location, Date startDate, String payrate, Company companyName,
			String jobDescription, ArrayList<Skill> skills) {
		super();
		this.position = position;
		this.location = location;
		this.startDate = startDate;
		this.payrate = payrate;
		this.companyName = companyName;
		this.jobDescription = jobDescription;
		this.skills = skills;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getPayrate() {
		return payrate;
	}

	public void setPayrate(String payrate) {
		this.payrate = payrate;
	}

	public Company getCompanyName() {
		return companyName;
	}

	public void setCompanyName(Company companyName) {
		this.companyName = companyName;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public ArrayList<Skill> getSkills() {
		return skills;
	}

	public void setSkills(ArrayList<Skill> skills) {
		this.skills = skills;
	}
}