package ru.ask.primaview.gantt.demo.shared.data;

import java.io.Serializable;

public class ActivityData extends GraphData implements Serializable {
	private static final long serialVersionUID = 1L;


	public ActivityData() {
		planStart = MINDATE;
		planFinish = MINDATE;
		duration = 1;

	}
}
