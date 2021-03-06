package com.ganesh.demo.quartzScheduling.jobs;

import java.util.Date;

import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
 
public class JobC extends QuartzJobBean implements InterruptableJob{
	private volatile Thread  thisThread;
	private volatile boolean isJobInterrupted = false;
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(JobC.class);
	private JobKey jobKey = null;
	
	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext)
		throws JobExecutionException {
		thisThread = Thread.currentThread();
		jobKey = jobExecutionContext.getJobDetail().getKey();
		try {
			System.out.println("Job with Identifier:"+ jobExecutionContext.getFireInstanceId() + "Started running Job C at - " + new Date());
			Thread.sleep(180000);
			System.out.println("Job with Identifier:"+ jobExecutionContext.getFireInstanceId() + "Completed running Job C at - "+ new Date());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("Job with Identifier:"+ jobExecutionContext.getFireInstanceId() + "Got an interrupted exception for job C");
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
		logger.info("JobC " + jobKey + "  -- INTERRUPTING --");
	    isJobInterrupted = true;
	    if (thisThread != null) {
	      // this call causes the ClosedByInterruptException to happen
	      thisThread.interrupt(); 
	    }
		
	}
 
}
