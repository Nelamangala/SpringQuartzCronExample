package com.ganesh.demo.quartzScheduling.dto;

import java.util.Date;

public class JobInfoDto {
	private String jobName;
	private String jobGroup;
	private Date nextFireTime;
	private Date prevFireTime;
	private Integer jobKeyHashCode;
	private String jobStatus;

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

	public Date getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

	public Date getPrevFireTime() {
		return prevFireTime;
	}

	public void setPrevFireTime(Date prevFireTime) {
		this.prevFireTime = prevFireTime;
	}

	public Integer getJobKeyHashCode() {
		return jobKeyHashCode;
	}

	public void setJobKeyHashCode(Integer jobKeyHashCode) {
		this.jobKeyHashCode = jobKeyHashCode;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

}
