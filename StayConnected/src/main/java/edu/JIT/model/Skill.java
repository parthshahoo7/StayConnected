package edu.JIT.model;

public class Skill {
	
	private String skillName;
	private String proficiency;
	
	public Skill(String skillName, String proficiency) {
		super();
		this.skillName = skillName;
		this.proficiency = proficiency;
	}

	public Skill() {
		super();
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public String getProficiency() {
		return proficiency;
	}

	public void setProficiency(String proficiency) {
		this.proficiency = proficiency;
	}
	
}
