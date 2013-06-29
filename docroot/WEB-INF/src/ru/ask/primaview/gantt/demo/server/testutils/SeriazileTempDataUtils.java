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
import ru.ask.primaview.gantt.demo.server.prima.utility.DataWBS;
import ru.ask.primaview.gantt.demo.shared.data.GanttData;

public class SeriazileTempDataUtils {

	public static void serializeAndSaveGanttData(GanttData value) {
		String fileName = value.getName() + ".data";
		serializeAndSaveObject(value, fileName);
	}
	
	private static void serializeAndSaveObject(Object value, String fileName){
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
		return (GanttData)getObjectFromFile(fileName);
	}
	
	private static Object getObjectFromFile(String fileName){
		try {
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(new FileInputStream(fileName));
				Object obj = ois.readObject();
				return obj;
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
		boolean sereilizeGantt = false;
		if (sereilizeGantt)
			serializeGanttData();
		else
			serializeSourceData();
	}
	
	private static void serializeSourceData(){
		System.out.println("SeriazileTempDataUtils.serializeSourceData()");;
		String fileName = "wbs_project_"+PrimaCostants.PROJECT_ID+".data";
		Object array = PrimaveraDataServiceUtils.getWbsArrayFromProject(PrimaCostants.PROJECT_ID, null);
		serializeAndSaveObject(array, fileName);
		Object object = getObjectFromFile(fileName);
		if (object instanceof DataWBS[]){
			System.out.println(((DataWBS[])object).length);
		}
	}
	
	private static void serializeGanttData(){
		boolean testMode = false;
		GanttData data = null;
		if (testMode)
			data = PrimavraTestEmulator.getTempData();
		else
			data = PrimaveraDataServiceUtils.getFromProject(PrimaCostants.PROJECT_ID);
		testSerializationData(data);		
	}
}
