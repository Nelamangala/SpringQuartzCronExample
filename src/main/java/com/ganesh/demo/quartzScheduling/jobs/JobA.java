package com.ganesh.demo.quartzScheduling.jobs;

import java.util.Date;

import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
 
public class JobA extends QuartzJobBean implements InterruptableJob{
 
	private volatile Thread  thisThread;
	private volatile boolean isJobInterrupted = false;
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(JobA.class);
	private JobKey jobKey = null;
	
	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext)
		throws JobExecutionException {
		thisThread = Thread.currentThread();
		jobKey = jobExecutionContext.getJobDetail().getKey();
		try {
			System.out.println("Job with Identifier:"+ jobExecutionContext.getFireInstanceId() + "Started running Job A at - " + new Date());
			Thread.sleep(60000);
			System.out.println("Job with Identifier:"+ jobExecutionContext.getFireInstanceId() + "Completed running Job A at - "+ new Date());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("Job with Identifier:"+ jobExecutionContext.getFireInstanceId() + "Got an interrupted exception for job A");
		} finally {
			if (isJobInterrupted) {
				logger.info("Job with Identifier:"+ jobExecutionContext.getFireInstanceId() + "Job " + jobKey + " did not complete");
			} else {
				logger.info("Job with Identifier:"+ jobExecutionContext.getFireInstanceId() + "Job " + jobKey + " completed at " + new Date());
			}
		}

		
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		logger.info("JobA " + jobKey + "  -- INTERRUPTING --");
	    isJobInterrupted = true;
	    if (thisThread != null) {
	      // this call causes the ClosedByInterruptException to happen
	      thisThread.interrupt(); 
	    }
		
	}
 
}
