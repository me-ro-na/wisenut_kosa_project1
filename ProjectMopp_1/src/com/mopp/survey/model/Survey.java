package com.mopp.survey.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.beans.property.SimpleIntegerProperty;
import lombok.Data;

@Data
public class Survey {
	private int no;
	private int bmi;
	private int mem_no;
	private String surveyDate;
	private SimpleIntegerProperty height;
	private SimpleIntegerProperty weight;
	private SimpleIntegerProperty drink;
	private SimpleIntegerProperty sleep;
	private SimpleIntegerProperty gender;
	private SimpleIntegerProperty age;
	public ArrayList<Trouble> troubleArr;
	private SimpleIntegerProperty trouble;
	public String trb_name;
	
	public Survey() {
		this.height = new SimpleIntegerProperty();
		this.weight = new SimpleIntegerProperty();
		this.drink = new SimpleIntegerProperty();
		this.sleep = new SimpleIntegerProperty();
		this.trouble = new SimpleIntegerProperty();
		Calendar cal= Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //"yyyy:MM:dd-HH:mm:ss"
		surveyDate = dateFormat.format(cal.getTime()); 
		troubleArr = new ArrayList<Trouble>();
	}
	
	public Survey(int height, int weight, int drink, int sleep, int trouble) {
		this.height = new SimpleIntegerProperty(height);
		this.weight = new SimpleIntegerProperty(weight);
		this.drink = new SimpleIntegerProperty(drink);
		this.sleep = new SimpleIntegerProperty(sleep);
		this.trouble = new SimpleIntegerProperty(trouble);
	}
	
	public Survey(int height, int weight, int drink, int sleep, int trouble, String trb_name) {
		this.height = new SimpleIntegerProperty(height);
		this.weight = new SimpleIntegerProperty(weight);
		this.drink = new SimpleIntegerProperty(drink);
		this.sleep = new SimpleIntegerProperty(sleep);
		this.trouble = new SimpleIntegerProperty(trouble);
		this.trb_name = troubleName(trouble);
	}
	
	public String troubleName(int t) 
	{
		String trb_name = "";
		switch (t) {
		case 1:
			trb_name = "혈관/혈액순환";
			break;
		case 2:
			trb_name = "소화/장";
			break;
		case 3:
			trb_name = "피부";
			break;
		case 4:
			trb_name = "눈";
			break;
		case 5:
			trb_name = "뇌";
			break;
		case 6:
			trb_name = "피로";
			break;
		case 7:
			trb_name = "면역";
			break;
		case 8:
			trb_name = "뼈/관절";
			break;
		case 9:
			trb_name = "모발";
			break;
		}
		return trb_name;
	}	
}
