package ru.ask.primaview.gantt.demo.server;

import ru.ask.primaview.gantt.demo.server.prima.PrimaContants;
import ru.ask.primaview.gantt.demo.server.prima.PrimaveraService;
import ru.ask.primaview.gantt.demo.server.prima.utility.DataActivity;
import ru.ask.primaview.gantt.demo.server.prima.utility.DataProject;
import ru.ask.primaview.gantt.demo.server.prima.utility.DataWBS;

public class PrimaUtilsTest {
	private static int offset;

	public static void main(String[] args) {
		GreetingServiceImpl service = new GreetingServiceImpl();
		service.getWbsDataList(PrimaContants.PROJECT_ID);
	}

	public static void main1(String[] args) {
		PrimaveraService service = new PrimaveraService();

		int projectId = 561;// Уральская-Советская ж/д №8 561

		DataProject project = service.GetProject(projectId);
		System.out.println(project.getName());
		System.out.println("start");
		DataWBS[] works = service.GetWBS(projectId);
		System.out.println(works.length);
		for (int i = 0; i < works.length; i++)
			addItem(works[i], 0);
		System.out.println("finish");
	}

	private static void printText(String format, Object... val) {
		String text = String.format(format, val);
		for (int i = 0; i < offset; i++) {
			text = "\t" + text;
		}
		System.out.println(text);
	}

	private static void addItem(DataWBS wbs, int projectId) {
		printText("wbsName= %s wbsID=%d projectId=%d", wbs.getName(), wbs.getId(), projectId);

		if (wbs.hasActivities()) {
			DataActivity[] works = wbs.getActivities();
			for (int i = 0; i < works.length; i++)
				addActivity(works[i], wbs.getId());
		}

		if (wbs.hasChildren()) {
			DataWBS[] children = wbs.getChildren();
			offset++;
			for (int i = 0; i < children.length; i++)
				addItem(children[i], wbs.getId());
			offset--;
		}
	}

	private static void addActivity(DataActivity activity, int wbsId) {
		printText("---->activity %s  wbsId = %d from %s to %s ", activity.getName(), wbsId, activity.getBsStart()
				.toString(), activity.getBsFinish());
	}

}
