package ru.ask.primaview.gantt.demo.client;

import java.util.List;

import ru.ask.primaview.gantt.demo.client.bacisgantt.GanttExample;
import ru.ask.primaview.gantt.demo.shared.data.WbsData;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class AskGanttModule implements EntryPoint {

	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	
	
	public void onModuleLoad() {
//		final Label l = new Label();
//		l.setText("test1");
//		RootPanel.get("ganttDemo").add(l);
		greetingService.getWbsDataList("test2", new AsyncCallback<List<WbsData>>() {
			
			@Override
			public void onSuccess(List<WbsData> data) {
				GanttExample gantt = new GanttExample(data);
				RootPanel.get("ganttDemo").add(gantt);				
			}
			
			@Override
			public void onFailure(Throwable arg0) {
//				l.setText("error");
			}
		});
	}
	
}
