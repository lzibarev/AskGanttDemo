package ru.ask.primaview.gantt.demo.server;

import java.util.ArrayList;
import java.util.List;

import ru.ask.primaview.gantt.demo.client.GreetingService;
import ru.ask.primaview.gantt.demo.server.prima.PrimaveraService;
import ru.ask.primaview.gantt.demo.server.prima.utility.DataActivity;
import ru.ask.primaview.gantt.demo.server.prima.utility.DataProject;
import ru.ask.primaview.gantt.demo.server.prima.utility.DataWBS;
import ru.ask.primaview.gantt.demo.shared.FieldVerifier;
import ru.ask.primaview.gantt.demo.shared.data.ActivityData;
import ru.ask.primaview.gantt.demo.shared.data.WbsData;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid.
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back
			// to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script
		// vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	@Override
	public List<WbsData> getWbsDataList(String name)
			throws IllegalArgumentException {
		boolean isTempData = false;
		System.out.println("service "+name);
		if (isTempData){
			return getTempData();
		}else{
			int projectId = 561;// Уральская-Советская ж/д №8 561
			return getFromProject(projectId);			
		}
	}

	private List<WbsData> getTempData() {
		List<WbsData> list = new ArrayList<WbsData>();

		WbsData data1 = new WbsData();
		data1.setName("test1");

		WbsData data2 = new WbsData();
		data2.setName("test2");
		WbsData data2_1 = new WbsData();
		data2_1.setName("test2_1");
		data2.addChild(data2_1);
		WbsData data2_2 = new WbsData();
		data2_2.setName("test2_2");
		data2.addChild(data2_2);

		WbsData data2_1_1 = new WbsData();
		data2_1_1.setName("test2_1_1");
		data2_1.addChild(data2_1_1);
		ActivityData data2_a1 = new ActivityData();
		data2_a1.setName("activity2_1");
		data2.addActivity(data2_a1);

		list.add(data1);
		list.add(data2);
		return list;
	}

	private List<WbsData> getFromProject(int projectId) {
		List<WbsData> list = new ArrayList<WbsData>();
		try {
			PrimaveraService service = new PrimaveraService();

			DataProject project = service.GetProject(projectId);
			System.out.println(project.getName());
			System.out.println("start");
			DataWBS[] works = service.GetWBS(projectId);

			for (int i = 0; i < works.length; i++) {
				WbsData wbsData = getWbsData(works[i]);
				list.add(wbsData);
			}
			System.out.println("finish");
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return list;

	}

	private WbsData getWbsData(DataWBS wbs) {
		WbsData wbsData = new WbsData();
		wbsData.setName(wbs.getName()+" "+wbs.getId());
		if (wbs.hasActivities()) {
			for (DataActivity activity : wbs.getActivities()) {
				ActivityData activityData = new ActivityData();
				activityData.setName(activity.getName()+" "+activity.getId()+" "+activity.getBsStart()+" "+activity.getBsFinish());
				activityData.setPlanStart(activity.getBsStart());
				activityData.setPlanFinish(activity.getBsFinish());
				wbsData.addActivity(activityData);
			}
		}
		if (wbs.hasChildren()) {
			for (DataWBS childWbs : wbs.getChildren()) {
				WbsData childWbsData = getWbsData(childWbs);
				wbsData.addChild(childWbsData);
			}
		}
		return wbsData;
	}
}
