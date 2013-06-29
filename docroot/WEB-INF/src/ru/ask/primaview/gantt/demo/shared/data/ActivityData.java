package ru.ask.primaview.gantt.demo.shared.data;

import java.io.Serializable;
import java.util.Date;

public class ActivityData implements Serializable {

	private String name;
	private Date planStart;
	private Date planFinish;
	
	public ActivityData(){
		
	}
	
	public void setName(String name){
		this.name = name.replace("\"", " ");
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
	
	public void setPlanFinish(Date value){
		planFinish = value;
	}
	public Date getPlanFinish(){
		return planFinish;
	}

}
