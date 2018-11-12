package edu.JIT.model;

import java.util.ArrayList;
import java.util.Date;

public class InternshipJobOpening extends PartTimeJobOpening {
	
	private Date endDate;

	public InternshipJobOpening() {
		super();
	}

	public InternshipJobOpening(String position, String location, Date startDate, String payrate, Company companyName,
			String jobDescription, ArrayList<Skill> skills, String hoursPerWeek, Date endDate) {
		super(position, location, startDate, payrate, companyName, jobDescription, skills, hoursPerWeek);
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
