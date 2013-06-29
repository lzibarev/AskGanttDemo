package ru.ask.primaview.gantt.demo.shared.data;

import java.io.Serializable;
import java.util.List;

public class GanttData implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<WbsData> wbss;
	private String name;

	public GanttData() {
	}

	public GanttData(List<WbsData> list) {
		wbss = list;
	}

	public void setName(String value) {
		name = value;
	}

	public String getName() {
		return name;
	}

	public void setWbss(List<WbsData> value) {
		wbss = value;
	}

	public List<WbsData> getWbss() {
		return wbss;
	}
}
