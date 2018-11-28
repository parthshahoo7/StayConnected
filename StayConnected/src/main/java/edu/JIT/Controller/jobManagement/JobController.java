package edu.JIT.Controller.jobManagement;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.JIT.Controller.jobManagement.form.SortJobOpening;
import edu.JIT.dao.daoInterfaces.JobOpeningDao;
import edu.JIT.dao.daoInterfaces.UserAccountDao;
import edu.JIT.model.accountManagement.Skill;
import edu.JIT.model.jobManagement.InternshipJobOpening;

@Controller
public class JobController {

	@Autowired
	UserAccountDao dao;

	@Autowired
	private JobOpeningDao jobOpenningDao;

	@GetMapping("/browsejobopenings")
	public String browseJobOpenings(@Valid SortJobOpening sortJobOpening, Model model) {
		List<InternshipJobOpening> jobs = jobOpenningDao.getAllJobOpenings();
		SortJobOpening sortedOpenings = new SortJobOpening();
		for (InternshipJobOpening internshipJobOpening : jobs) {
			if (!sortedOpenings.getCompanyName().contains(internshipJobOpening.getCompanyName().getCompanyName()))
				sortedOpenings.addCompanyName(internshipJobOpening.getCompanyName().getCompanyName());
			if (!sortedOpenings.getHoursPerWeek().contains(internshipJobOpening.getHoursPerWeek()))
				sortedOpenings.addHoursPerWeek(internshipJobOpening.getHoursPerWeek());
			if (!sortedOpenings.getlocations().contains(internshipJobOpening.getLocation()))
				sortedOpenings.addLocation(internshipJobOpening.getLocation());
			if (!sortedOpenings.getPositions().contains(internshipJobOpening.getPosition()))
				sortedOpenings.addPosition(internshipJobOpening.getPosition());
			if (!sortedOpenings.getProficiency().contains(internshipJobOpening.getProficiancy()))
				sortedOpenings.addProficency(internshipJobOpening.getProficiancy());
		}
		model.addAttribute("jobopenings", jobs);
		model.addAttribute("companies", sortedOpenings.getCompanyName());
		model.addAttribute("positions", sortedOpenings.getPositions());
		model.addAttribute("proficiencies", sortedOpenings.getProficiency());
		model.addAttribute("locations", sortedOpenings.getlocations());
		model.addAttribute("workhours", sortedOpenings.getHoursPerWeek());
		return "jobManagement/browsejobopenings";
	}

