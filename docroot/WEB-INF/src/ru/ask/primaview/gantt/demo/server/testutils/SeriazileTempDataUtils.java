package ru.ask.primaview.gantt.demo.server.testutils;

import java.io.*;

import ru.ask.primaview.gantt.demo.server.PrimavraTestEmulator;
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

	private static void testEmulationData() {
		GanttData dataSource = PrimavraTestEmulator.getTempData();
		serializeAndSaveGanttData(dataSource);
		GanttData data = getGanttDataFromFile(dataSource.getName());
		System.out.println(data.getName());
	}

	private static void testProjectData() {
		// TODO:
	}

	public static void main(String[] args) {
		testEmulationData();
	}
}
