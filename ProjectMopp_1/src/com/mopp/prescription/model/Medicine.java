package com.mopp.prescription.model;

import lombok.Data;

@Data
public class Medicine {
	private int medNo;
	private String medName;
	private String medCode;
	private String medMade;
	private	String medWarn;
}
