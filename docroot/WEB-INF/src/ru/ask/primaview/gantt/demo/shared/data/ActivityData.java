package ru.ask.primaview.gantt.demo.shared.data;

import java.io.Serializable;
import java.util.Date;

public class ActivityData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Date planStart;
	private int duration;
	
	public ActivityData(){
		
	}
	
	public void setName(String name){
		this.name = name.replace("\"", "_");
	}
	
	public String getName(){
		return name;
	}
	
	public void setPlanStart(Date value){
		planStart = value;
	}
	public Date getPnalStart(){
		return planStart;
	}

	public void setDuration(int value){
		duration = value;
	}
	
	public int getDuration(){
		return duration;
	}

}
