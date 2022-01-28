package com.example.dbms1;


import java.util.HashMap;
import java.util.Map;

public class Entity {
	
	private String Title;
	
	private Map<String, String> StringFields;
	private Map<String, Double> DoubleFields;
	Entity()
	{
		
		DoubleFields = new HashMap<>();
		StringFields = new HashMap<>();
		
	}
	
	public Map<String, String> getStringFields() {
		return StringFields;
	}
	public void setStringFields(Map<String, String> stringFields) {
		StringFields = stringFields;
	}
	public Map<String, Double> getDoubleFields() {
		return DoubleFields;
	}
	public void setDoubleFields(Map<String, Double> doubleFields) {
		DoubleFields = doubleFields;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	

}
