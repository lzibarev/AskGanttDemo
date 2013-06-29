package ru.ask.primaview.gantt.demo.shared.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WbsData implements Serializable {

	public List<ActivityData> activities;
	public List<WbsData> childs;
	public String name;
	
	
	public WbsData(){
		activities  = new ArrayList<ActivityData>();
		childs = new ArrayList<WbsData>();
	}
	
	public void setName(String name){
		this.name = name.replace("\"", " ");
	}
	
	public void addActivity(ActivityData acticity){
		activities.add(acticity);
	}
	
	public void addChild(WbsData wbs){
		childs.add(wbs);
	}
	
	public List<WbsData> getChilds(){
		return childs;
	}
	
	public List<ActivityData> getActivities(){
		return activities;
	}
	
	public String getName(){
		return name;
	}
}
