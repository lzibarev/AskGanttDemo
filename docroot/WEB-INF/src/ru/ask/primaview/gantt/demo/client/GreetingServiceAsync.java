package ru.ask.primaview.gantt.demo.client;

import ru.ask.primaview.gantt.demo.shared.data.GanttData;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {

	void getWbsDataList(String input, AsyncCallback<GanttData> callback) throws IllegalArgumentException;

}
