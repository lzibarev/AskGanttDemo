package ru.ask.primaview.gantt.demo.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.ask.primaview.gantt.demo.server.prima.PrimaCostants;
import ru.ask.primaview.gantt.demo.server.prima.PrimaveraService;
import ru.ask.primaview.gantt.demo.server.prima.utility.DataActivity;
import ru.ask.primaview.gantt.demo.server.prima.utility.DataProject;
import ru.ask.primaview.gantt.demo.server.prima.utility.DataWBS;
import ru.ask.primaview.gantt.demo.server.testutils.SerializeTempDataUtils;
import ru.ask.primaview.gantt.demo.shared.data.ActivityData;
import ru.ask.primaview.gantt.demo.shared.data.GanttData;
import ru.ask.primaview.gantt.demo.shared.data.WbsData;

public class PrimaveraDataServiceUtils {

	private static boolean offline = true;

	public static DataWBS[] getWbsArrayFromProject(int projectId, PrimaveraService service) {
		if (offline) {
			return SerializeTempDataUtils.getDataWbs(PrimaCostants.PROJECT_ID);
		}
		if (service == null)
			service = new PrimaveraService();
		DataWBS[] works = service.GetWBS(projectId);
		return works;
	}

	public static GanttData getFromProject(int projectId) {
		GanttData data = null;
		List<WbsData> list = new ArrayList<WbsData>();
		try {
			PrimaveraService service = null;
			String projectName;
			if (!offline) {
				service = new PrimaveraService();
				DataProject project = service.GetProject(projectId);
				projectName = project.getName();

			} else {
				projectName = "offlie";
			}

			System.out.println(projectName);
			System.out.println("start");
			DataWBS[] works = getWbsArrayFromProject(projectId, service);

			for (int i = 0; i < works.length; i++) {
				WbsData wbsData = getWbsData(works[i]);
				list.add(wbsData);
			}
			System.out.println("finish");
			data = new GanttData(list);
			data.setName(projectName);
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return data;

	}

	private static WbsData getWbsData(DataWBS wbs) {
		WbsData wbsData = new WbsData();
		wbsData.setName(wbs.getName() + " " + wbs.getId());
		if (wbs.hasActivities()) {
			for (DataActivity activity : wbs.getActivities()) {
				ActivityData activityData = new ActivityData();
				activityData.setName(activity.getName() + " " + activity.getId() + " " + activity.getBsStart() + " "
						+ activity.getBsFinish());
//				activityData.setPlanStart(activity.getBsStart());
				activityData.setPlanStart(new Date());
				activityData.setDuration(10);
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
