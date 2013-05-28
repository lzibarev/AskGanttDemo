package ru.ask.primaview.gantt.demo.client;

import ru.ask.primaview.gantt.demo.client.bacisgantt.GanttExample;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class AskGanttModule implements EntryPoint {

	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	public void onModuleLoad() {
		GanttExample gantt = new GanttExample();
		
		RootPanel.get("ganttDemo").add(gantt);
	}
}
