package ru.ask.primaview.gantt.demo.shared.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WbsData extends GraphData implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<ActivityData> activities;
	private List<WbsData> childs;

	public WbsData() {
		planStart = MINDATE;
		planFinish = MINDATE;
		duration = 1;
		activities = new ArrayList<ActivityData>();
		childs = new ArrayList<WbsData>();
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

}
