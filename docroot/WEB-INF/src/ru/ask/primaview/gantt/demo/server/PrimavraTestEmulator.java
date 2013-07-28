package ru.ask.primaview.gantt.demo.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.ask.primaview.gantt.demo.shared.data.ActivityData;
import ru.ask.primaview.gantt.demo.shared.data.GanttData;
import ru.ask.primaview.gantt.demo.shared.data.WbsData;

public class PrimavraTestEmulator {

	public static GanttData getTempData() {
		System.out.println("PrimavraTestEmulator.getTempData()");
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
		ActivityData data2_a1 = createDefaultActivityData("activity2_1");
		data2.addActivity(data2_a1);

		list.add(data1);
		list.add(data2);
		
		GanttData data = new GanttData(list);
		data.setName("Emulator");
		return data;
	}

	private static ActivityData createDefaultActivityData(String name) {
		ActivityData activityData = new ActivityData();
		activityData.setName(name);
		activityData.setPlanStart(new Date());
		activityData.setDuration(1);
		activityData.setComplite(0);
		return activityData;
	}

}
