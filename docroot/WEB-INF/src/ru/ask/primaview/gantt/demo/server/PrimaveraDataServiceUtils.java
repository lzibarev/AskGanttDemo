package ru.ask.primaview.gantt.demo.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.ask.primaview.gantt.demo.server.prima.PrimaContants;
import ru.ask.primaview.gantt.demo.server.prima.PrimaveraService;
import ru.ask.primaview.gantt.demo.server.prima.PrimaveraServiceUtils;
import ru.ask.primaview.gantt.demo.server.prima.utility.DataActivity;
import ru.ask.primaview.gantt.demo.server.prima.utility.DataProject;
import ru.ask.primaview.gantt.demo.server.prima.utility.DataWBS;
import ru.ask.primaview.gantt.demo.server.testutils.SerializeTempDataUtils;
import ru.ask.primaview.gantt.demo.shared.data.ActivityData;
import ru.ask.primaview.gantt.demo.shared.data.GanttData;
import ru.ask.primaview.gantt.demo.shared.data.ProjectData;
import ru.ask.primaview.gantt.demo.shared.data.WbsData;

public class PrimaveraDataServiceUtils {

	private static final boolean offline = true;

	public static List<ProjectData> getProjectsList() {
		List<ProjectData> list = null;
		if (offline) {
			list = new ArrayList<ProjectData>();
			ProjectData pdata = new ProjectData();
			pdata.setName("Уральская-Советская ж/д №8 561");
			pdata.setValue(PrimaContants.PROJECT_ID_STR);
			list.add(pdata);
			pdata = new ProjectData();
			pdata.setName("Test2");
			pdata.setValue(PrimaContants.PROJECT_ID_STR);
			list.add(pdata);
		} else {
			list = new ArrayList<ProjectData>();
			PrimaveraService service = new PrimaveraService();
			DataProject[] projects = service.GetProjectsByPortfolio(
					PrimaveraServiceUtils.projectsListPortfolio,
					PrimaveraServiceUtils.projectsListWhere,
					PrimaveraServiceUtils.projectsListOrder);
			for (DataProject dataProject : projects) {
				ProjectData pData = new ProjectData();
				String idStr = String.valueOf(dataProject.getId());
				pData.setName(dataProject.getName()+" "+idStr);
				pData.setValue(String.valueOf(dataProject.getId()));
				list.add(pData);
			}
		}
		return list;
	}

	public static DataWBS[] getWbsArrayFromProject(int projectId) {
		if (offline) {
			return SerializeTempDataUtils.getDataWbs(projectId);
		}
		PrimaveraService service = new PrimaveraService();
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
			DataWBS[] works = getWbsArrayFromProject(projectId);

			for (int i = 0; i < works.length; i++) {
				WbsData wbsData = getWbsData(works[i]);
				list.add(wbsData);
			}
			System.out.println("finish");
			data = new GanttData(list);
			data.setName(projectName);
			setMinMaxDate(data);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex);
		}
		return data;

	}

	// TODO: переделать так как дата может быть и в нижележащих уровнях. надо
	// переделать на сервис - экхемпляр
	private static void setMinMaxDate(GanttData data) {
		Date minDate = new Date();
		Date maxDate = new Date();
		for (WbsData wbsData : data.getWbss()) {
			if (!wbsData.isPlanStartNull() && minDate.after(wbsData.getPlanStart()))
				minDate = wbsData.getPlanStart();
			if (!wbsData.isPlanFinishNull() && maxDate.before(wbsData.getPlanFinish()))
				maxDate = wbsData.getPlanFinish();
		}
		data.setDateStart(minDate);
		data.setDateFinish(maxDate);

	}

	private static WbsData getWbsData(DataWBS wbs) {
		WbsData wbsData = new WbsData();
		wbsData.setName(String.format("%s (%d)", wbs.getName(), wbs.getId()));

		if (wbs.getBsStart() != null) {
			Date wbsPlanDate = new Date(wbs.getBsStart().getTime());
			wbsData.setPlanStart(wbsPlanDate);
		}
		if (wbs.getBsFinish() != null) {
			Date wbsPlanDate = new Date(wbs.getBsFinish().getTime());
			wbsData.setPlanFinish(wbsPlanDate);
		}
		long difference = wbsData.getPlanFinish().getTime() - wbsData.getPlanStart().getTime();
		int days = (int) (difference / (24 * 60 * 60 * 1000));
		wbsData.setDuration(days);
		wbsData.setComplite(wbs.getCompletePercent());
		if (wbs.hasActivities()) {
			for (DataActivity activity : wbs.getActivities()) {
				ActivityData activityData = new ActivityData();
				activityData.setName(String.format("%s (%d)", activity.getName(), activity.getId()));
				Date planStart = new Date(activity.getBsStart().getTime());
				Date planFinish = new Date(activity.getBsFinish().getTime());
				activityData.setPlanStart(planStart);
				activityData.setPlanFinish(planFinish);
				difference = planFinish.getTime() - planStart.getTime();
				days = (int) (difference / (24 * 60 * 60 * 1000));
				activityData.setDuration(days);
				activityData.setComplite(activity.getCompletePercent());
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
