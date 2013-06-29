package ru.ask.primaview.gantt.demo.server;

import ru.ask.primaview.gantt.demo.client.GreetingService;
import ru.ask.primaview.gantt.demo.server.prima.PrimaCostants;
import ru.ask.primaview.gantt.demo.shared.data.GanttData;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	@Override
	public GanttData getWbsDataList(String name) throws IllegalArgumentException {
		GanttData data;
		boolean isTempData = true;
		System.out.println("service " + name);
		if (isTempData) {
			data = PrimavraTestEmulator.getTempData();
		} else {
			data = PrimaveraDataServiceUtils.getFromProject(PrimaCostants.PROJECT_ID);
		}
		return data;
	}

}
