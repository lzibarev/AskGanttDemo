package ru.ask.primaview.gantt.demo.server.testutils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ru.ask.primaview.gantt.demo.server.PrimaveraDataServiceUtils;
import ru.ask.primaview.gantt.demo.server.PrimavraTestEmulator;
import ru.ask.primaview.gantt.demo.server.prima.PrimaCostants;
import ru.ask.primaview.gantt.demo.shared.data.GanttData;

public class SeriazileTempDataUtils {

	public static void serializeAndSaveGanttData(GanttData value) {
		try {
			ObjectOutputStream oos = null;
			try {
				String fileName = value.getName() + ".data";
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
		try {
			ObjectInputStream ois = null;
			try {
				String fileName = name + ".data";
				ois = new ObjectInputStream(new FileInputStream(fileName));
				Object obj = ois.readObject();
				if (obj instanceof GanttData)
					return (GanttData) obj;
			} finally {
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
		boolean testMode = true;
		GanttData data = null;
		if (testMode)
			data = PrimavraTestEmulator.getTempData();
		else
			data = PrimaveraDataServiceUtils.getFromProject(PrimaCostants.PROJECT_ID);
		testSerializationData(data);
	}
}
