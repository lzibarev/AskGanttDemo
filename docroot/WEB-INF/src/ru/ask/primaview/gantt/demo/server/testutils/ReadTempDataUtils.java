package ru.ask.primaview.gantt.demo.server.testutils;

import ru.ask.primaview.gantt.demo.server.prima.PrimaContants;
import ru.ask.primaview.gantt.demo.server.prima.utility.DataWBS;

public class ReadTempDataUtils {

	public static void main(String[] args) {
		DataWBS[] data = SerializeTempDataUtils.getDataWbs(PrimaContants.PROJECT_ID);
		System.out.println(data.length);
	}

}
