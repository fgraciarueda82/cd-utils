package com.bbva.cdutils.bean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EmployeeBean {
	private String id;
	private String name;
	private ArrayList<ImputationBean> imputationRecords;
	
	public EmployeeBean(String id, String name, ArrayList<ImputationBean> imputationRecords) {
		super();
		this.id = id;
		this.name = name;
		this.imputationRecords = imputationRecords;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ImputationBean> getImputationRecords() {
		return imputationRecords;
	}

	public void setImputationRecords(ArrayList<ImputationBean> imputationRecords) {
		this.imputationRecords = imputationRecords;
	}
	
	public void addImputationRecord(ImputationBean imputationRecord) {
		if(this.imputationRecords==null) {
			this.imputationRecords = new ArrayList<ImputationBean>();
		}
		this.imputationRecords.add(imputationRecord);
		this.imputationRecords.sort(Comparator.comparing(ImputationBean::getDate));
	}
}
