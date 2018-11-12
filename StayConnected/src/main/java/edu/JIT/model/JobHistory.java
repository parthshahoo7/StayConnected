package edu.JIT.model;

import java.util.Date;

public class JobHistory {
	
	private String companyName;
	private String position;
	private Date startDate;
	private Date endDate;
	private boolean currentlyEmployed;
	
	public JobHistory(String companyName, String position, Date startDate, Date endDate, boolean currentlyEmployed) {
		super();
		this.companyName = companyName;
		this.position = position;
		this.startDate = startDate;
		this.endDate = endDate;
		this.currentlyEmployed = currentlyEmployed;
	}

	public JobHistory() {
		super();
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isCurrentlyEmployed() {
		return currentlyEmployed;
	}

	public void setCurrentlyEmployed(boolean currentlyEmployed) {
		this.currentlyEmployed = currentlyEmployed;
	}
	
}
