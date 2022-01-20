package com.mopp.profile.model;

import java.util.Date;

import lombok.Data;

@Data
public class Member {
	private int memNo;
	private String memId;
	private String name;
	private String pwd;
	private String tel;
	private String gender;
	private String email1;
	private String email2;
	private Date birthday;
	private String regDate;
	private String isAdmin;
	
	
}
