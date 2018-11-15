package edu.JIT.dao.daoInterfaces;

import java.util.List;

import org.springframework.stereotype.Repository;

import edu.JIT.model.jobManagement.JobOpening;

@Repository
public interface JobOpeningDao {
	public JobOpening postJobOpening(JobOpening newJob);

	public List<JobOpening> getAllJobOpenings();

	public JobOpening getJobOpening(int jobID);

	public boolean removeJobOpening(int jobID);
}