package ru.ask.primaview.gantt.demo.client.dummydata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gantt.client.config.GanttConfig.TaskType;
import com.sencha.gxt.core.client.util.DateWrapper;

public class DemoData2 implements IDemoData {

	public Task getTasks() {
		DateWrapper dw = new DateWrapper(new Date()).clearTime().addDays(-7);
		Task t = new Task("Project_X", dw.addDays(1).asDate(), 10, 30,
				TaskType.PARENT);
		Task t2 = new Task("Planning", dw.addDays(1).asDate(), 5, 40,
				TaskType.PARENT);
		t2.addChild(new Task("Prestudy", dw.addDays(1).asDate(), 2, 100,
				TaskType.LEAF));
		t2.addChild(new Task("Fesabillity_Study", dw.addDays(3).asDate(), 1,
				10, TaskType.LEAF));
		t2.addChild(new Task("Resource_allocation", dw.addDays(4).asDate(), 2,
				30, TaskType.LEAF));

		Task t3 = new Task("Execution", dw.addDays(6).asDate(), 5, 0,
				TaskType.PARENT);
		t3.addChild(new Task("Task-1", dw.addDays(6).asDate(), 1, 0,
				TaskType.LEAF));
		t3.addChild(new Task("Task-2", dw.addDays(7).asDate(), 4, 0,
				TaskType.LEAF));
		t3.addChild(new Task("Task-3", dw.addDays(7).asDate(), 2, 0,
				TaskType.LEAF));
		Task t4 = new Task("Task-4", dw.addDays(11).asDate(), 5, 0,
				TaskType.PARENT);
		t4.addChild(new Task("Subtask-1", dw.addDays(11).asDate(), 3, 0,
				TaskType.LEAF));
		t4.addChild(new Task("Subtask-2", dw.addDays(14).asDate(), 2, 0,
				TaskType.LEAF));
		t3.addChild(t4);
		t3.addChild(new Task("Task-5", dw.addDays(16).asDate(), 2, 0,
				TaskType.LEAF));

		t.addChild(t2);
		t.addChild(new Task("M2", dw.addDays(6).asDate(), 0, 0,
				TaskType.MILESTONE));
		t.addChild(t3);
		t.addChild(new Task("Project-End", dw.addDays(18).asDate(), 0, 0,
				TaskType.MILESTONE));

		ArrayList<Task> list = new ArrayList<Task>();
		list.add(t);
		Task root = new Task(list);
		return root;
	}

	// Create the dependencies
	public List<Dependency> getDependencies() {
		List<Dependency> list = new ArrayList<Dependency>();
		return list;
	}

}
