package com.ganesh.demo.quartzScheduling.dto;

import java.util.Date;

public class CurrentJobDto {
	private Integer jobContextHashCode;
	private String jobName;
	private String jobGroup;
	private Date fireTime;
	private String jobInstanceIdentifier;

	public Integer getJobContextHashCode() {
		return jobContextHashCode;
	}

	public void setJobContextHashCode(Integer jobContextHashCode) {
		this.jobContextHashCode = jobContextHashCode;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public Date getFireTime() {
		return fireTime;
	}

	public void setFireTime(Date fireTime) {
		this.fireTime = fireTime;
	}

	public String getJobInstanceIdentifier() {
		return jobInstanceIdentifier;
	}

	public void setJobInstanceIdentifier(String jobInstanceIdentifier) {
		this.jobInstanceIdentifier = jobInstanceIdentifier;
	}

}
