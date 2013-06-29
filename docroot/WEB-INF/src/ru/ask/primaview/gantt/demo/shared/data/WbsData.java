package ru.ask.primaview.gantt.demo.shared.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WbsData implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<ActivityData> activities;
	private List<WbsData> childs;
	private String name;
	private Date planStart;
	private int duration;

	public WbsData() {
		activities = new ArrayList<ActivityData>();
		childs = new ArrayList<WbsData>();
	}

	public void setName(String name) {
		this.name = name.replace("\"", "_");
	}

	public void addActivity(ActivityData acticity) {
		activities.add(acticity);
	}

	public void addChild(WbsData wbs) {
		childs.add(wbs);
	}

	public List<WbsData> getChilds() {
		return childs;
	}

	public List<ActivityData> getActivities() {
		return activities;
	}

	public String getName() {
		return name;
	}

	public void setPlanStart(Date value) {
		planStart = value;
	}

	public Date getPnalStart() {
		return planStart;
	}

	public void setDuration(int value) {
		duration = value;
	}

	public int getDuration() {
		return duration;
	}
}
