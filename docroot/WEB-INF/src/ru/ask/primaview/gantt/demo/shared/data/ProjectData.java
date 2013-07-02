package ru.ask.primaview.gantt.demo.shared.data;

import java.io.Serializable;

public class ProjectData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int value;

	public String getName() {
		return name;
	}

	public void setName(String value) {
		name = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
