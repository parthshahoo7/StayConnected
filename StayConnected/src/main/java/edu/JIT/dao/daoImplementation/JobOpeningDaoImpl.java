package edu.JIT.dao.daoImplementation;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import edu.JIT.dao.daoInterfaces.JobOpeningDao;
import edu.JIT.dao.mapper.jobManagement.JobOpeningMapper;
import edu.JIT.model.jobManagement.InternshipJobOpening;

@Repository
public class JobOpeningDaoImpl implements JobOpeningDao {

	@Autowired
	private DataSourceTransactionManager transactionManager;

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
	}

	@Override
	public InternshipJobOpening postJobOpening(InternshipJobOpening newJob) {
		// TODO Auto-generated method stub
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			String postJobSql = "insert into stayconnected.jobopening(jobid,position,location,startdate,payrate,jobdescription,hoursperweek,enddate,companyname) values(?,?,?,?,?,?,?,?,?)";
			if (!newJob.getHoursPerWeek().equals("")) {
				jdbcTemplate.update(postJobSql, newJob.getJobID(), newJob.getPosition(), newJob.getLocation(),
						newJob.getStartDate(), newJob.getPayrate(), newJob.getJobDescription(),
						Integer.parseInt(newJob.getHoursPerWeek()), newJob.getEndDate(),
						newJob.getCompanyName().getCompanyName());
			} else {
				jdbcTemplate.update(postJobSql, newJob.getJobID(), newJob.getPosition(), newJob.getLocation(),
						newJob.getStartDate(), newJob.getPayrate(), newJob.getJobDescription(), null,
						newJob.getEndDate(), newJob.getCompanyName().getCompanyName());
			}
			String jobQualificationSql = "insert into stayconnected.jobqualification(proficiancy,jobid,skillid) values(?,?,?)";
			for (int i = 0; i < newJob.getSkills().size(); i++) {
				jdbcTemplate.update(jobQualificationSql, newJob.getProficiancy(), newJob.getJobID(),
						newJob.getSkills().get(i).getSkillID());
			}transactionManager.commit(status);
		} catch (DataAccessException e) {
			System.out.println("Error in posting Job, rolling back");
			transactionManager.rollback(status);
			throw (e);
		}
		return newJob;
	}

	@Override
	public List<InternshipJobOpening> getAllJobOpenings() {
		// TODO Auto-generated method stub

		String jobOpeningSQL = "select p.*, q.proficiancy as proficiancy from stayconnected.jobopening as p, stayconnected.jobqualification as q where p.jobid=q.jobid";
		List<InternshipJobOpening> jobOpenings = jdbcTemplate.query(jobOpeningSQL, new JobOpeningMapper());
		return jobOpenings;

	}

	@Override
	public InternshipJobOpening getJobOpening(int jobID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeJobOpening(int jobID) {
		// TODO Auto-generated method stub
		return false;
	}

}
