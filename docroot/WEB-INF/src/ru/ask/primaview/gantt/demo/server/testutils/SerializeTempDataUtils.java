package ru.ask.primaview.gantt.demo.server.testutils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ru.ask.primaview.gantt.demo.server.PrimaveraDataServiceUtils;
import ru.ask.primaview.gantt.demo.server.PrimavraTestEmulator;
import ru.ask.primaview.gantt.demo.server.prima.PrimaContants;
import ru.ask.primaview.gantt.demo.server.prima.utility.DataWBS;
import ru.ask.primaview.gantt.demo.shared.data.GanttData;

public class SerializeTempDataUtils {

	public static void serializeAndSaveGanttData(GanttData value) {
		String fileName = value.getName() + ".data";
		serializeAndSaveObject(value, fileName);
	}

	private static void serializeAndSaveObject(Object value, String fileName) {
		try {
			ObjectOutputStream oos = null;
			try {
				oos = new ObjectOutputStream(new FileOutputStream(fileName));
				oos.writeObject(value);
				System.out.println("write in " + fileName);
			} finally {
				oos.close();
			}

		} catch (FileNotFoundException fnfe) {
			System.err.println("cannot create a file with the given file name ");
		} catch (IOException ioe) {
			System.err.println("an I/O error occurred while processing the file");
		}
	}

	public static GanttData getGanttDataFromFile(String name) {
		String fileName = name + ".data";
		return (GanttData) getObjectFromFile(fileName);
	}

	private static Object getObjectFromFile(String fileName) {
		try {
			fileName = "/Develop/Liferay/liferay-plugins-sdk-6.1.1/portlets/AskGanttDemo-portlet/docroot/WEB-INF/classes/"
					+ fileName;
			ObjectInputStream ois = null;
			try {
				FileInputStream fis = new FileInputStream(fileName);
				ois = new ObjectInputStream(fis);
				Object obj = ois.readObject();
				return obj;
			} finally {
				if (ois != null)
					ois.close();
			}
		} catch (FileNotFoundException fnfe) {
			System.err.println("cannot create a file with the given file name ");
		} catch (IOException ioe) {
			System.err.println("an I/O error occurred while processing the file");
		} catch (ClassNotFoundException cnfe) {
			System.err.println("cannot recognize the class of the object - is the file corrupted?");
		}
		return null;

	}

	private static void testSerializationData(GanttData dataSource) {
		serializeAndSaveGanttData(dataSource);
		GanttData data = getGanttDataFromFile(dataSource.getName());
		System.out.println(data.getName());
	}

	public static void main(String[] args) {
		boolean sereilizeGantt = false;
		if (sereilizeGantt)
			serializeGanttData();
		else
			serializeSourceData();
	}

	public static String getSourceDataFileName(int programId) {
		return "wbs_project_" + programId + ".data";
	}

	private static void serializeSourceData() {
		System.out.println("SeriazileTempDataUtils.serializeSourceData()");
		String fileName = getSourceDataFileName(PrimaContants.PROJECT_ID);
		Object array = PrimaveraDataServiceUtils.getWbsArrayFromProject(PrimaContants.PROJECT_ID);
		serializeAndSaveObject(array, fileName);
		System.out.println("finish");
	}

	public static DataWBS[] getDataWbs(int projectId) {
		Object object = getObjectFromFile(getSourceDataFileName(projectId));
		if (object instanceof DataWBS[]) {
			return (DataWBS[]) object;
		}
		return null;
	}

	private static void serializeGanttData() {
		boolean testMode = false;
		GanttData data = null;
		if (testMode)
			data = PrimavraTestEmulator.getTempData();
		else
			data = PrimaveraDataServiceUtils.getFromProject(PrimaContants.PROJECT_ID);
		testSerializationData(data);
	}
}
