package ru.ask.primaview.gantt.demo.client;

import java.util.List;

import ru.ask.primaview.gantt.demo.shared.data.WbsData;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void getWbsDataList(String input, AsyncCallback<List<WbsData>> callback)
			throws IllegalArgumentException;

}
