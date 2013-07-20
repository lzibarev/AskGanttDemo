package ru.ask.primaview.gantt.demo.shared.data;

import java.io.Serializable;

import ru.ask.primaview.gantt.demo.client.utils.IComboBoxItem;

public class ProjectData implements Serializable, IComboBoxItem {
	private static final long serialVersionUID = 1L;

	private String name;
	private String value;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String value) {
		name = value;
	}

	@Override
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
