package edu.JIT.Controller.form;

import java.util.ArrayList;

public class BrowseUserForm {
	private String userType;
	private ArrayList<String> criteria;
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public ArrayList<String> getCriteria() {
		return criteria;
	}
	public void setCriteria(ArrayList<String> criteria) {
		this.criteria = criteria;
	}
}
