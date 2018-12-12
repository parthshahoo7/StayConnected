package edu.JIT.dao.daoImplementation.jobManagement;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import edu.JIT.dao.daoInterfaces.jobManagement.JobOpeningDao;
import edu.JIT.dao.mapper.jobManagement.CompanyListMapper;
import edu.JIT.dao.mapper.jobManagement.JobOpeningMapper;
import edu.JIT.model.jobManagement.Company;
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
		ArrayList<String> companiesName = new ArrayList<>();
		for (int i = 0; i < getAllCompany().size(); i++) {
			companiesName.add(getAllCompany().get(i).getCompanyName());
		}
		try {
			String postJobSql = "insert into stayconnected.jobopening(jobid,position,location,startdate,payrate,jobdescription,hoursperweek,enddate,companyname,phone_number) values(?,?,?,?,?,?,?,?,?,?)";
			String insertCompanySql = "insert into stayconnected.company(name,address,phone_number) values(?,?,?)";
			if (!(companiesName.contains(newJob.getCompanyName().getCompanyName()))) {
				jdbcTemplate.update(insertCompanySql, newJob.getCompanyName().getCompanyName(), newJob.getLocation(),
						newJob.getCompanyName().getPhoneNumber());
			}
			if (!newJob.getHoursPerWeek().equals("")) {
				jdbcTemplate.update(postJobSql, newJob.getJobID(), newJob.getPosition(), newJob.getLocation(),
						newJob.getStartDate(), newJob.getPayrate(), newJob.getJobDescription(),
						Integer.parseInt(newJob.getHoursPerWeek()), newJob.getEndDate(),
						newJob.getCompanyName().getCompanyName(), newJob.getCompanyName().getPhoneNumber());
			} else {
				jdbcTemplate.update(postJobSql, newJob.getJobID(), newJob.getPosition(), newJob.getLocation(),
						newJob.getStartDate(), newJob.getPayrate(), newJob.getJobDescription(), null,
						newJob.getEndDate(), newJob.getCompanyName().getCompanyName(),
						newJob.getCompanyName().getPhoneNumber());
			}
			String jobQualificationSql = "insert into stayconnected.jobqualification(proficiancy,jobid,skillid) values(?,?,?)";
			for (int i = 0; i < newJob.getSkills().size(); i++) {
				jdbcTemplate.update(jobQualificationSql, newJob.getProficiancy(), newJob.getJobID(),
						newJob.getSkills().get(i).getSkillID());
			}
			transactionManager.commit(status);
		} catch (Exception e) {
			System.out.println("Error in posting Job, rolling back");
			transactionManager.rollback(status);
			return null;
		}
		return newJob;
	}

	@Override
	public List<InternshipJobOpening> getAllJobOpenings() {
		// TODO Auto-generated method stub
		String jobOpeningSQL = "select p.*, q.proficiancy as proficiancy from stayconnected.jobopening as p, stayconnected.jobqualification as q where p.jobid=q.jobid group by p.jobid,q.proficiancy order by p.jobid";
		List<InternshipJobOpening> jobOpenings = jdbcTemplate.query(jobOpeningSQL, new JobOpeningMapper());
		return jobOpenings;

	}

	@Override
	public List<Company> getAllCompany() {
		// TODO Auto-generated method stub

		String jobOpeningSQL = "select * from stayconnected.company";
		List<Company> companies = jdbcTemplate.query(jobOpeningSQL, new CompanyListMapper());
		return companies;

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