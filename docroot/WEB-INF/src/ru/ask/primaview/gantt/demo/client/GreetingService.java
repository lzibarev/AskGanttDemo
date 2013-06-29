package ru.ask.primaview.gantt.demo.client;

import ru.ask.primaview.gantt.demo.shared.data.GanttData;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {

	GanttData getWbsDataList(String name) throws IllegalArgumentException;
}
