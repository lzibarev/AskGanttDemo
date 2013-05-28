package ru.ask.primaview.gantt.demo.client.dummydata;

import java.util.List;

public interface IDemoData {

	Task getTasks();
	List<Dependency> getDependencies();
}
