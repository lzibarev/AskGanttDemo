package ru.ask.primaview.gantt.demo.shared.data;

import java.io.Serializable;
import java.util.Date;

public abstract class GraphData implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final Date MINDATE = new Date(0);

	protected String name;
	protected Date planStart;
	protected Date planFinish;
	protected int duration;
	protected int complite;

	public GraphData() {
		planStart = MINDATE;
		planFinish = MINDATE;
		duration = 0;
		complite = 0;
	}

	public String getName() {
		return name;
	}

	public void setPlanStart(Date value) {
		planStart = value;
	}

	public Date getPlanStart() {
		return planStart;
	}

	public boolean isPlanStartNull() {
		return planStart.equals(MINDATE);
	}

	public void setPlanFinish(Date value) {
		planFinish = value;
	}

	public Date getPlanFinish() {
		return planFinish;
	}

	public boolean isPlanFinishNull() {
		return planFinish.equals(MINDATE);
	}

	public void setDuration(int value) {
		duration = value;
	}

	public int getDuration() {
		return duration;
	}

	public void setName(String name) {
		this.name = name.replace("\"", "_");
	}

	public boolean isMilestone() {
		return planStart.equals(planFinish);
	}

	public int getComplite() {
		return complite;
	}

	public void setComplite(int value) {
		complite = value;
	}
}
