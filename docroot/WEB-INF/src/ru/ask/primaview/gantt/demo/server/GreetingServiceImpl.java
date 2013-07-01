package ru.ask.primaview.gantt.demo.server;

import ru.ask.primaview.gantt.demo.client.GreetingService;
import ru.ask.primaview.gantt.demo.shared.data.GanttData;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	@Override
	public GanttData getWbsDataList(String projectIdStr, boolean offline) throws IllegalArgumentException {
		System.out.println("service projectIdStr=" + projectIdStr+" offline="+offline);
		int projectId = Integer.parseInt(projectIdStr);
		GanttData data = PrimaveraDataServiceUtils.getFromProject(projectId, offline);
		return data;
	}

}
