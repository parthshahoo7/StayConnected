package edu.JIT.model;

import java.util.ArrayList;
import java.util.Date;

public class PartTimeJobOpening extends JobOpening {
	
	private String hoursPerWeek;

	public PartTimeJobOpening() {
		super();
	}

	public PartTimeJobOpening(String position, String location, Date startDate, String payrate, Company companyName,
			String jobDescription, ArrayList<Skill> skills, String hoursPerWeek) {
		super(position, location, startDate, payrate, companyName, jobDescription, skills);
		this.setHoursPerWeek(hoursPerWeek);
	}

	public String getHoursPerWeek() {
		return hoursPerWeek;
	}

	public void setHoursPerWeek(String hoursPerWeek) {
		this.hoursPerWeek = hoursPerWeek;
	}
	
}
