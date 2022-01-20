package com.mopp.prescription.model;

import lombok.Data;

@Data
public class Prescription {
	private int no;
	private String rxID;
	private int memNo;
	private String createDate;
	private String symptoms;
	private String isTaking;
	private String medicine;

	public Prescription() { }
	public Prescription(int no, String createDate, String medicine, String symptoms, String isTaking) {
		this.no = no;
		this.createDate = createDate;
		this.medicine = medicine;
		this.symptoms = symptoms;
		this.isTaking = isTaking;
	}
	public Prescription(String rxID, int memNo, String createDate, String medicine, String symptoms, String isTaking) {
		this.rxID = rxID;
		this.memNo = memNo;
		this.createDate = createDate;
		this.medicine = medicine;
		this.symptoms = symptoms;
		this.isTaking = isTaking;
	}
}
