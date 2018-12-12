package edu.JIT.dao.daoInterfaces.jobManagement;

import java.util.List;

import org.springframework.stereotype.Repository;

import edu.JIT.model.jobManagement.Company;
import edu.JIT.model.jobManagement.InternshipJobOpening;

@Repository
public interface JobOpeningDao {
	public InternshipJobOpening postJobOpening(InternshipJobOpening newJob);

	public List<InternshipJobOpening> getAllJobOpenings();

	public InternshipJobOpening getJobOpening(int jobID);

	public boolean removeJobOpening(int jobID);

	public List<Company> getAllCompany();
}