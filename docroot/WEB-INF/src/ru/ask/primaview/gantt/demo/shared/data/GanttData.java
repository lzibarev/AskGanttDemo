package ru.ask.primaview.gantt.demo.shared.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GanttData implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<WbsData> wbss;
	private String name;
	private Date dateStart;

	public GanttData() {
	}

	public GanttData(List<WbsData> list) {
		wbss = list;
	}

	public void setName(String value) {
		name = value.replace("/", "_");
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

	public void setDateStart(Date value) {
		dateStart = value;
	}

	public Date getDateStart() {
		return dateStart;
	}
}
