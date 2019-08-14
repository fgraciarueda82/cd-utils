package com.bbva.cdutils.bean;

import java.util.Date;

public class ImputationBean implements Comparable<ImputationBean> {
	private String projectName;
	private boolean billable;
	private String time;
	private String state;
	private Date date;
	
	public ImputationBean(String projectName, boolean billable, String time, String state, Date date) {
		super();
		this.projectName = projectName;
		this.billable = billable;
		this.time = time;
		this.state = state;
		this.date = date;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public boolean isBillable() {
		return billable;
	}

	public void setBillable(boolean billable) {
		this.billable = billable;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int compareTo(ImputationBean o) {
		if (getDate() == null || o.getDate() == null) {
			return 0;
		}
		return getDate().compareTo(o.getDate());
	}
}