	@PostMapping("/browsejobopenings")
	public String browseJobOpeningsResult(@Valid SortJobOpening sortJobOpening, final BindingResult bindingResult,
			Model model) {
		List<InternshipJobOpening> jobs = jobOpenningDao.getAllJobOpenings();
		SortJobOpening sortedOpenings = new SortJobOpening();
		for (InternshipJobOpening internshipJobOpening : jobs) {
			if (!sortedOpenings.getCompanyName().contains(internshipJobOpening.getCompanyName().getCompanyName()))
				sortedOpenings.addCompanyName(internshipJobOpening.getCompanyName().getCompanyName());
			if (!sortedOpenings.getHoursPerWeek().contains(internshipJobOpening.getHoursPerWeek()))
				sortedOpenings.addHoursPerWeek(internshipJobOpening.getHoursPerWeek());
			if (!sortedOpenings.getlocations().contains(internshipJobOpening.getLocation()))
				sortedOpenings.addLocation(internshipJobOpening.getLocation());
			if (!sortedOpenings.getPositions().contains(internshipJobOpening.getPosition()))
				sortedOpenings.addPosition(internshipJobOpening.getPosition());
			if (!sortedOpenings.getProficiency().contains(internshipJobOpening.getProficiancy()))
				sortedOpenings.addProficency(internshipJobOpening.getProficiancy());
		}
		if (bindingResult.hasErrors()) {
			System.out.println(bindingResult.getFieldError());
			model.addAttribute("jobopenings", jobs);
			model.addAttribute("companies", sortedOpenings.getCompanyName());
			/*
			 * model.addAttribute("startDates", sortedOpenings.getStartDate());
			 * model.addAttribute("endDates", sortedOpenings.getEndDate());
			 */
			model.addAttribute("positions", sortedOpenings.getPositions());
			model.addAttribute("proficiencies", sortedOpenings.getProficiency());
			model.addAttribute("locations", sortedOpenings.getlocations());
			model.addAttribute("workhours", sortedOpenings.getHoursPerWeek());
			return "jobManagement/browsejobopenings";
		}
		ArrayList<InternshipJobOpening> jobopenings = new ArrayList<>();
		for (int i = 0; i < jobs.size(); i++) {
			if (!(sortJobOpening.getJobOpening().getCompanyName().getCompanyName().equals("0"))
					&& !(sortJobOpening.getJobOpening().getHoursPerWeek().equals("0"))
					&& !(sortJobOpening.getJobOpening().getLocation().equals("0"))
					&& !(sortJobOpening.getJobOpening().getPosition().equals("0"))
					&& !(sortJobOpening.getJobOpening().getProficiancy().equals("0"))) {
				if (sortJobOpening.getJobOpening().getCompanyName().getCompanyName()
						.equals(jobs.get(i).getCompanyName().getCompanyName())
						&& sortJobOpening.getJobOpening().getHoursPerWeek().equals(jobs.get(i).getHoursPerWeek())
						&& sortJobOpening.getJobOpening().getLocation().equals(jobs.get(i).getLocation())
						&& sortJobOpening.getJobOpening().getPosition().equals(jobs.get(i).getPosition())
						&& sortJobOpening.getJobOpening().getProficiancy().equals(jobs.get(i).getProficiancy())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getCompanyName().getCompanyName().equals("0"))
					&& !(sortJobOpening.getJobOpening().getHoursPerWeek().equals("0"))
					&& !(sortJobOpening.getJobOpening().getLocation().equals("0"))
					&& !(sortJobOpening.getJobOpening().getPosition().equals("0"))) {
				if (sortJobOpening.getJobOpening().getCompanyName().getCompanyName()
						.equals(jobs.get(i).getCompanyName().getCompanyName())
						&& sortJobOpening.getJobOpening().getHoursPerWeek().equals(jobs.get(i).getHoursPerWeek())
						&& sortJobOpening.getJobOpening().getLocation().equals(jobs.get(i).getLocation())
						&& sortJobOpening.getJobOpening().getPosition().equals(jobs.get(i).getPosition())) {
					System.out.println("child condition");
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getCompanyName().getCompanyName().equals("0"))
					&& !(sortJobOpening.getJobOpening().getHoursPerWeek().equals("0"))
					&& !(sortJobOpening.getJobOpening().getLocation().equals("0"))
					&& !(sortJobOpening.getJobOpening().getProficiancy().equals("0"))) {
				if (sortJobOpening.getJobOpening().getCompanyName().getCompanyName()
						.equals(jobs.get(i).getCompanyName().getCompanyName())
						&& sortJobOpening.getJobOpening().getHoursPerWeek().equals(jobs.get(i).getHoursPerWeek())
						&& sortJobOpening.getJobOpening().getLocation().equals(jobs.get(i).getLocation())
						&& sortJobOpening.getJobOpening().getProficiancy().equals(jobs.get(i).getProficiancy())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getCompanyName().getCompanyName().equals("0"))
					&& !(sortJobOpening.getJobOpening().getHoursPerWeek().equals("0"))
					&& !(sortJobOpening.getJobOpening().getPosition().equals("0"))
					&& !(sortJobOpening.getJobOpening().getProficiancy().equals("0"))) {
				if (sortJobOpening.getJobOpening().getCompanyName().getCompanyName()
						.equals(jobs.get(i).getCompanyName().getCompanyName())
						&& sortJobOpening.getJobOpening().getHoursPerWeek().equals(jobs.get(i).getHoursPerWeek())
						&& sortJobOpening.getJobOpening().getPosition().equals(jobs.get(i).getPosition())
						&& sortJobOpening.getJobOpening().getProficiancy().equals(jobs.get(i).getProficiancy())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getCompanyName().getCompanyName().equals("0"))
					&& !(sortJobOpening.getJobOpening().getLocation().equals("0"))
					&& !(sortJobOpening.getJobOpening().getPosition().equals("0"))
					&& !(sortJobOpening.getJobOpening().getProficiancy().equals("0"))) {
				if (sortJobOpening.getJobOpening().getCompanyName().getCompanyName()
						.equals(jobs.get(i).getCompanyName().getCompanyName())
						&& sortJobOpening.getJobOpening().getLocation().equals(jobs.get(i).getLocation())
						&& sortJobOpening.getJobOpening().getPosition().equals(jobs.get(i).getPosition())
						&& sortJobOpening.getJobOpening().getProficiancy().equals(jobs.get(i).getProficiancy())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getHoursPerWeek().equals("0"))
					&& !(sortJobOpening.getJobOpening().getLocation().equals("0"))
					&& !(sortJobOpening.getJobOpening().getPosition().equals("0"))
					&& !(sortJobOpening.getJobOpening().getProficiancy().equals("0"))) {
				if (sortJobOpening.getJobOpening().getHoursPerWeek().equals(jobs.get(i).getHoursPerWeek())
						&& sortJobOpening.getJobOpening().getLocation().equals(jobs.get(i).getLocation())
						&& sortJobOpening.getJobOpening().getPosition().equals(jobs.get(i).getPosition())
						&& sortJobOpening.getJobOpening().getProficiancy().equals(jobs.get(i).getProficiancy())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getCompanyName().getCompanyName().equals("0"))
					&& !(sortJobOpening.getJobOpening().getHoursPerWeek().equals("0"))
					&& !(sortJobOpening.getJobOpening().getLocation().equals("0"))) {
				if (sortJobOpening.getJobOpening().getCompanyName().getCompanyName()
						.equals(jobs.get(i).getCompanyName().getCompanyName())
						&& sortJobOpening.getJobOpening().getHoursPerWeek().equals(jobs.get(i).getHoursPerWeek())
						&& sortJobOpening.getJobOpening().getLocation().equals(jobs.get(i).getLocation())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getCompanyName().getCompanyName().equals("0"))
					&& !(sortJobOpening.getJobOpening().getHoursPerWeek().equals("0"))
					&& !(sortJobOpening.getJobOpening().getPosition().equals("0"))) {
				if (sortJobOpening.getJobOpening().getCompanyName().getCompanyName()
						.equals(jobs.get(i).getCompanyName().getCompanyName())
						&& sortJobOpening.getJobOpening().getHoursPerWeek().equals(jobs.get(i).getHoursPerWeek())
						&& sortJobOpening.getJobOpening().getPosition().equals(jobs.get(i).getPosition())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getCompanyName().getCompanyName().equals("0"))
					&& !(sortJobOpening.getJobOpening().getLocation().equals("0"))
					&& !(sortJobOpening.getJobOpening().getPosition().equals("0"))) {
				if (sortJobOpening.getJobOpening().getCompanyName().getCompanyName()
						.equals(jobs.get(i).getCompanyName().getCompanyName())
						&& sortJobOpening.getJobOpening().getLocation().equals(jobs.get(i).getLocation())
						&& sortJobOpening.getJobOpening().getPosition().equals(jobs.get(i).getPosition())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getHoursPerWeek().equals("0"))
					&& !(sortJobOpening.getJobOpening().getLocation().equals("0"))
					&& !(sortJobOpening.getJobOpening().getPosition().equals("0"))) {
				if (sortJobOpening.getJobOpening().getHoursPerWeek().equals(jobs.get(i).getHoursPerWeek())
						&& sortJobOpening.getJobOpening().getLocation().equals(jobs.get(i).getLocation())
						&& sortJobOpening.getJobOpening().getPosition().equals(jobs.get(i).getPosition())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getCompanyName().getCompanyName().equals("0"))
					&& !(sortJobOpening.getJobOpening().getHoursPerWeek().equals("0"))
					&& !(sortJobOpening.getJobOpening().getProficiancy().equals("0"))) {
				if (sortJobOpening.getJobOpening().getCompanyName().getCompanyName()
						.equals(jobs.get(i).getCompanyName().getCompanyName())
						&& sortJobOpening.getJobOpening().getHoursPerWeek().equals(jobs.get(i).getHoursPerWeek())
						&& sortJobOpening.getJobOpening().getProficiancy().equals(jobs.get(i).getProficiancy())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getCompanyName().getCompanyName().equals("0"))
					&& !(sortJobOpening.getJobOpening().getLocation().equals("0"))
					&& !(sortJobOpening.getJobOpening().getProficiancy().equals("0"))) {
				if (sortJobOpening.getJobOpening().getCompanyName().getCompanyName()
						.equals(jobs.get(i).getCompanyName().getCompanyName())
						&& sortJobOpening.getJobOpening().getLocation().equals(jobs.get(i).getLocation())
						&& sortJobOpening.getJobOpening().getProficiancy().equals(jobs.get(i).getProficiancy())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getHoursPerWeek().equals("0"))
					&& !(sortJobOpening.getJobOpening().getLocation().equals("0"))
					&& !(sortJobOpening.getJobOpening().getProficiancy().equals("0"))) {
				if (sortJobOpening.getJobOpening().getHoursPerWeek().equals(jobs.get(i).getHoursPerWeek())
						&& sortJobOpening.getJobOpening().getLocation().equals(jobs.get(i).getLocation())
						&& sortJobOpening.getJobOpening().getProficiancy().equals(jobs.get(i).getProficiancy())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getCompanyName().getCompanyName().equals("0"))
					&& !(sortJobOpening.getJobOpening().getPosition().equals("0"))
					&& !(sortJobOpening.getJobOpening().getProficiancy().equals("0"))) {
				if (sortJobOpening.getJobOpening().getCompanyName().getCompanyName()
						.equals(jobs.get(i).getCompanyName().getCompanyName())
						&& sortJobOpening.getJobOpening().getPosition().equals(jobs.get(i).getPosition())
						&& sortJobOpening.getJobOpening().getProficiancy().equals(jobs.get(i).getProficiancy())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getHoursPerWeek().equals("0"))
					&& !(sortJobOpening.getJobOpening().getPosition().equals("0"))
					&& !(sortJobOpening.getJobOpening().getProficiancy().equals("0"))) {
				if (sortJobOpening.getJobOpening().getHoursPerWeek().equals(jobs.get(i).getHoursPerWeek())
						&& sortJobOpening.getJobOpening().getPosition().equals(jobs.get(i).getPosition())
						&& sortJobOpening.getJobOpening().getProficiancy().equals(jobs.get(i).getProficiancy())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getLocation().equals("0"))
					&& !(sortJobOpening.getJobOpening().getPosition().equals("0"))
					&& !(sortJobOpening.getJobOpening().getProficiancy().equals("0"))) {
				if (sortJobOpening.getJobOpening().getLocation().equals(jobs.get(i).getLocation())
						&& sortJobOpening.getJobOpening().getPosition().equals(jobs.get(i).getPosition())
						&& sortJobOpening.getJobOpening().getProficiancy().equals(jobs.get(i).getProficiancy())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getCompanyName().getCompanyName().equals("0"))
					&& !(sortJobOpening.getJobOpening().getHoursPerWeek().equals("0"))) {
				if (sortJobOpening.getJobOpening().getCompanyName().getCompanyName()
						.equals(jobs.get(i).getCompanyName().getCompanyName())
						&& sortJobOpening.getJobOpening().getHoursPerWeek().equals(jobs.get(i).getHoursPerWeek())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getCompanyName().getCompanyName().equals("0"))
					&& !(sortJobOpening.getJobOpening().getLocation().equals("0"))) {
				if (sortJobOpening.getJobOpening().getCompanyName().getCompanyName()
						.equals(jobs.get(i).getCompanyName().getCompanyName())
						&& sortJobOpening.getJobOpening().getLocation().equals(jobs.get(i).getLocation())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getCompanyName().getCompanyName().equals("0"))
					&& !(sortJobOpening.getJobOpening().getPosition().equals("0"))) {
				if (sortJobOpening.getJobOpening().getCompanyName().getCompanyName()
						.equals(jobs.get(i).getCompanyName().getCompanyName())
						&& sortJobOpening.getJobOpening().getPosition().equals(jobs.get(i).getPosition())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getCompanyName().getCompanyName().equals("0"))
					&& !(sortJobOpening.getJobOpening().getProficiancy().equals("0"))) {
				if (sortJobOpening.getJobOpening().getCompanyName().getCompanyName()
						.equals(jobs.get(i).getCompanyName().getCompanyName())
						&& sortJobOpening.getJobOpening().getProficiancy().equals(jobs.get(i).getProficiancy())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getHoursPerWeek().equals("0"))
					&& !(sortJobOpening.getJobOpening().getLocation().equals("0"))) {
				if (sortJobOpening.getJobOpening().getHoursPerWeek().equals(jobs.get(i).getHoursPerWeek())
						&& sortJobOpening.getJobOpening().getLocation().equals(jobs.get(i).getLocation())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getHoursPerWeek().equals("0"))
					&& !(sortJobOpening.getJobOpening().getPosition().equals("0"))) {
				if (sortJobOpening.getJobOpening().getHoursPerWeek().equals(jobs.get(i).getHoursPerWeek())
						&& sortJobOpening.getJobOpening().getPosition().equals(jobs.get(i).getPosition())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getHoursPerWeek().equals("0"))
					&& !(sortJobOpening.getJobOpening().getProficiancy().equals("0"))) {
				if (sortJobOpening.getJobOpening().getHoursPerWeek().equals(jobs.get(i).getHoursPerWeek())
						&& sortJobOpening.getJobOpening().getProficiancy().equals(jobs.get(i).getProficiancy())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getLocation().equals("0"))
					&& !(sortJobOpening.getJobOpening().getPosition().equals("0"))) {
				if (sortJobOpening.getJobOpening().getLocation().equals(jobs.get(i).getLocation())
						&& sortJobOpening.getJobOpening().getPosition().equals(jobs.get(i).getPosition())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getLocation().equals("0"))
					&& !(sortJobOpening.getJobOpening().getProficiancy().equals("0"))) {
				if (sortJobOpening.getJobOpening().getLocation().equals(jobs.get(i).getLocation())
						&& sortJobOpening.getJobOpening().getProficiancy().equals(jobs.get(i).getProficiancy())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!(sortJobOpening.getJobOpening().getPosition().equals("0"))
					&& !(sortJobOpening.getJobOpening().getProficiancy().equals("0"))) {
				if (sortJobOpening.getJobOpening().getPosition().equals(jobs.get(i).getPosition())
						&& sortJobOpening.getJobOpening().getProficiancy().equals(jobs.get(i).getProficiancy())) {
					jobopenings.add(jobs.get(i));
				}
			}

			else if (!sortJobOpening.getJobOpening().getCompanyName().getCompanyName().equals("0")) {
				if (sortJobOpening.getJobOpening().getCompanyName().getCompanyName()
						.equals(jobs.get(i).getCompanyName().getCompanyName())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!sortJobOpening.getJobOpening().getHoursPerWeek().equals("0")) {
				if (sortJobOpening.getJobOpening().getHoursPerWeek().equals(jobs.get(i).getHoursPerWeek())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!sortJobOpening.getJobOpening().getLocation().equals("0")) {
				if (sortJobOpening.getJobOpening().getLocation().equals(jobs.get(i).getLocation())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!sortJobOpening.getJobOpening().getPosition().equals("0")) {
				if (sortJobOpening.getJobOpening().getPosition().equals(jobs.get(i).getPosition())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (!sortJobOpening.getJobOpening().getProficiancy().equals("0")) {
				if (sortJobOpening.getJobOpening().getProficiancy().equals(jobs.get(i).getProficiancy())) {
					jobopenings.add(jobs.get(i));
				}
			} else if (sortJobOpening.getJobOpening().getCompanyName().getCompanyName().equals("0")
					&& sortJobOpening.getJobOpening().getHoursPerWeek().equals("0")
					&& sortJobOpening.getJobOpening().getLocation().equals("0")
					&& sortJobOpening.getJobOpening().getPosition().equals("0")
					&& sortJobOpening.getJobOpening().getProficiancy().equals("0")) {
				jobopenings.add(jobs.get(i));
			}
		}
		model.addAttribute("jobopenings", jobopenings);
		model.addAttribute("companies", sortedOpenings.getCompanyName());
		model.addAttribute("positions", sortedOpenings.getPositions());
		model.addAttribute("proficiencies", sortedOpenings.getProficiency());
		model.addAttribute("locations", sortedOpenings.getlocations());
		model.addAttribute("workhours", sortedOpenings.getHoursPerWeek());
		return "jobManagement/browsejobopenings";
	}

	@GetMapping("/postJobOpenings")
	public String postJobOpeningForm(InternshipJobOpening jobOpening, Model model) {
		model.addAttribute("postjob", jobOpening);
		model.addAttribute("skills", dao.getSkills());
		return "jobManagement/postjobopening";
	}

	@PostMapping("/postJobOpenings")
	public String postJobOpening(@RequestParam(value = "ski", required = false) int[] ski,
			@Valid InternshipJobOpening jobOpening, final BindingResult result, Model model, Principal principal) {
		if (result.hasErrors()) {
			System.out.println(result.getFieldError());
			model.addAttribute("postjob", jobOpening);
			model.addAttribute("skills", dao.getSkills());
			return "jobManagement/postjobopening";
		}ArrayList<Skill> skills = (ArrayList<Skill>) dao.getSkills();
		if (ski != null) {
			Skill skill = null;
			for (int i = 0; i < ski.length; i++) {
				for (int j = 0; j < skills.size(); j++) {
					if (skills.get(j).getSkillID() == ski[i]) {
						skill = new Skill();
						skill.setSkillName(skills.get(j).getSkillName());
						skill.setSkillID(skills.get(j).getSkillID());
						jobOpening.addSkills(skill);
					}
				}
			}
		}
		jobOpenningDao.postJobOpening(jobOpening);
		return "redirect:/browsejobopenings";
	}
	
/*	@InitBinder
	public void initBinder(WebDataBinder binder) 
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}*/
}