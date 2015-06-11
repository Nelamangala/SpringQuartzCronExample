package com.ganesh.demo.quartzScheduling.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ganesh.demo.quartzScheduling.dto.CurrentJobDto;
import com.ganesh.demo.quartzScheduling.dto.JobInfoDto;

@Controller
public class BaseController {

	private static int counter = 0;
	private static final String VIEW_INDEX = "index";
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(BaseController.class);
	private Map<Integer, JobExecutionContext> jobContexts;
	private Map<Integer, TriggerKey> triggerKeysByJobKeyHashcode;
	
	@Autowired
	private Scheduler scheduler;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String welcome(ModelMap model) {

		model.addAttribute("message", "Welcome");
		model.addAttribute("counter", ++counter);
		logger.debug("[welcome] counter : {}", counter);

		// Spring uses InternalResourceViewResolver and return back index.jsp
		return VIEW_INDEX;

	}

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public String welcomeName(@PathVariable String name, ModelMap model) {

		model.addAttribute("message", "Welcome " + name);
		model.addAttribute("counter", ++counter);
		logger.debug("[welcomeName] counter : {}", counter);
		return VIEW_INDEX;

	}
	
	@RequestMapping(value = "/getScheduledJobs", method = RequestMethod.GET)
	public String getScheduledJobs( ModelMap model) {

		List<JobInfoDto> jobsInfo = new ArrayList<JobInfoDto>();
		triggerKeysByJobKeyHashcode = new HashMap<Integer, TriggerKey>();
		try {
			for (String groupName : scheduler.getJobGroupNames()) {

				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher
						.jobGroupEquals(groupName))) {

					String jobName = jobKey.getName();
					String jobGroup = jobKey.getGroup();

					// get job's trigger
					List<Trigger> triggers = (List<Trigger>) scheduler
							.getTriggersOfJob(jobKey);
					if(triggers.isEmpty()){
						continue;
					}
					Date nextFireTime = triggers.get(0).getNextFireTime();
					Date prevFireTime = triggers.get(0).getPreviousFireTime();

					Integer jobKeyHashCode = jobKey.hashCode();
					triggerKeysByJobKeyHashcode.put(jobKeyHashCode, triggers.get(0).getKey());
					
					final JobInfoDto jobInfo = new JobInfoDto();
					jobInfo.setJobGroup(jobGroup);
					jobInfo.setJobName(jobName);
					jobInfo.setNextFireTime(nextFireTime);
					jobInfo.setPrevFireTime(prevFireTime);
					jobInfo.setJobKeyHashCode(jobKeyHashCode);
					jobInfo.setJobStatus(scheduler.getTriggerState(triggers.get(0).getKey()).name());;
					
					jobsInfo.add(jobInfo);
					
				}

			}
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.debug("Failed to get scheduled jobs information.");
		}
		
		model.addAttribute("jobsInfo", jobsInfo);
		
		List<CurrentJobDto> currentExecutingJobs = new ArrayList<CurrentJobDto>();
		try {
			jobContexts = new HashMap<Integer, JobExecutionContext>(scheduler.getCurrentlyExecutingJobs().size());
			for(JobExecutionContext jobContext:scheduler.getCurrentlyExecutingJobs()){
				jobContexts.put(jobContext.hashCode(), jobContext);
				
				final CurrentJobDto currentJobDto = new CurrentJobDto();
				currentJobDto.setJobContextHashCode(jobContext.hashCode());
				currentJobDto.setJobName(jobContext.getJobDetail().getKey().getName());
				currentJobDto.setJobGroup(jobContext.getJobDetail().getKey().getGroup());
				currentJobDto.setFireTime(jobContext.getFireTime());
				currentJobDto.setJobInstanceIdentifier(jobContext.getFireInstanceId());
				
				currentExecutingJobs.add(currentJobDto);
			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("currentExecutingJobs", currentExecutingJobs);
		
		return VIEW_INDEX;

	}
	
	@RequestMapping(value = { "/startScheduledJobNow" }, method = RequestMethod.POST, params = { "jobName", "jobGroup" })
	public String startScheduledJob(@RequestParam String jobName,@RequestParam String jobGroup){
		
		final JobKey jobKey = this.getJobKey(jobName, jobGroup);
		if(jobKey != null){
			try {
				scheduler.triggerJob(jobKey);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.debug(String.format("Failed while trying to trigger job-name:%s, job-group:%s", jobName, jobGroup));
			}
		}

		
		return "redirect:/getScheduledJobs";
	}
	
	@RequestMapping(value = { "/stopScheduledJobNow" }, method = RequestMethod.POST, params = { "jobContextHashCode" })
	public String stopScheduledJob(@RequestParam Integer jobContextHashCode){
		
		final JobExecutionContext currentJobContext = jobContexts.get(jobContextHashCode);
		
		if(currentJobContext != null){
			try {
				scheduler.interrupt(currentJobContext.getJobDetail().getKey());
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.debug(String.format("Failed while trying to interrupt job-name:%s, job-group:%s", currentJobContext.getJobDetail().getKey().getName(), currentJobContext.getJobDetail().getKey().getGroup()));
			}
		}

		
		return "redirect:/getScheduledJobs";
	}
	
	@RequestMapping(value = { "/pauseJob" }, method = RequestMethod.POST, params = { "jobName", "jobGroup" })
	public String pauseJob(@RequestParam String jobName,@RequestParam String jobGroup){
		
		final JobKey jobKey = this.getJobKey(jobName, jobGroup);
		if(jobKey != null){
			try {
				scheduler.pauseJob(jobKey);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.debug(String.format("Failed while trying to pause job-name:%s, job-group:%s", jobName, jobGroup));
			}
		}

		
		return "redirect:/getScheduledJobs";
	}
	
	@RequestMapping(value = { "/resumeJob" }, method = RequestMethod.POST, params = { "jobName", "jobGroup" })
	public String resumeJob(@RequestParam String jobName,@RequestParam String jobGroup){
		
		final JobKey jobKey = this.getJobKey(jobName, jobGroup);
		if(jobKey != null){
			try {
				scheduler.resumeJob(jobKey);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.debug(String.format("Failed while trying to resume job-name:%s, job-group:%s", jobName, jobGroup));
			}
		}

		
		return "redirect:/getScheduledJobs";
	}
	
	@RequestMapping(value = { "/pauseAll" }, method = RequestMethod.GET)
	public String pauseAllJobs() {

		try {
			scheduler.pauseAll();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("Failed while trying to pause all jobs");
		}

		return "redirect:/getScheduledJobs";
	}
	
	@RequestMapping(value = { "/resumeAll" }, method = RequestMethod.GET)
	public String resumeAllJobs() {

		try {
			scheduler.resumeAll();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("Failed while trying to resume all jobs");
		}

		return "redirect:/getScheduledJobs";
	}
	
	private JobKey getJobKey(String jobName, String jobGroup){
		try {
			for (String groupName : scheduler.getJobGroupNames()) {

				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher
						.jobGroupEquals(groupName))) {

					if(jobName.equals(jobKey.getName()) && jobGroup.equals(jobKey.getGroup())){
						return jobKey;
					}
					
				}
			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug(String.format("Failed while trying to trigger JOBKEY with job details name:%s, group:%s", jobName, jobGroup));
		}
		
		return null;
	}

}