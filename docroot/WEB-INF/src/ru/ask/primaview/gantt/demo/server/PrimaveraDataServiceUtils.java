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
			DataWBS[] data = SerializeTempDataUtils.getDataWbs(PrimaCostants.PROJECT_ID);
			DataWBS[] result = new DataWBS[] { data[0], data[1], data[2], data[3]};
			return result;
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

			Date minDate = new Date();
			for (WbsData wbsData : list) {
				if (wbsData.getPnalStart() == null)
					continue;
				if (minDate.after(wbsData.getPnalStart()))
					minDate = wbsData.getPnalStart();
			}
			data.setDateStart(minDate);
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return data;

	}

	private static WbsData getWbsData(DataWBS wbs) {
		WbsData wbsData = new WbsData();
		wbsData.setName(wbs.getName() + " " + wbs.getId());

		if (wbs.getBsStart() != null) {
			System.out.println("not null bs");
			Date wbsPlanDate = new Date(wbs.getBsStart().getTime());
			wbsData.setPlanStart(wbsPlanDate);
		}
		if (wbs.getStart() != null) {
			System.out.println("not null");
		}
		wbsData.setDuration(10);
		if (wbs.hasActivities()) {
			for (DataActivity activity : wbs.getActivities()) {
				ActivityData activityData = new ActivityData();
				activityData.setName(activity.getName() + " " + activity.getId());
				Date planStart = new Date(activity.getBsStart().getTime());
				activityData.setPlanStart(planStart);
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
