package ru.ask.primaview.gantt.demo.client.dummydata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.ask.primaview.gantt.demo.shared.data.ActivityData;
import ru.ask.primaview.gantt.demo.shared.data.WbsData;

import com.gantt.client.config.GanttConfig.TaskType;
import com.sencha.gxt.core.client.util.DateWrapper;

public class DemoData3 implements IDemoData {

	private List<WbsData> data;

	public DemoData3(List<WbsData> data) {
		this.data = data;
	}

	@Override
	public Task getTasks() {
		DateWrapper dw = new DateWrapper(new Date()).clearTime().addDays(-7);

		ArrayList<Task> list = new ArrayList<Task>();
		for (WbsData wbs : data) {
			Task task = getTaskByWbs(wbs, dw);
			list.add(task);
		}
		Task root = new Task(list);
		return root;
	}

	private Task getTaskByWbs(WbsData wbs, DateWrapper dw) {
		Task task = new Task(wbs.getName(), dw.addDays(1).asDate(), 10, 30,
				TaskType.PARENT);
		for (ActivityData activity : wbs.getActivities()) {
			Task childTask = getTaskByActivity(activity, dw);
			task.addChild(childTask);
		}
		for (WbsData child : wbs.getChilds()) {
			Task childTask = getTaskByWbs(child, dw);
			task.addChild(childTask);
		}
		return task;
	}

	private Task getTaskByActivity(ActivityData activity, DateWrapper dw) {
		Task task = new Task(activity.getName(), activity.getPnalStart(), 10,
				30, TaskType.LEAF);
		return task;
	}

	@Override
	public List<Dependency> getDependencies() {
		List<Dependency> list = new ArrayList<Dependency>();
		return list;
	}

}
