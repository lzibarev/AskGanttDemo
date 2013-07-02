package ru.ask.primaview.gantt.demo.client;

import java.util.List;

import ru.ask.primaview.gantt.demo.shared.data.GanttData;
import ru.ask.primaview.gantt.demo.shared.data.ProjectData;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {

	GanttData getWbsDataList(int projectId) throws IllegalArgumentException;

	List<ProjectData> getProjectsList();
}
