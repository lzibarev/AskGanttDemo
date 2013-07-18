package ru.ask.primaview.gantt.demo.server;

import java.util.List;

import ru.ask.primaview.gantt.demo.client.GreetingService;
import ru.ask.primaview.gantt.demo.shared.data.GanttData;
import ru.ask.primaview.gantt.demo.shared.data.ProjectData;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	@Override
	public GanttData getWbsDataList(int projectId, String scale) throws IllegalArgumentException {
		System.out.println("GreetingServiceImpl.getWbsDataList() projectId=" + projectId);
		GanttData data = PrimaveraDataServiceUtils.getFromProject(projectId);
		data.setScale(scale);
		return data;
	}

	@Override
	public List<ProjectData> getProjectsList() {
		System.out.println("GreetingServiceImpl.getProjectsList()");
		List<ProjectData> projectList = PrimaveraDataServiceUtils.getProjectsList();
		return projectList;
	}

}
