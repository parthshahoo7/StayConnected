package edu.JIT.model.jobManagement;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import edu.JIT.model.accountManagement.Skill;

public class InternshipJobOpening extends PartTimeJobOpening {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;

	public InternshipJobOpening() {
		super();
	}

	public InternshipJobOpening(int jobID, String position, String location, String proficiancy, Date startDate,
			String payrate, Company companyName, String jobDescription, ArrayList<Skill> skills, String hoursPerWeek,
			Date endDate) {
		super(jobID, position, location, proficiancy, startDate, payrate, companyName, jobDescription, skills,
				hoursPerWeek);
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}