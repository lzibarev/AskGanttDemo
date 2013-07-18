package ru.ask.primaview.gantt.demo.client;

import java.util.List;

import ru.ask.primaview.gantt.demo.shared.data.GanttData;
import ru.ask.primaview.gantt.demo.shared.data.ProjectData;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {

	void getWbsDataList(int projectId, String scale, AsyncCallback<GanttData> callback)
			throws IllegalArgumentException;

	void getProjectsList(AsyncCallback<List<ProjectData>> callback);

}
