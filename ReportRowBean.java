package com.bbva.cdutils.bean;

import java.util.Date;

public class ReportRowBean {
	private String employeeName;
	private String employeeId;
	private String employeeProfile;
	private String project;
	private String time;
	private boolean billable;
	private String client;
	private String state;
	private Date date;
	private String family;
	private String deliveryManagerName;
	private String deliveryManagerId;
	private String deliveryCoordinatorName;
	private String deliveryCoordinatorId;
	
	public ReportRowBean() {
		super();
	}
	
	public ReportRowBean(String employeeName, String employeeProfile, String project, String time, boolean billable, 
			String client, String state, Date date, String family, String deliveryManager, String deliveryCoordinator) {
		super();
		this.employeeName = employeeName;
		this.employeeProfile = employeeProfile;
		this.project = project;
		this.billable = billable;
		this.client = client;
		this.state = state;
		this.date = date;
		this.family = family;
		this.deliveryManagerName = deliveryManager;
		this.deliveryCoordinatorName = deliveryCoordinator;
	}
	
	public String getEmployeeName() {
		return employeeName;
	}
	
	public void setEmployeeName(String employeeName) {	
		String name = employeeName.substring(0, employeeName.indexOf("("));
		String id = employeeName.substring(employeeName.indexOf("(") + 1, employeeName.indexOf(")"));
		this.employeeName = name;
		this.employeeId = id;
	}
	
	public String getEmployeeProfile() {
		return employeeProfile;
	}
	
	public void setEmployeeProfile(String employeeProfile) {
		this.employeeProfile = employeeProfile;
	}
	
	public String getProject() {
		return project;
	}
	
	public void setProject(String project) {
		this.project = project;
	}
	
	public boolean isBillable() {
		return billable;
	}
	
	public void setBillable(boolean billable) {
		this.billable = billable;
	}
	
	public void setBillable(String billable) {
		switch (billable) {
			case "Sí":
				this.billable = true;
				break;
			default:
				this.billable = false;
				break;
		}
	}
	
	public String getClient() {
		return client;
	}
	
	public void setClient(String client) {
		this.client = client;
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
	
	public String getFamily() {
		return family;
	}
	
	public void setFamily(String family) {
		this.family = family;
	}
		
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public void setTime(Date time) {
		this.time = String.valueOf(time.getTime());
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getDeliveryManagerName() {		
		return deliveryManagerName;
	}

	public void setDeliveryManagerName(String deliveryManagerName) {
		String name = new String(""), id = new String("");
		if(!deliveryManagerName.isEmpty()) {
			name = deliveryManagerName.substring(0, deliveryManagerName.indexOf("("));
			id = deliveryManagerName.substring(deliveryManagerName.indexOf("(") + 1, deliveryManagerName.indexOf(")"));
		}
		this.deliveryManagerName = name;
		this.deliveryManagerId = id;
	}

	public String getDeliveryManagerId() {
		return deliveryManagerId;
	}

	public void setDeliveryManagerId(String deliveryManagerId) {
		this.deliveryManagerId = deliveryManagerId;
	}

	public String getDeliveryCoordinatorName() {
		return deliveryCoordinatorName;
	}

	public void setDeliveryCoordinatorName(String deliveryCoordinatorName) {	
		String name = new String(""), id = new String("");
		if(!deliveryCoordinatorName.isEmpty()) {
			name = deliveryCoordinatorName.substring(0, deliveryCoordinatorName.indexOf("("));
			id = deliveryCoordinatorName.substring(deliveryCoordinatorName.indexOf("(") + 1, deliveryCoordinatorName.indexOf(")"));	
		}
		this.deliveryCoordinatorName = name;
		this.deliveryCoordinatorId = id;
	}

	public String getDeliveryCoordinatorId() {
		return deliveryCoordinatorId;
	}

	public void setDeliveryCoordiantorId(String deliveryCoordinatorId) {
		this.deliveryCoordinatorId = deliveryCoordinatorId;
	}
}
