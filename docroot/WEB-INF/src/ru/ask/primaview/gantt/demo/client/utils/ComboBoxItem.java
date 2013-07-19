package ru.ask.primaview.gantt.demo.client.utils;

public class ComboBoxItem implements IComboBoxItem {

	private String value;
	private String name;

	public ComboBoxItem(String value, String name) {
		this.value = value;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}
